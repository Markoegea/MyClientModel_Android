package com.kingmarco.myclientmodel.POJOs;

import com.kingmarco.myclientmodel.Auxiliary.MessagesStatus;

import java.util.Date;

public class Messages {
    private String text;
    private String sender;
    private Date time;
    private MessagesStatus status;

    public Messages() {
    }

    public Messages(String text, String sender, Date time, MessagesStatus status) {
        this.text = text;
        this.sender = sender;
        this.time = time;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getTime() {
        return time;
    }

    public MessagesStatus getStatus() {
        return status;
    }

    public void setStatus(MessagesStatus status) {
        this.status = status;
    }
}
