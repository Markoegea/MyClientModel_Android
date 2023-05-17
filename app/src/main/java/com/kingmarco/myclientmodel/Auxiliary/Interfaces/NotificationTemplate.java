package com.kingmarco.myclientmodel.Auxiliary.Interfaces;

import com.kingmarco.myclientmodel.POJOs.Chats;

public interface NotificationTemplate {
    void createNotificationChannel();
    void checkNotificationPermission();
    void createMessageSummary();
    void newMessagesNotification(String title, String text, int clientID, String imageUrl);
    void deleteMessagesNotification(int clientID);
}
