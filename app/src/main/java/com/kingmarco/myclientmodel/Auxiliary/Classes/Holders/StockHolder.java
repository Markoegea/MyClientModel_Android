package com.kingmarco.myclientmodel.Auxiliary.Classes.Holders;

import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.StockObserver;
import com.kingmarco.myclientmodel.POJOs.Stock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        return getStockList(nameList);
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

    public static List<Stock> getFilterStockList(ArrayList<Long> stockId, StockType stockType){
        return getStockList(stockType).stream()
                .filter(new Predicate<Stock>() {
                    @Override
                    public boolean test(Stock stock) {
                        return stockId.contains(stock.getId());
                    }
                })
                .collect(Collectors.toList());
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

    public static void clearAllStockItems(){
        stockItems.clear();
    }

    public static void addObserver(StockObserver observer){
        observers.add(observer);
        notifyObservers();
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
