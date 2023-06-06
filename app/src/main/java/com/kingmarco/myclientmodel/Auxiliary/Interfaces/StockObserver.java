package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.POJOs.Stock;

import java.util.ArrayList;
import java.util.Map;

public interface StockObserver {
    void onVariableChange();
}
