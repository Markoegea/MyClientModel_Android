package com.kingmarco.myclientmodel.Auxiliary.Classes;

import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ClientObserver;
import com.kingmarco.myclientmodel.POJOs.Clients;

import java.util.ArrayList;
import java.util.List;

public class ClientHolder {

    private static Clients youClient;
    private static final List<ClientObserver> observers = new ArrayList<>();

    public static void setYouClient(Clients value){
        youClient = value;
        notifyObservers();
    }

    public static Clients getYouClient(){
        return youClient;
    }

    public static void addObserver(ClientObserver observer){
        observers.add(observer);
    }

   public static void removeObserver(ClientObserver observer){
        observers.remove(observer);
   }

   private static void notifyObservers(){
        for (ClientObserver observer: observers){
            observer.onVariableChange(youClient);
        }
   }
}
