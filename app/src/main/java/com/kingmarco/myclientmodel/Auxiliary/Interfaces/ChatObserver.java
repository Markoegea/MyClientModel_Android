package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import com.kingmarco.myclientmodel.POJOs.Chats;
import com.kingmarco.myclientmodel.POJOs.Messages;

import java.util.ArrayList;

public interface ChatObserver {
    void onMessagesChange(ArrayList<Messages> arrayList);
    void onChatChange(Chats chats);
}
