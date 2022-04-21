package com.example.knox.activities;

import android.content.Context;
import android.content.Intent;
//import android.hardware.biometrics.BiometricPrompt;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import android.view.View;

import com.example.knox.R;
import com.example.knox.systemComponents.Validator;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.view.autofill.AutofillManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.knox.databinding.ActivityMainBinding;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private boolean isValidated = false;
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
                //TODO: Autofill not supported on device; abort or redirect user
            } else {
                //prompts user to enable autofill from settings
                //TODO: Autofill is supported but not enabled; prompt user

            }
        }
        //Validator.getInstance().createPrompt(this);

        //Biometric prompt to user before accessing rest of app

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                          executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                isValidated = true;
                debugMode(findViewById(R.id.debug_button));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in with fingerprint")
                .setNegativeButtonText("Use account password")
                .build();

        Button biometricLogin = findViewById(R.id.fp_button);
        biometricLogin.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        //Validator.getInstance().createPrompt(this);
    }

    public void debugMode(View view){
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

}