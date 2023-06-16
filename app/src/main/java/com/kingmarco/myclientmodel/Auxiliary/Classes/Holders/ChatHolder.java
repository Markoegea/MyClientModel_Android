package com.kingmarco.myclientmodel.Auxiliary.Classes.Holders;

import com.google.firebase.database.DatabaseReference;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ChatObserver;
import com.kingmarco.myclientmodel.POJOs.Chats;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.POJOs.Messages;

import java.util.ArrayList;

public class ChatHolder {
    private static Chats myChat;

    private static final ArrayList<Messages> myMessages = new ArrayList<>();

    private static final ArrayList<ChatObserver> observers = new ArrayList<>();

    public static void addChat(Chats chats){
        myChat = chats;
    }

    public static void newChat(DatabaseReference databaseReference){
        Clients client = ClientHolder.getYouClient();
        Chats newChat = new Chats(client.getMessagingId(),client.getImageUrl(),client.getName());
        myChat = newChat;
        databaseReference.setValue(newChat);
    }

    public static Chats getChat(){
        return myChat;
    }

    public static void setChatNull(){
        myChat = null;
    }

    public static void addSingleMessage(Messages messages){
        myMessages.add(messages);
    }

    public static void addMessagesList(ArrayList<Messages> messages){
        myMessages.addAll(messages);
    }

    public static void clearMessagesList(){
        myMessages.clear();
    }

    public static ArrayList<Messages> getMyMessages(){
        return myMessages;
    }

    public static void addObserver(ChatObserver observer){
        observers.add(observer);
        notifyObserversMessages();
    }

    public static void removeObserver(ChatObserver observer){
        observers.remove(observer);
    }

    public static void notifyObserversMessages(){
        for (ChatObserver observer: observers){
            observer.onMessagesChange(myMessages);
        }
    }
}
