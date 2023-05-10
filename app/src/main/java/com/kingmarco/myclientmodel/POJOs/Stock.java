package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Stock implements Parcelable {
    private Long id;
    private String name;
    private int price;
    private ArrayList<String> url;
    private String lastUpdateBy;

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel parcel) {
            return new Stock(parcel);
        }

        @Override
        public Stock[] newArray(int i) {
            return new Stock[i];
        }
    };

    public Stock() {
    }

    public Stock(Long id, String name, int price, ArrayList<String> url, String lastUpdateBy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.lastUpdateBy = lastUpdateBy;
    }

    protected Stock(Parcel in){
        this.id = in.readLong();
        this.name = in.readString();
        this.price = in.readInt();
        this.url = in.createStringArrayList();
        this.lastUpdateBy = in.readString();
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.price);
        parcel.writeStringList(this.url);
        parcel.writeString(this.lastUpdateBy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public void setUrl(ArrayList<String> url) {
        this.url = url;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
