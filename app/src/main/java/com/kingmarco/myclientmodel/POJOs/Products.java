package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**The class Products that is used to organize the information of the Product parcel it to other fragments*/

public class Products  extends Stock {

    private int quantity;
    private String description;
    private ArrayList<String> rating;
    private ArrayList<String> category;

    public Products(Long id, int price, String name, ArrayList<String> url, String lastUpdateBy, int quantity, String description, ArrayList<String> rating, ArrayList<String> category) {
        super(id, name, price, url, lastUpdateBy);
        this.quantity = quantity;
        this.description = description;
        this.rating = rating;
        this.category = category;
    }

    public Products() {
        super();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getRating() {
        return rating;
    }

    public void setRating(ArrayList<String> rating) {
        this.rating = rating;
    }
}
