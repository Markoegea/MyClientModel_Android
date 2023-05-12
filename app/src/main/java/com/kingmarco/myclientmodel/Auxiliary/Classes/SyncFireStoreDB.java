package com.kingmarco.myclientmodel.Auxiliary.Classes;

import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.kingmarco.myclientmodel.Adapters.CartsAdapter;
import com.kingmarco.myclientmodel.Adapters.StockAdapter;

import java.util.ArrayList;

public class SyncFireStoreDB {
    public static <T> ListenerRegistration newListenerRegistration(StockAdapter stockAdapter,
                                                                   Class<T> parcelableClass,
                                                                   String collectionPath){
        ArrayList<Parcelable> allParcelable = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference parcelableDB = db.collection(collectionPath);
        return parcelableDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null){return;}
                allParcelable.clear();
                for (DocumentSnapshot document: value.getDocuments()){
                    T parcelable = document.toObject(parcelableClass);
                    if (!(parcelable instanceof Parcelable)){continue;}
                    allParcelable.add((Parcelable) parcelable);
                }
                stockAdapter.setDatabase(allParcelable);
            }
        });
    }
    public static <T> ListenerRegistration newListenerRegistration(CartsAdapter cartsAdapter,
                                                                   Class<T> parcelableClass,
                                                                   String collectionPath){
        if(!SyncAuthDB.getInstance().isLogin()){return null;}
        if(ClientHolder.getYouClient() == null){
            return null;
        }
        ArrayList<Parcelable> allParcelable = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference parcelableDB = db.collection(collectionPath);
        return parcelableDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null){return;}
                if(!SyncAuthDB.getInstance().isLogin()){return;}
                allParcelable.clear();
                for (DocumentSnapshot document: value.getDocuments()){
                    T parcelable = document.toObject(parcelableClass);
                    if (!(parcelable instanceof Parcelable)){continue;}
                    allParcelable.add((Parcelable) parcelable);
                }
                //cartsAdapter.setDatabase(allParcelable);
            }
        });
    }
}
