package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import com.kingmarco.myclientmodel.POJOs.Messages;

import java.util.ArrayList;

public interface MessagesObserver {
    void onMessagesChange(ArrayList<Messages> arrayList);
}
