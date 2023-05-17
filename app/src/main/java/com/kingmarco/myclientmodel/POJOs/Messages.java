package com.kingmarco.myclientmodel.POJOs;

import com.kingmarco.myclientmodel.Auxiliary.Classes.TimestampDeserializer;
import com.kingmarco.myclientmodel.Auxiliary.Enums.MessagesStatus;

public class Messages {
    //TODO: Set the field for the image
    private String id;
    private Integer senderId;
    private String senderName;
    private String imageUrl;
    private String text;
    private MessagesStatus status;
    private TimestampDeserializer timestamp;

    public Messages() {
    }

    public Messages(Integer senderId, String senderName, String imageUrl, String text, MessagesStatus status, TimestampDeserializer timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.imageUrl = imageUrl;
        this.text = text;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public TimestampDeserializer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(TimestampDeserializer timestamp) {
        this.timestamp = timestamp;
    }

    public MessagesStatus getStatus() {
        return status;
    }

    public void setStatus(MessagesStatus status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
