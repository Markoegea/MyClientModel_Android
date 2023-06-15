package com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.CartHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ChatHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.NotificationManager;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetRealtimeDB;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.POJOs.Chats;
import com.kingmarco.myclientmodel.POJOs.Messages;
import com.kingmarco.myclientmodel.POJOs.Stock;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;
import java.util.UUID;

public class SyncRealtimeDB {

    public static ValueEventListener newChatListener(@NonNull DatabaseReference destinyReference,
                                                     @NonNull GetRealtimeDB getRealtimeDB){
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!SyncAuthDB.getInstance().isLogin()){return;}
                if(ClientHolder.getYouClient() == null){return;}
                if (!dataSnapshot.exists()){
                    ChatHolder.newChat(destinyReference);
                    return;
                }
                if (ChatHolder.getChat() == null){
                    ChatHolder.addChat(new Chats());
                }
                ChatHolder.getChat().setClientID(dataSnapshot.child("clientID").getValue(Integer.class));
                ChatHolder.getChat().setImage(dataSnapshot.child("image").getValue(String.class));
                ChatHolder.getChat().setName(dataSnapshot.child("name").getValue(String.class));
                DatabaseReference lastMessage = dataSnapshot.child("messages").getRef();
                lastMessage.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ChatHolder.clearMessagesList();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Messages message = dataSnapshot.getValue(Messages.class);
                            if(message == null){return;}
                            message.setId(dataSnapshot.getKey());
                            ChatHolder.addSingleMessage(message);
                            ChatHolder.getChat().setMessages(message);
                            NotificationManager.getInstance().sendNotifications();
                        }
                        ChatHolder.notifyObserversMessages();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.MESSAGES_ERROR);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.CHATS_ERROR);
            }
        };
    }

    /**Cart Firebase Methods*/
    public static ValueEventListener newCartListener(@NonNull GetRealtimeDB getRealtimeDB){
        if(!SyncAuthDB.getInstance().isLogin()){return null;}
        if(ClientHolder.getYouClient() == null){return null;}
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!SyncAuthDB.getInstance().isLogin()){return;}
                if(ClientHolder.getYouClient() == null){return;}
                CartHolder.clearCartList();
                if (!snapshot.exists()){return;}
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Carts newCart = dataSnapshot.getValue(Carts.class);
                    if (newCart == null){continue;}
                    CartHolder.addSingleCartListItem(newCart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.CARTS_ERROR);
            }
        };
    }

    public static void newCartItemsRequest(Stock stock, StockType stockType, int quantity, GetRealtimeDB getRealtimeDB, Fragment fragment){
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        if(ClientHolder.getYouClient() == null){return;}
        Carts cart = CartHolder.getDraftCartItem();

        if (cart.getId() == null || cart.getClientId().isEmpty()) {
            int id = UUID.randomUUID().hashCode();
            cart.setId(id);
            cart.setClientId(ClientHolder.getYouClient().getId());
        }

        ArrayList<Long> stockList = cart.getPurchasedItemsId().get(stockType.name());
        if (stockList == null){
            stockList = new ArrayList<>();
            cart.getPurchasedItemsId().put(stockType.name(),stockList);
        }
        for (int i = 0; i<quantity;i++){
            stockList.add(stock.getId());
            cart.setTotalPrice(cart.getTotalPrice()+stock.getPrice());
        }
        cart.setLastUpdateBy(ClientHolder.getYouClient().getName()+
                ", "+
                ClientHolder.getYouClient().getLastName());

        String path = "Carritos/"+ClientHolder.getYouClient().getId()+"/"+cart.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(path);
        databaseReference.setValue(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(R.id.cartFragment);
                getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.UPLOAD_SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.DATA_ERROR);
            }
        });
    }

    public static void updateCartRequest(@NonNull Carts carts,
                                         @NonNull GetRealtimeDB getRealtimeDB){
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        if(ClientHolder.getYouClient() == null){return;}
        String path = "Carritos/"+ClientHolder.getYouClient().getId()+"/"+carts.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(path);
        databaseReference.setValue(carts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.UPLOAD_SUCCESS);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.DATA_ERROR);
                    }
                });
    }

    public static void deleteCartRequest(Carts carts, GetRealtimeDB getRealtimeDB){
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        if(ClientHolder.getYouClient() == null){return;}

        String path = "Carritos/"+ClientHolder.getYouClient().getId()+"/"+carts.getId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(path);
        databaseReference.setValue(null)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.DELETE_SUCCESS);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getRealtimeDB.getSyncRealtimeDB(SnackBarsInfo.DELETE_ERROR);
                    }
                });
    }

    public static void stopListening(DatabaseReference destinyReference, ValueEventListener eventListener){
        if(destinyReference == null || eventListener == null){return;}
        destinyReference.removeEventListener(eventListener);
    }

    public static void startListening(DatabaseReference destinyReference, ValueEventListener eventListener){
        if(destinyReference == null || eventListener == null){return;}
        destinyReference.addValueEventListener(eventListener);
    }

    public static void uploadChat(DatabaseReference destinyReference, String information, String field){
        if (destinyReference == null){return;}
        destinyReference.child(field).setValue(information);
    }
}
