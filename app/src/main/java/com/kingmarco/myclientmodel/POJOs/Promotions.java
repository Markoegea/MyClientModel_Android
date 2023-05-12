package com.kingmarco.myclientmodel.POJOs;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.TimestampDeserializer;

import java.util.ArrayList;

/**The class PromoProduct that is used to organize the information of the PromoProduct and parcel it to other fragments*/
public class Promotions extends Stock implements Parcelable {

    private ArrayList<Long> products;
    private TimestampDeserializer finishDate;
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

    public Promotions(Long id, String promoName, int newPrice, ArrayList<String>  url, String lastUpdateBy, ArrayList<Long> products, TimestampDeserializer finishDate) {
        super(id, promoName, newPrice, url, lastUpdateBy);
        this.products = products;
        this.finishDate = finishDate;
    }

    public Promotions() {
        super();
    }

    protected Promotions(Parcel in){
        super(in);
        this.products = new ArrayList<>();
        in.readList(this.products, Long.class.getClassLoader());
        this.finishDate = in.readParcelable(TimestampDeserializer.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeList(this.products);
        this.finishDate.writeToParcel(parcel,i);
    }

    public ArrayList<Long> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Long> products) {
        this.products = products;
    }

    public TimestampDeserializer getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(TimestampDeserializer finishDate) {
        this.finishDate = finishDate;
    }
}
