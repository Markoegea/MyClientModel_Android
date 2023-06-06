package com.kingmarco.myclientmodel.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Auxiliary.Classes.CartHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.GlideApp;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncRealtimeDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ClientObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.NotificationTemplate;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

import java.util.ArrayList;


//TODO: CHECK THE CODE AND COMMIT
public class MainActivity extends AppCompatActivity implements SetLabelName, GetAuthDB, ClientObserver
        , NotificationTemplate {

    public final int NAVIGATION_DISABLE = 0;
    public final int NAVIGATION_ENABLE = 1;
    private final String CHANNEL_HIGH = "my_summary_channel_id";
    private final String CHANNEL_LOW = "my_message_channel_id";
    private final int SUMMARY_ID = 0;
    private final String GROUP_KEY_MESSAGES = "ClientsMessages";
    private ListenerRegistration cartListener, productListener, promotionListener;
    private BottomNavigationView bottomNavigationView;
    private Toolbar tbActivity;
    private NavController navController;
    private SyncRealtimeDB syncRealtimeDB;
    private View fragment;
    private ViewGroup.MarginLayoutParams layoutParams;

    /**The Main Class, that control the bottom navigation bar, how it works to navigate to other fragments
       The label name of the fragments, and the what happens when the back button is pressed*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        checkNotificationPermission();

        syncRealtimeDB = SyncRealtimeDB.getInstance();

        bottomNavigationView = findViewById(R.id.nav_view);
        tbActivity = findViewById(R.id.tbActivity);
        setSupportActionBar(tbActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /**Basic Configuration Bottom Navigator View*/
        fragment = findViewById(R.id.fragmentContainerView);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(fragment.getId());
        navController = host.getNavController();

        layoutParams = (ViewGroup.MarginLayoutParams) fragment.getLayoutParams();


        /**Show the bottom navigation bar depends of the name fragment*/
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.homeFragment  ||
                        navDestination.getId() == R.id.promotionFragment ||
                        navDestination.getId() == R.id.chatFragment ||
                        navDestination.getId() == R.id.cartFragment ||
                        navDestination.getId() == R.id.loginFragment) {
                    bottomNavigationView.setOnItemSelectedListener(null);
                    bottomNavigationView.setSelectedItemId(navDestination.getId());
                    bottomNavigationView.setOnItemSelectedListener(MainActivity.this::onNavigationItemSelected);
                    setToolbarBehavior(NAVIGATION_ENABLE);
                    return;
                } else if (navDestination.getId() == R.id.clientAccountFragment){
                    bottomNavigationView.setOnItemSelectedListener(null);
                    bottomNavigationView.setSelectedItemId(R.id.loginFragment);
                    bottomNavigationView.setOnItemSelectedListener(MainActivity.this::onNavigationItemSelected);
                    setToolbarBehavior(NAVIGATION_ENABLE);
                    return;
                }
                setToolbarBehavior(NAVIGATION_DISABLE);
            }
        });

        /**According the name of the button of the bottom bar, it navigate to one fragment ot other*/
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.homeFragment){
            navController.navigate(R.id.homeFragment);
        } else if (item.getItemId() == R.id.promotionFragment){
            navController.navigate(R.id.promotionFragment);
        } else if (item.getItemId() == R.id.chatFragment){
            navController.navigate(R.id.chatFragment);
        } else if (item.getItemId() == R.id.cartFragment){
            navController.navigate(R.id.cartFragment);
        } else if (item.getItemId() == R.id.loginFragment){
            if (SyncAuthDB.getInstance().isLogin()){
                navController.navigate(R.id.clientAccountFragment);
            } else{
                navController.navigate(R.id.loginFragment);
            }
        }
        return true;
    }

    private void setToolbarBehavior(int behavior) {
        switch (behavior){
            case NAVIGATION_ENABLE:
                float density = getResources().getDisplayMetrics().density;
                layoutParams.bottomMargin = (int) (50 * density + 0.5f);
                fragment.setLayoutParams(layoutParams);
                bottomNavigationView.setVisibility(View.VISIBLE);
                setSupportActionBar(tbActivity);
                break;
            case NAVIGATION_DISABLE:
                layoutParams.bottomMargin = 0;
                fragment.setLayoutParams(layoutParams);
                bottomNavigationView.setVisibility(View.GONE);
                tbActivity.setNavigationIcon(R.drawable.ic_back);
                tbActivity.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void createNotificationChannel() {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        //Channel HIGH
        CharSequence name = getString(R.string.summary_channel_name);
        String description = "All Notification Channel For Messages";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel highChannel = new NotificationChannel(CHANNEL_HIGH, name, importance);
        highChannel.setDescription(description);
        notificationManager.createNotificationChannel(highChannel);
        //Channel LOW
        name = getString(R.string.message_channel_name);
        description = "Unique Notification Channel For Messages";
        importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel lowChannel = new NotificationChannel(CHANNEL_LOW, name, importance);
        lowChannel.setDescription(description);
        notificationManager.createNotificationChannel(lowChannel);
    }

    @Override
    public void checkNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setTitle("Permiso de Notificacion Requerido")
                    .setMessage("Puedes enterarte de los nuevos mensajes" +
                            "incluso si tienes la aplicacion cerrada")
                    .setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package",MainActivity.this.getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No quiero",null)
                    .create()
                    .show();
        }
    }

    @Override
    public void createMessageSummary() {
        Notification summaryNotification = new NotificationCompat.Builder(this, CHANNEL_LOW)
                .setSmallIcon(R.drawable.ic_person)
                .setContentTitle("Hola")
                .setContentText("No tienes ningun mensaje pendiente")
                .setVibrate(new long[]{1000,1000})
                .setGroup(GROUP_KEY_MESSAGES)
                .setGroupSummary(true)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManagerCompat.notify(SUMMARY_ID, summaryNotification);
        }
    }

    @Override
    public void newMessagesNotification(String title, String text, int clientID, String imageUrl) {
        RemoteViews notificationLayoutBig = new RemoteViews(getPackageName(), R.layout.big_custom_notification);
        notificationLayoutBig.setTextViewText(R.id.notification_title, title);
        notificationLayoutBig.setTextViewText(R.id.notification_text, text);
        ViewGroup tempContainerBig = new FrameLayout(this);
        LayoutInflater layoutInflaterBig = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View temViewBig = layoutInflaterBig.inflate(notificationLayoutBig.getLayoutId(), tempContainerBig,false);
        notificationLayoutBig.reapply(this,temViewBig);

        RemoteViews notificationLayoutSmall = new RemoteViews(getPackageName(), R.layout.small_custom_notification);
        notificationLayoutSmall.setTextViewText(R.id.notification_title, title);
        notificationLayoutSmall.setTextViewText(R.id.notification_text, text);
        ViewGroup tempContainerSmall = new FrameLayout(this);
        LayoutInflater layoutInflaterSmall = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View temViewSmall = layoutInflaterSmall.inflate(notificationLayoutBig.getLayoutId(), tempContainerSmall,false);
        notificationLayoutSmall.reapply(this,temViewSmall);

        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("navigateTo",R.id.chatFragment);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, clientID,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_HIGH)
                .setSmallIcon(R.drawable.ic_person)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayoutSmall)
                .setCustomBigContentView(notificationLayoutBig)
                .setGroup(GROUP_KEY_MESSAGES)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        Notification notification = builder.build();

        NotificationTarget notificationTarget = new NotificationTarget(
                this,
                R.id.notification_icon,
                notificationLayoutBig,
                notification,
                clientID);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(imageUrl);
        GlideApp.with(this)
                .asBitmap()
                .load(gsReference)
                .apply(RequestOptions.circleCropTransform())
                .into(notificationTarget);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManager.notify(clientID, notification);
        }
    }

    @Override
    public void deleteMessagesNotification(int clientID) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(clientID);
    }

    /**Return to the previous fragment, erasing the actual fragment ot the stack*/
    private boolean clearPopBackStack(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return true;
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onNotificationOpen(intent);
    }

    private void onNotificationOpen(Intent intent){
        if (intent == null){return;}
        int navigateTo = intent.getIntExtra("navigateTo",0);
        if (navigateTo == 0) {
            return;
        }
        navController.navigate(navigateTo);
    }

    /**Calling when use back button of the app*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**Calling when use back button of the system*/
    @Override
    public void onBackPressed() {
        if(!clearPopBackStack()){
            super.onBackPressed();
        }
    }

    /**Change the label name of the app depending of the fragment*/
    @Override
    public void setLabelName(String data) {
        if (tbActivity != null){
            tbActivity.setTitle(data);
            return;
        }
        setTitle(data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ClientHolder.addObserver(this);
        SyncAuthDB.getInstance().addListenerAuth(this);
        productListener = SyncFireStoreDB.newCartListenerRegistration(StockType.PRODUCT,"Productos");
        promotionListener = SyncFireStoreDB.newCartListenerRegistration(StockType.PROMOTION,"Promociones");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        syncRealtimeDB.stopListening();
        ClientHolder.removeObserver(this);
        SyncAuthDB.getInstance().removeListenerClient();
        SyncAuthDB.getInstance().removeListenerAuth(this);
        if (productListener != null ){
            productListener.remove();
            productListener = null;
        }
        if(promotionListener != null){
            promotionListener.remove();
            promotionListener = null;
        }
    }

    @Override
    public void onVariableChange(Clients client) {
        if (client == null){
            syncRealtimeDB.stopListening();
            return;
        }
        cartListener = SyncFireStoreDB.newCartListenerRegistration();
        syncRealtimeDB.listening(FirebaseDatabase.getInstance()
                .getReference("chats/"+client.getMessagingId()),this);
        syncRealtimeDB.startListening();
    }

    @Override
    public void onAuthStateChange(@NonNull FirebaseAuth firebaseAuth) {
        if (SyncAuthDB.getInstance().isLogin()) {
            SyncAuthDB.getInstance().addListenerClient();
        }else{
            SyncAuthDB.getInstance().removeListenerClient();
            syncRealtimeDB.stopListening();

            if(cartListener == null){return;}
            CartHolder.clearCartList();
            cartListener.remove();
            cartListener = null;
        }
    }
}