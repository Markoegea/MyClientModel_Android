package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Stock {
    private Long id;
    private String name;
    private int price;
    private ArrayList<String> url;
    private String lastUpdateBy;

    public Stock() {
    }

    public Stock(Long id, String name, int price, ArrayList<String> url, String lastUpdateBy) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.lastUpdateBy = lastUpdateBy;
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
