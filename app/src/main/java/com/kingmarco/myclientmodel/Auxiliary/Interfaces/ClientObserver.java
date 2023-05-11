package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import com.kingmarco.myclientmodel.POJOs.Clients;

public interface ClientObserver {
    void onVariableChange(Clients client);
}
