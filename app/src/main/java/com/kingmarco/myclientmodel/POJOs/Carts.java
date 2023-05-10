package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**The class cart that is used to organize the information of the cart and parcel it to pass to others fragments*/
public class Carts implements Parcelable {

    private Long id;
    private ArrayList<String> purchasedProducts;
    private Long buyerClient;
    private String owner;
    private int price;
    private String status;
    private String direction;
    private Timestamp purchasedDate;
    private Timestamp arrivedDate;
    private String lastUpdateBy;

    public static final Creator<Carts> CREATOR = new Creator<>() {
        @Override
        public Carts createFromParcel(Parcel parcel) {
            return new Carts(parcel);
        }

        @Override
        public Carts[] newArray(int i) {
            return new Carts[0];
        }
    };

    public Carts(Long id,ArrayList<String> purchasedProducts, Long buyerClient, String owner, int price, String status, String direction, Timestamp purchasedDate, Timestamp arrivedDate) {
        this.id = id;
        this.purchasedProducts = purchasedProducts;
        this.buyerClient = buyerClient;
        this.owner = owner;
        this.price = price;
        this.status = status;
        this.direction = direction;
        this.purchasedDate = purchasedDate;
        this.arrivedDate = arrivedDate;
    }

    protected Carts(Parcel in){
        this.id = in.readLong();
        in.readList(this.purchasedProducts, String.class.getClassLoader());
        this.buyerClient = in.readLong();
        this.owner = in.readString();
        this.price = in.readInt();
        this.status = in.readString();
        this.direction = in.readString();
        this.purchasedDate = in.readParcelable(Timestamp.class.getClassLoader());
        this.arrivedDate = in.readParcelable(Timestamp.class.getClassLoader());
        this.lastUpdateBy = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeList(this.purchasedProducts);
        parcel.writeLong(this.buyerClient);
        parcel.writeString(this.owner);
        parcel.writeInt(this.price);
        parcel.writeString(this.status);
        parcel.writeString(this.direction);
        this.purchasedDate.writeToParcel(parcel, i);
        this.arrivedDate.writeToParcel(parcel, i);
        parcel.writeString(this.lastUpdateBy);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<String> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<String> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public Long getBuyerClient() {
        return buyerClient;
    }

    public void setBuyerClient(Long buyerClient) {
        this.buyerClient = buyerClient;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Timestamp getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(Timestamp purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public Timestamp getArrivedDate() {
        return arrivedDate;
    }

    public void setArrivedDate(Timestamp arrivedDate) {
        this.arrivedDate = arrivedDate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
