package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.TimestampDeserializer;
import com.kingmarco.myclientmodel.Auxiliary.Enums.CartStatus;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**The class cart that is used to organize the information of the cart*/
public class Carts {

    private Long id;
    private Map<String,ArrayList<Long>> purchasedItemsId;
    private String clientId;
    private int totalPrice;
    private CartStatus status;
    private TimestampDeserializer purchasedDate;
    private TimestampDeserializer arrivedDate;
    private String lastUpdateBy;
    public Carts() {
    }

    public Carts(CartStatus status) {
        this.status = status;
        this.purchasedItemsId = new HashMap<>();

    }

    public Carts(Long id,
                 Map<String, ArrayList<Long>> purchasedItemsId,
                 String clientID,
                 int totalPrice,
                 CartStatus status,
                 TimestampDeserializer purchasedDate,
                 TimestampDeserializer arrivedDate) {
        this.id = id;
        this.purchasedItemsId = purchasedItemsId;
        this.clientId = clientID;
        this.totalPrice = totalPrice;
        this.status = status;
        this.purchasedDate = purchasedDate;
        this.arrivedDate = arrivedDate;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, ArrayList<Long>> getPurchasedItemsId() {
        return purchasedItemsId;
    }

    public void setPurchasedItemsId(Map<String, ArrayList<Long>> purchasedItemsId) {
        this.purchasedItemsId = purchasedItemsId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CartStatus getStatus() {
        return status;
    }

    public void setStatus(CartStatus status) {
        this.status = status;
    }

    public TimestampDeserializer getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(TimestampDeserializer purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public TimestampDeserializer getArrivedDate() {
        return arrivedDate;
    }

    public void setArrivedDate(TimestampDeserializer arrivedDate) {
        this.arrivedDate = arrivedDate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
