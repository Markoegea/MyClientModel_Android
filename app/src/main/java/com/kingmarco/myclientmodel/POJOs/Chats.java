package com.kingmarco.myclientmodel.POJOs;

import java.util.ArrayList;
import java.util.Date;

/**The class Messages that is used to organize the information of the Message and show it the chat fragment*/
public class Chats {
    private Long clientID;
    private String image;
    private String name;
    private Messages lastMessage;

    public Chats() {
    }

    public Chats(String image, String name) {
        this.image = image;
        this.name = name;
    }

    public Chats(Long clientID, String image, String name) {
        this.clientID = clientID;
        this.image = image;
        this.name = name;
        this.lastMessage = lastMessage;
    }

    public Long getClientID() {
        return clientID;
    }

    public void setClientID(Long clientID) {
        this.clientID = clientID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Messages getMessages() {
        return lastMessage;
    }

    public void setMessages(Messages lastMessage) {
        this.lastMessage = lastMessage;
    }
}
