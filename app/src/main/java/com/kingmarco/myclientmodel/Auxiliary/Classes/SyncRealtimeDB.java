package com.kingmarco.myclientmodel.Auxiliary.Classes;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kingmarco.myclientmodel.Auxiliary.Enums.MessagesStatus;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetRealtimeDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.NotificationTemplate;
import com.kingmarco.myclientmodel.POJOs.Chats;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.POJOs.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncRealtimeDB {
    private static SyncRealtimeDB sharedInstance;
    private DatabaseReference destinyReference;
    private ValueEventListener eventListener;
    private boolean isListening = false;
    private Chats myChat;
    private final ArrayList<Messages> allMessages = new ArrayList<>();
    private final ArrayList<GetRealtimeDB> observers = new ArrayList<>();

    public static SyncRealtimeDB getInstance(){
        if(sharedInstance == null){
            sharedInstance = new SyncRealtimeDB();
        }
        return sharedInstance;
    }

    public SyncRealtimeDB() {
    }

    public void listening(@NonNull DatabaseReference destinyReference,
                          @NonNull NotificationTemplate notificationTemplate){
        this.destinyReference = destinyReference;
        this.eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!SyncAuthDB.getInstance().isLogin()){return;}
                if(ClientHolder.getYouClient() == null){return;}
                if (!dataSnapshot.exists()){
                    newChatField(destinyReference);
                    return;
                }
                if (myChat == null){
                    myChat = new Chats();
                }
                myChat.setClientID(dataSnapshot.child("clientID").getValue(Integer.class));
                myChat.setImage(dataSnapshot.child("image").getValue(String.class));
                myChat.setName(dataSnapshot.child("name").getValue(String.class));
                DatabaseReference lastMessage = dataSnapshot.child("messages").getRef();
                lastMessage.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        allMessages.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Messages message = dataSnapshot.getValue(Messages.class);
                            if(message == null){return;}
                            message.getTimestamp().newDateFormat();
                            message.setId(dataSnapshot.getKey());
                            allMessages.add(message);
                            myChat.setMessages(message);
                        }
                        notifyObservers();
                        sendNotifications(notificationTemplate);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        notifyObservers(SnackBarsInfo.MESSAGES_ERROR);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                notifyObservers(SnackBarsInfo.CHATS_ERROR);
            }
        };
    }

    private void newChatField(DatabaseReference databaseReference){
        Clients client = ClientHolder.getYouClient();
        Chats newChat = new Chats(client.getMessagingId(),client.getImageUrl(),client.getName());
        myChat = newChat;
        databaseReference.setValue(newChat);
    }

    public void stopListening(){
        if(this.destinyReference == null){return;}
        this.destinyReference.removeEventListener(this.eventListener);
        isListening = false;
    }

    public void startListening(){
        if(this.destinyReference == null){return;}
        if(isListening){return;}
        this.destinyReference.addValueEventListener(this.eventListener);
        isListening = true;
    }

    public void addObserver(GetRealtimeDB observer){
        observers.add(observer);
        notifyObservers();
    }

    public void removeObserver(GetRealtimeDB observer){
        observers.remove(observer);
    }

    private void notifyObservers(){
        for (GetRealtimeDB observer: observers){
            observer.getSyncRealtimeDB(allMessages);
        }
    }

    private void notifyObservers(SnackBarsInfo snackBarsInfo){
        for (GetRealtimeDB observer: observers){
            observer.getSyncRealtimeDB(snackBarsInfo);
        }
    }

    public Chats getMyChat(){
        return myChat;
    }

    public void uploadChat(String information, String field){
        if (destinyReference == null || myChat == null){return;}
        destinyReference.child(field).setValue(information);
    }

    private void sendNotifications(NotificationTemplate notificationTemplate){
        Messages message = myChat.getMessages();
        if(message == null){return;}
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("chats/"+myChat.getClientID()+"/messages");
        if(message.getSenderId() != ClientHolder.getYouClient().getMessagingId()){
            if (message.getStatus() == MessagesStatus.SENT){
                message.setStatus(MessagesStatus.RECEIVED);
                String title = "Nuevo Mensaje de "+message.getSenderName();
                String text = message.getText();
                int clientId = message.getSenderId();
                notificationTemplate.newMessagesNotification(
                        title,
                        text,
                        clientId,
                        message.getImageUrl()
                );
                databaseReference.child(message.getId()).setValue(message);
            } else if (message.getStatus() == MessagesStatus.SEEN){
                notificationTemplate.deleteMessagesNotification(message.getSenderId());
            }
        }
        notificationTemplate.createMessageSummary();
    }
}
