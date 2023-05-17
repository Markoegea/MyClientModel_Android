package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.POJOs.Chats;
import com.kingmarco.myclientmodel.POJOs.Messages;

import java.util.ArrayList;
import java.util.Map;

public interface GetRealtimeDB {
    void getSyncRealtimeDB(ArrayList<Messages> arrayList);
    void getSyncRealtimeDB(SnackBarsInfo snackBarsInfo);
}
