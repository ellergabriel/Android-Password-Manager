package com.example.knox;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.autofill.AutofillManager;

import com.example.knox.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this is just a test
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //check for autofill enabled on startup
        AutofillManager manager = getSystemService(AutofillManager.class);
        if(!manager.hasEnabledAutofillServices()){
            //device does not support autofill; edge case to fill out later
            if(!manager.isAutofillSupported()){
                System.out.println("shazbot\n");
                //TODO: Autofill not supported on device; abort or redirect user
            } else {
                //prompts user to enable autofill from settings
                //TODO: Autofill is supported but not enabled; prompt user
                //Requestor.ActivityStarter starter = new Requestor.ActivityStarter();
                //startActivity(new Intent(Settings.ACTION_REQUEST_SET_AUTOFILL_SERVICE));
            }
        }
        //4 component initialization

    }

    public void debugMode(View view){
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }
}