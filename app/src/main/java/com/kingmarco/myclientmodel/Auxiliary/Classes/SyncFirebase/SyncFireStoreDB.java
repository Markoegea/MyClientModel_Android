package com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.StockHolder;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.POJOs.Products;
import com.kingmarco.myclientmodel.POJOs.Promotions;

public class SyncFireStoreDB {
    public static ListenerRegistration newStockListenerRegistration(StockType stockType,
                                                                    String collectionPath){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference stockDB = db.collection(collectionPath);
        if(stockType == StockType.PRODUCT){
            return registerProductListener(stockDB);
        } else if (stockType == StockType.PROMOTION){
            return registerPromotionListener(stockDB);
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
                    Products products = document.toObject(Products.class);
                    StockHolder.addInStockList(StockType.PRODUCT,products);
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
                    Promotions promotions = document.toObject(Promotions.class);
                    StockHolder.addInStockList(StockType.PROMOTION,promotions);
                }
            }
        });
    }
}
