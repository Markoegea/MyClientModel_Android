package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import com.kingmarco.myclientmodel.POJOs.Carts;

import java.util.ArrayList;

public interface CartObserver {
    void onVariableChange(ArrayList<Carts> carts);
}
