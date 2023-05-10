package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**The class cart that is used to organize the information of the cart and parcel it to pass to others fragments*/
public class Carts implements Parcelable {

    private int id;
    private ArrayList<Parcelable> purchasedProducts;
    private Clients buyerClient;
    private String owner;
    private int price;
    private String status;
    private String direction;
    private LocalDate purchasedDate;
    private LocalDate arrivedDate;

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

    public Carts(int id,ArrayList<Parcelable> purchasedProducts, Clients buyerClient, String owner, int price, String status, String direction, LocalDate purchasedDate,LocalDate arrivedDate) {
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
        this.id = in.readInt();
        this.purchasedProducts = new ArrayList<>();
        this.purchasedProducts.addAll(Arrays.asList(in.readParcelableArray(this.purchasedProducts.getClass().getClassLoader())));
        this.buyerClient = in.readParcelable(this.buyerClient.getClass().getClassLoader());
        this.owner = in.readString();
        this.price = in.readInt();
        this.status = in.readString();
        this.direction = in.readString();
        this.purchasedDate = LocalDate.ofEpochDay(in.readLong());
        this.arrivedDate = LocalDate.ofEpochDay(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeParcelableArray(this.purchasedProducts.toArray(new Parcelable[0]),i);
        parcel.writeParcelable(this.buyerClient,i);
        parcel.writeString(this.owner);
        parcel.writeInt(this.price);
        parcel.writeString(this.status);
        parcel.writeString(this.direction);
        parcel.writeLong(this.purchasedDate.toEpochDay());
        parcel.writeLong(this.arrivedDate.toEpochDay());
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Parcelable> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Parcelable> purchasedProducts) {
        this.purchasedProducts = purchasedProducts;
    }

    public Clients getBuyerClient() {
        return buyerClient;
    }

    public void setBuyerClient(Clients buyerClient) {
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

    public LocalDate getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(LocalDate purchasedDate) {
        this.purchasedDate = purchasedDate;
    }

    public LocalDate getArrivedDate() {
        return arrivedDate;
    }

    public void setArrivedDate(LocalDate arrivedDate) {
        this.arrivedDate = arrivedDate;
    }
}
