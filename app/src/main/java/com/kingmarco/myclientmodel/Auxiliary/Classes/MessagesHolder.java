package com.kingmarco.myclientmodel.Auxiliary.Classes;

import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetRealtimeDB;
import com.kingmarco.myclientmodel.POJOs.Messages;

import java.util.ArrayList;

public class MessagesHolder {

    private static final ArrayList<Messages> allMessages = new ArrayList<>();
    private static final ArrayList<GetRealtimeDB> observers = new ArrayList<>();

    public static void clearMessageList(){
        allMessages.clear();
    }

    public static void addMessageToList(Messages messages){
        allMessages.add(messages);
    }

    public static void addObserver(GetRealtimeDB observer){
        observers.add(observer);
        notifyObservers();
    }

    public static void removeObserver(GetRealtimeDB observer){
        observers.remove(observer);
    }

    public static void notifyObservers(){
        for (GetRealtimeDB observer: observers){
            observer.getSyncRealtimeDB(allMessages);
        }
    }

    public static void notifyObservers(SnackBarsInfo snackBarsInfo){
        for (GetRealtimeDB observer: observers){
            observer.getSyncRealtimeDB(snackBarsInfo);
        }
    }

}
