package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**The class Products that is used to organize the information of the Product parcel it to other fragments*/

public class Products implements Parcelable {

    //TODO: WHEN GET PRICE, DON'T GET A STRING INSTEAD A INT
    //TODO: DO THE SAME WITH QUANTITY
    private int id;
    private int price;
    private int quantity;
    private String name;
    private String description;
    private ArrayList<String> url;
    private ArrayList<String> rating;
    private ArrayList<String> category;

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel parcel) {
            return new Products(parcel);
        }

        @Override
        public Products[] newArray(int i) {
            return new Products[i];
        }
    };

    public Products(int id, int price, int quantity, String name, String description, ArrayList<String> url, ArrayList<String> rating, ArrayList<String> category) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.url = url;
        this.rating = rating;
        this.category = category;
    }

    protected Products(Parcel in){
        this.id = in.readInt();
        this.price = in.readInt();
        this.quantity = in.readInt();
        this.name = in.readString();
        this.description = in.readString();
        in.readStringList(this.url);
        in.readStringList(this.rating);
        in.readStringList(this.category);
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeInt(this.price);
        parcel.writeInt(this.quantity);
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeStringList(this.url);
        parcel.writeStringList(this.rating);
        parcel.writeStringList(this.category);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getUrl() {
        return url;
    }

    public void setUrl(ArrayList<String> url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return new DecimalFormat("###,###,###").format(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity+"";
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
