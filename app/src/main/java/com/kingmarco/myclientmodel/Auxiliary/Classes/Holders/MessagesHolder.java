package com.kingmarco.myclientmodel.Auxiliary.Classes.Holders;

import com.kingmarco.myclientmodel.Auxiliary.Interfaces.MessagesObserver;
import com.kingmarco.myclientmodel.POJOs.Messages;

import java.util.ArrayList;

public class MessagesHolder {

    private static final ArrayList<Messages> myMessages = new ArrayList<>();

    private static final ArrayList<MessagesObserver> observers = new ArrayList<>();

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

    public static void addObserver(MessagesObserver observer){
        observers.add(observer);
        notifyObserversMessages();
    }

    public static void removeObserver(MessagesObserver observer){
        observers.remove(observer);
    }

    public static void notifyObserversMessages(){
        for (MessagesObserver observer: observers){
            observer.onMessagesChange(myMessages);
        }
    }
}
