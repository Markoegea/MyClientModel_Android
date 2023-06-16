package com.kingmarco.myclientmodel.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.CartHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ChatHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.FragmentAnimation;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.InAppSnackBars;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Static.NotificationManager;
import com.kingmarco.myclientmodel.Auxiliary.Classes.Holders.StockHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncFireStoreDB;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncFirebase.SyncRealtimeDB;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.Auxiliary.Enums.StockType;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ClientObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetRealtimeDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ProgressBarBehavior;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;


//TODO: The email field in the client and employee
public class MainActivity extends AppCompatActivity implements SetLabelName, GetAuthDB, ClientObserver,
        GetRealtimeDB, ProgressBarBehavior {

    public final int NAVIGATION_DISABLE = 0;
    public final int NAVIGATION_ENABLE = 1;
    private DatabaseReference chatReference, cartReference;
    private ValueEventListener chatListener,cartListener;
    private ListenerRegistration productListener, promotionListener;
    private BottomNavigationView bottomNavigationView;
    private Toolbar tbActivity;
    private NavController navController;
    private NavOptions navOptions;
    private View fragment;
    private ViewGroup.MarginLayoutParams layoutParams;
    private ProgressBar progressBar;

    /**The Main Class, that control the bottom navigation bar, how it works to navigate to other fragments
       The label name of the fragments, and the what happens when the back button is pressed*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navOptions = FragmentAnimation.navigateBehavior();

        progressBar = findViewById(R.id.progressBar);
        bottomNavigationView = findViewById(R.id.nav_view);
        tbActivity = findViewById(R.id.tbActivity);
        setSupportActionBar(tbActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragment = findViewById(R.id.fragmentContainerView);
        layoutParams = (ViewGroup.MarginLayoutParams) fragment.getLayoutParams();

        NotificationManager notificationManager = NotificationManager.getInstance().setMainActivity(this);
        notificationManager.createNotificationChannel();
        notificationManager.checkNotificationPermission();

        ClientHolder.addObserver(this);
        SyncAuthDB.getInstance().addListenerAuth(this);

        productListener = SyncFireStoreDB.newStockListenerRegistration(StockType.PRODUCT,"Productos");
        promotionListener = SyncFireStoreDB.newStockListenerRegistration(StockType.PROMOTION,"Promociones");

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(fragment.getId());
        navController = host.getNavController();

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
            navController.navigate(R.id.homeFragment,null,navOptions);
        } else if (item.getItemId() == R.id.promotionFragment){
            navController.navigate(R.id.promotionFragment,null,navOptions);
        } else if (item.getItemId() == R.id.chatFragment){
            navController.navigate(R.id.chatFragment,null,navOptions);
        } else if (item.getItemId() == R.id.cartFragment){
            navController.navigate(R.id.cartFragment,null,navOptions);
        } else if (item.getItemId() == R.id.loginFragment){
            if (SyncAuthDB.getInstance().isLogin()){
                navController.navigate(R.id.clientAccountFragment,null,navOptions);
            } else{
                navController.navigate(R.id.loginFragment,null,navOptions);
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
        navController.navigate(navigateTo,null,navOptions);
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

    private void stopListeners(){
        ChatHolder.setChatNull();
        ChatHolder.clearMessagesList();
        CartHolder.clearCartList();
        SyncRealtimeDB.stopListening(chatReference, chatListener);
        SyncRealtimeDB.stopListening(cartReference, cartListener);
        chatReference = null;
        chatListener = null;
        cartReference = null;
        cartListener = null;
    }

    private void startListeners(Clients client){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (chatReference == null || chatListener == null){
            chatReference = firebaseDatabase.getReference("chats/"+client.getMessagingId());
            chatListener = SyncRealtimeDB.newChatListener(chatReference,this);
            SyncRealtimeDB.startListening(chatReference,chatListener);
        }
        if (cartReference == null || cartListener == null){
            cartReference = firebaseDatabase.getReference("Carritos/"+client.getId());
            cartListener = SyncRealtimeDB.newCartListener(this);
            SyncRealtimeDB.startListening(cartReference,cartListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (productListener != null ){
            productListener.remove();
            productListener = null;
        }
        if(promotionListener != null){
            promotionListener.remove();
            promotionListener = null;
        }
        ClientHolder.removeObserver(this);
        SyncAuthDB.getInstance().removeListenerAuth(this);
    }

    @Override
    public void onVariableChange(Clients client) {
        if (client == null){
            stopListeners();
            return;
        }
        startListeners(client);
    }

    @Override
    public void onAuthStateChange(@NonNull FirebaseAuth firebaseAuth) {
        if (SyncAuthDB.getInstance().isLogin()) {
            SyncAuthDB.getInstance().addListenerClient();
        }else{
            SyncAuthDB.getInstance().removeListenerClient();
            stopListeners();
        }
    }

    @Override
    public void getSyncRealtimeDB(SnackBarsInfo snackBarsInfo) {
        InAppSnackBars.defineSnackBarInfo(snackBarsInfo,findViewById(R.id.fragmentContainerView),this,this,false);
    }

    @Override
    public void startProgressBar() {
        if (fragment == null || progressBar == null){return;}
        fragment.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    @Override
    public void stopProgressBar() {
        if (fragment == null || progressBar == null){return;}
        fragment.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminate(false);
    }
}