package com.kingmarco.myclientmodel.Auxiliary.Classes;

import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.StockObserver;
import com.kingmarco.myclientmodel.POJOs.Stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockHolder {

    private static final Map<StockType, ArrayList<Stock>> stockItems = new HashMap<>();
    private static final List<StockObserver> observers = new ArrayList<>();

    private static ArrayList<Stock> getStockList(StockType nameList){
        ArrayList<Stock> parcelables = stockItems.get(nameList);
        if (parcelables == null){
            parcelables = new ArrayList<>();
            stockItems.put(nameList,parcelables);
        }
        return parcelables;
    }

    public static ArrayList<Stock> getList(StockType nameList){
        ArrayList<Stock> parcelables = getStockList(nameList);
        return new ArrayList<>(parcelables);
    }

    public static Stock getSingleStockItem(StockType nameList, Long id){
        ArrayList<Stock> parcelables = getStockList(nameList);
        for (Stock stock: parcelables){
            if (stock.getId().longValue() == id.longValue()){
                return stock;
            }
        }
        return null;
    }

    public static void addInStockList(StockType nameList, Stock item){
        ArrayList<Stock> parcelables = getStockList(nameList);
        parcelables.add(item);
        notifyObservers();
    }

    public static void clearInStockList(StockType nameList){
        ArrayList<Stock> parcelables = getStockList(nameList);
        parcelables.clear();
        notifyObservers();
    }

    public static void addObserver(StockObserver observer){
        observers.add(observer);
    }

    public static void removeObserver(StockObserver observer){
        observers.remove(observer);
    }

    private static void notifyObservers(){
        for (StockObserver observer: observers){
            observer.onVariableChange();
        }
    }
}
