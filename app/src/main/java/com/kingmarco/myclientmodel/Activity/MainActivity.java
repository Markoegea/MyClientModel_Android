package com.kingmarco.myclientmodel.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kingmarco.myclientmodel.Auxiliary.SetLabelName;
import com.kingmarco.myclientmodel.R;

public class MainActivity extends AppCompatActivity implements SetLabelName {

    BottomNavigationView bottomNavigationView;
    NavController navController;

    /**The Main Class, that control the bottom navigation bar, how it works to navigate to other fragments
       The label name of the fragments, and the what happens when the back button is pressed*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.nav_view);

        /**Basic Configuration Bottom Navigator View*/
        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        navController = host.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment,R.id.promotionFragment,R.id.chatFragment,R.id.cartFragment,R.id.loginFragment,R.id.clientAccountFragment
        ).build();

        //TODO: DELETE THIS, AND FIX THE ERRORS
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);


        /**Show the bottom navigation bar depends of the name fragment*/
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.productDetailsFragment  ||
                        navDestination.getId() == R.id.promotionDetailsFragment ||
                        navDestination.getId() == R.id.cartDetailsFragment ||
                        navDestination.getId() == R.id.registerClientFragment ||
                        navDestination.getId() == R.id.changeInfoAccountFragment ||
                        navDestination.getId() == R.id.changeLocationDataFragment ||
                        navDestination.getId() == R.id.changeLoginDataFragment)
                {
                    bottomNavigationView.setVisibility(View.GONE);
                    return;
                }
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        /**According the name of the button of the bottom bar, it navigate to one fragment ot other*/
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeFragment:
                        navController.navigate(R.id.homeFragment);
                        break;
                    case R.id.chatFragment:
                        navController.navigate(R.id.chatFragment);
                        break;
                    case R.id.promotionFragment:
                        navController.navigate(R.id.promotionFragment);
                        break;
                    case R.id.cartFragment:
                        navController.navigate(R.id.cartFragment);
                        break;
                    case R.id.loginFragment:
                        navController.navigate(R.id.loginFragment);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
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

    /**Calling when use back button of the system*/
    @Override
    public void onBackPressed() {
        if(!clearPopBackStack()){
            super.onBackPressed();
        }
    }

    /**Calling when use back button of the app*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**Change the label name of the app depending of the fragment*/
    @Override
    public void setLabelName(String data) {
        setTitle(data);
    }
}