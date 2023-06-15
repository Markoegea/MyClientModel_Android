package com.kingmarco.myclientmodel.POJOs;

import java.util.ArrayList;

/**The class PromoProduct that is used to organize the information of the PromoProduct and parcel it to other fragments*/
public class Promotions extends Stock {

    private ArrayList<Long> products;
    private TimestampDeserializer finishDate;

    public Promotions(Long id, String promoName, int newPrice, ArrayList<String>  url, String lastUpdateBy, ArrayList<Long> products, TimestampDeserializer finishDate) {
        super(id, promoName, newPrice, url, lastUpdateBy);
        this.products = products;
        this.finishDate = finishDate;
    }

    public Promotions() {
        super();
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
