package com.kingmarco.myclientmodel.Auxiliary.Classes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetFireStoreDB;
import com.kingmarco.myclientmodel.POJOs.Carts;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;
import com.kingmarco.myclientmodel.POJOs.Stock;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;
import java.util.Calendar;

public class SyncFireStoreDB {
    public static ListenerRegistration newCartListenerRegistration(StockType stockType,
                                                                   String collectionPath){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference stockDB = db.collection(collectionPath);
        if(stockType == StockType.PRODUCT){
            registerProductListener(stockDB);
        } else if (stockType == StockType.PROMOTION){
            registerPromotionListener(stockDB);
        }
        return null;
    }

    private static ListenerRegistration registerProductListener(CollectionReference stockDB){
        return stockDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null){return;}
                StockHolder.clearInStockList(StockType.PRODUCT);
                for (DocumentSnapshot document: value.getDocuments()){
                    Products parcelable = document.toObject(Products.class);
                    StockHolder.addInStockList(StockType.PRODUCT,parcelable);
                }
            }
        });
    }

    private static ListenerRegistration registerPromotionListener(CollectionReference stockDB){
        return stockDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null){return;}
                StockHolder.clearInStockList(StockType.PROMOTION);
                for (DocumentSnapshot document: value.getDocuments()){
                    Promotions parcelable = document.toObject(Promotions.class);
                    StockHolder.addInStockList(StockType.PROMOTION,parcelable);
                }
            }
        });
    }


    /**Cart Firebase Methods*/
    public static ListenerRegistration newCartListenerRegistration(){
        if(!SyncAuthDB.getInstance().isLogin()){return null;}
        if(ClientHolder.getYouClient() == null){return null;}
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        return db.collection("Carritos")
                .document(ClientHolder.getYouClient().getId())
                .collection(ClientHolder.getYouClient().getId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value == null){return;}
                        if(!SyncAuthDB.getInstance().isLogin()){return;}
                        CartHolder.clearCartList();
                        for (DocumentSnapshot document: value.getDocuments()){
                            Carts carts = document.toObject(Carts.class);
                            if (carts == null){continue;}
                            CartHolder.addSingleCartListItem(carts);
                        }
                    }
                });
    }

    public static void updateCartRequest(Carts carts, GetFireStoreDB getFireStoreDB){
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        if(ClientHolder.getYouClient() == null){return;}
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Carritos")
                .document(ClientHolder.getYouClient().getId())
                .collection(ClientHolder.getYouClient().getId())
                .document(carts.getId().toString())
                .set(carts).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.UPLOAD_SUCCESS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.DATA_ERROR);
                    }
                });
    }

    public static void uploadCartItemsRequest(Stock stock, StockType stockType, int quantity, GetFireStoreDB getFireStoreDB, Fragment fragment){
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        if(ClientHolder.getYouClient() == null){return;}
        Carts cart = CartHolder.getDraftCartItem();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (cart.getId() == null || cart.getClientId().isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            Long longId = Long.parseLong(String.format("%d%d%d%d%d%d", year, month, day, hour, minute, second));
            cart.setId(longId);
            cart.setClientId(ClientHolder.getYouClient().getId());
        }

        ArrayList<Long> stockList = cart.getPurchasedItemsId().get(stockType.name());
        if (stockList == null){return;}
        for (int i = 0; i<quantity;i++){
            stockList.add(stock.getId());
            cart.setTotalPrice(cart.getTotalPrice()+stock.getPrice());
        }

        cart.setLastUpdateBy(ClientHolder.getYouClient().getName()+
                ", "+
                ClientHolder.getYouClient().getLastName());
        DocumentReference documentReference = db.collection("Carritos")
                .document(ClientHolder.getYouClient().getId())
                .collection(ClientHolder.getYouClient().getId())
                .document(cart.getId().toString());
        documentReference.set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.navigate(R.id.cartFragment);
                getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.UPLOAD_SUCCESS);
            }
        }). addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.DATA_ERROR);
            }
        });
    }

    public static void deleteCartRequest(Carts carts, GetFireStoreDB getFireStoreDB, Fragment fragment){
        if(!SyncAuthDB.getInstance().isLogin()){return;}
        if(ClientHolder.getYouClient() == null){return;}
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("Carritos")
                .document(ClientHolder.getYouClient().getId())
                .collection(ClientHolder.getYouClient().getId())
                .document(carts.getId().toString());
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.DELETE_SUCCESS);
                }else {
                    getFireStoreDB.onCompleteFireStoreRequest(SnackBarsInfo.DELETE_ERROR);
                }
            }
        });
    }
}
