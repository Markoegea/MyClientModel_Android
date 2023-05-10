package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

/**The class PromoProduct that is used to organize the information of the PromoProduct and parcel it to other fragments*/
public class Promotions implements Parcelable {

    private int id;
    private String url;
    private String promoName;
    private ArrayList<Products> products;
    private int newPrice;
    private LocalDate finishDate;

    public static final Creator<Promotions> CREATOR = new Creator<Promotions>() {
        @Override
        public Promotions createFromParcel(Parcel parcel) {
            return new Promotions(parcel);
        }

        @Override
        public Promotions[] newArray(int i) {
            return new Promotions[i];
        }
    };

    public Promotions(String url, String promoName, ArrayList<Products> products, int newPrice, LocalDate finishDate) {
        this.url = url;
        this.promoName = promoName;
        this.products = products;
        this.newPrice = newPrice;
        this.finishDate = finishDate;
    }

    protected Promotions(Parcel in){
        this.url = in.readString();
        this.promoName = in.readString();
        this.products = new ArrayList<>();
        for( Parcelable parcelable: in.readParcelableArray(products.getClass().getClassLoader()) ){
            products.add((Products) parcelable);
        }
        this.newPrice = in.readInt();
        this.finishDate = LocalDate.ofEpochDay(in.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeString(this.promoName);
        parcel.writeParcelableArray(this.getProducts().toArray(new Products[0]),i);
        parcel.writeInt(this.newPrice);
        parcel.writeLong(this.finishDate.toEpochDay());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    public String getNewPrice() {
        return new DecimalFormat("###,###,###").format(this.newPrice);
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }
}
