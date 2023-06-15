package com.kingmarco.myclientmodel.Auxiliary.Classes.Static;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Activity.MainActivity;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ChatHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Enums.MessagesStatus;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.NotificationTemplate;
import com.kingmarco.myclientmodel.POJOs.Messages;
import com.kingmarco.myclientmodel.R;

public class NotificationManager implements NotificationTemplate {

    private static NotificationManager sharedInstance;
    private final String CHANNEL_HIGH = "my_summary_channel_id";
    private final String CHANNEL_LOW = "my_message_channel_id";
    private final int SUMMARY_ID = 0;
    private final String GROUP_KEY_MESSAGES = "ClientsMessages";
    private MainActivity mainActivity;

    public static NotificationManager getInstance(){
        if(sharedInstance == null){
            sharedInstance = new NotificationManager();
        }
        return sharedInstance;
    }

    public NotificationManager setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        return this;
    }

    public void sendNotifications(){
        Messages message = ChatHolder.getChat().getMessages();
        if(message == null){return;}
        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("chats/"+ChatHolder.getChat().getClientID()+"/messages");
        if(message.getSenderId() != ClientHolder.getYouClient().getMessagingId()){
            if (message.getStatus() == MessagesStatus.SENT){
                message.setStatus(MessagesStatus.RECEIVED);
                String title = "Nuevo Mensaje de "+message.getSenderName();
                String text = message.getText();
                int clientId = message.getSenderId();
                newMessagesNotification(
                        title,
                        text,
                        clientId,
                        message.getImageUrl()
                );
                databaseReference.child(message.getId()).setValue(message);
            }
        }
        createMessageSummary();
    }

    @Override
    public void createNotificationChannel() {
        android.app.NotificationManager notificationManager = mainActivity.getSystemService(android.app.NotificationManager.class);
        //Channel HIGH
        CharSequence name = mainActivity.getString(R.string.summary_channel_name);
        String description = "All Notification Channel For Messages";
        int importance = android.app.NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel highChannel = new NotificationChannel(CHANNEL_HIGH, name, importance);
        highChannel.setDescription(description);
        notificationManager.createNotificationChannel(highChannel);
        //Channel LOW
        name = mainActivity.getString(R.string.message_channel_name);
        description = "Unique Notification Channel For Messages";
        importance = android.app.NotificationManager.IMPORTANCE_LOW;
        NotificationChannel lowChannel = new NotificationChannel(CHANNEL_LOW, name, importance);
        lowChannel.setDescription(description);
        notificationManager.createNotificationChannel(lowChannel);
    }

    @Override
    public void checkNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(mainActivity)
                    .setTitle("Permiso de Notificacion Requerido")
                    .setMessage("Puedes enterarte de los nuevos mensajes" +
                            "incluso si tienes la aplicacion cerrada")
                    .setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package",mainActivity.getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mainActivity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("No quiero",null)
                    .create()
                    .show();
        }
    }

    @Override
    public void createMessageSummary() {
        Notification summaryNotification = new NotificationCompat.Builder(mainActivity, CHANNEL_LOW)
                .setSmallIcon(R.drawable.ic_person)
                .setContentTitle("Hola")
                .setContentText("No tienes ningun mensaje pendiente")
                .setVibrate(new long[]{1000,1000})
                .setGroup(GROUP_KEY_MESSAGES)
                .setGroupSummary(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mainActivity);
        if(ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManagerCompat.notify(SUMMARY_ID, summaryNotification);
        }
    }

    @Override
    public void newMessagesNotification(String title, String text, int clientID, String imageUrl) {
        RemoteViews notificationLayoutBig = new RemoteViews(mainActivity.getPackageName(), R.layout.big_custom_notification);
        notificationLayoutBig.setTextViewText(R.id.notification_title, title);
        notificationLayoutBig.setTextViewText(R.id.notification_text, text);
        ViewGroup tempContainerBig = new FrameLayout(mainActivity);
        LayoutInflater layoutInflaterBig = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View temViewBig = layoutInflaterBig.inflate(notificationLayoutBig.getLayoutId(), tempContainerBig,false);
        notificationLayoutBig.reapply(mainActivity,temViewBig);

        RemoteViews notificationLayoutSmall = new RemoteViews(mainActivity.getPackageName(), R.layout.small_custom_notification);
        notificationLayoutSmall.setTextViewText(R.id.notification_title, title);
        notificationLayoutSmall.setTextViewText(R.id.notification_text, text);
        ViewGroup tempContainerSmall = new FrameLayout(mainActivity);
        LayoutInflater layoutInflaterSmall = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View temViewSmall = layoutInflaterSmall.inflate(notificationLayoutBig.getLayoutId(), tempContainerSmall,false);
        notificationLayoutSmall.reapply(mainActivity,temViewSmall);

        Intent intent = new Intent(mainActivity,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("navigateTo",R.id.chatFragment);
        PendingIntent pendingIntent = PendingIntent.getActivity(mainActivity, clientID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mainActivity, CHANNEL_HIGH)
                .setSmallIcon(R.drawable.ic_person)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayoutSmall)
                .setCustomBigContentView(notificationLayoutBig)
                .setGroup(GROUP_KEY_MESSAGES)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        Notification notification = builder.build();

        NotificationTarget notificationTarget = new NotificationTarget(
                mainActivity,
                R.id.notification_icon,
                notificationLayoutBig,
                notification,
                clientID);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
        GlideApp.with(mainActivity)
                .asBitmap()
                .load(gsReference)
                .apply(RequestOptions.circleCropTransform())
                .into(notificationTarget);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mainActivity);
        if(ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(clientID, notification);
        }
    }

    @Override
    public void deleteMessagesNotification(int clientID) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(clientID);
    }
}
