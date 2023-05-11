package com.kingmarco.myclientmodel.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kingmarco.myclientmodel.Auxiliary.Classes.ClientHolder;
import com.kingmarco.myclientmodel.Auxiliary.Classes.SyncAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.ClientObserver;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.GetAuthDB;
import com.kingmarco.myclientmodel.Auxiliary.Interfaces.SetLabelName;
import com.kingmarco.myclientmodel.POJOs.Clients;
import com.kingmarco.myclientmodel.R;

import org.checkerframework.checker.units.qual.C;

public class MainActivity extends AppCompatActivity implements SetLabelName, GetAuthDB, ClientObserver {

    public final int NAVIGATION_DISABLE = 0;
    public final int NAVIGATION_ENABLE = 1;
    private final String CHANNEL_HIGH = "my_summary_channel_id";
    private final String CHANNEL_LOW = "my_message_channel_id";
    private final int SUMMARY_ID = 0;
    private final String GROUP_KEY_MESSAGES = "ClientsMessages";
    private BottomNavigationView bottomNavigationView;
    private Toolbar tbActivity;
    private NavController navController;

    /**The Main Class, that control the bottom navigation bar, how it works to navigate to other fragments
       The label name of the fragments, and the what happens when the back button is pressed*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ClientHolder.addObserver(this);

        SyncAuthDB.getInstance().addListenerAuth(this);

        bottomNavigationView = findViewById(R.id.nav_view);
        tbActivity = findViewById(R.id.tbActivity);
        setSupportActionBar(tbActivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /**Basic Configuration Bottom Navigator View*/
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
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
                bottomNavigationView.setVisibility(View.VISIBLE);
                setSupportActionBar(tbActivity);
                break;
            case NAVIGATION_DISABLE:
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SyncAuthDB.getInstance().removeListenerClient();
        SyncAuthDB.getInstance().removeListenerAuth(this);
    }

    @Override
    public void onVariableChange(Clients client) {
        if (client == null){return;}
    }

    @Override
    public void onAuthStateChange(@NonNull FirebaseAuth firebaseAuth) {
        if (SyncAuthDB.getInstance().isLogin()) {
            SyncAuthDB.getInstance().addListenerClient();
        }else{
            SyncAuthDB.getInstance().removeListenerClient();
        }
    }
}