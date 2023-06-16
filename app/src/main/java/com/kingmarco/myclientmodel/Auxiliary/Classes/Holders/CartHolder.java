package com.kingmarco.myclientmodel.Auxiliary.Classes.Holders;

import com.kingmarco.myclientmodel.Auxiliary.Enums.CartStatus;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.CartObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ClientObserver;
import com.kingmarco.myclientmodel.POJOs.Carts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CartHolder {

    private static final ArrayList<Carts> cartList = new ArrayList<>();
    private static final List<CartObserver> observers = new ArrayList<>();

    public static ArrayList<Carts> getCartList(){
        return new ArrayList<>(cartList);
    }

    public static List<Carts> getCartList(CartStatus deliveryStatus){
        return cartList.stream().filter(new Predicate<Carts>() {
            @Override
            public boolean test(Carts carts) {
                if (deliveryStatus == null){
                    return true;
                }
                if (deliveryStatus == CartStatus.ALL){
                    return true;
                }
                return carts.getStatus() == deliveryStatus;
            }
        }).collect(Collectors.toList());
    }

    public static Carts getSingleCartItem(Long id){
        for (Carts carts: cartList){
            if (carts.getId().longValue() == id.longValue()){
                return carts;
            }
        }
        return null;
    }

    public static Carts getDraftCartItem(){
        for (Carts carts: cartList){
            if (carts.getStatus() == CartStatus.DRAFT){
                return carts;
            }
        }
        Carts carts = new Carts(CartStatus.DRAFT);
        carts.getPurchasedItemsId().put(StockType.PRODUCT.name(),new ArrayList<>());
        carts.getPurchasedItemsId().put(StockType.PROMOTION.name(),new ArrayList<>());
        addSingleCartListItem(carts);
        return carts;
    }

    public static void addSingleCartListItem(Carts carts){
        cartList.add(carts);
        notifyObservers();
    }

    public static void removeSingleListItem(Carts carts){
        cartList.remove(carts);
        notifyObservers();
    }

    public static void removeSingleListItem(Long id){
        for (Carts carts: cartList){
            if (carts.getId().longValue() == id.longValue()){
                cartList.remove(carts);
                notifyObservers();
                break;
            }
        }
    }

    public static void clearCartList(){
        cartList.clear();
        notifyObservers();
    }

    public static void addObserver(CartObserver observer){
        observers.add(observer);
        notifyObservers();
    }

    public static void removeObserver(CartObserver observer){
        observers.remove(observer);
    }

    private static void notifyObservers(){
        for (CartObserver observer: observers){
            observer.onVariableChange(cartList);
        }
    }
}
