package com.example.knox.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import android.os.Bundle;
import com.example.knox.R;
import com.example.knox.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.view.autofill.AutofillManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private boolean isExit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //check for autofill enabled on startup
        AutofillManager manager = getSystemService(AutofillManager.class);
        if(!manager.hasEnabledAutofillServices()){
            System.out.println("shazbot\n");
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


        //TODO: make Validator perform the following code
        /*entire method is already written in the class, getting the prompt to display
        * directly from main is the issue. Could be threading. */
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                          executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                        if (("Fingerprint operation canceled by user.").equals(errString)) {
                            System.out.println("Back button tester\n");
                        } else {
                            //TODO: popup window with goodbye message as user has pressed 'Exit Knox'
                            Toast.makeText(getApplicationContext(),
                                    "Authentication error: " + errString, Toast.LENGTH_SHORT)
                                    .show();
                            System.exit(0);
                        }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded, welcome back", Toast.LENGTH_SHORT).show();
                vaultMode();
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
                .setNegativeButtonText("Exit Knox")
                .build();

        Button biometricLogin = findViewById(R.id.fp_button);
        biometricLogin.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });

        //Validator.getInstance().createPrompt(this);

        Button debug = findViewById(R.id.debug_button);
        debug.setOnClickListener(view -> {
            debugMode();
        });
    }

    public void debugMode(){
        Intent intent = new Intent(this, DebugActivity.class);
        startActivity(intent);
    }

    public void vaultMode(){
        Intent intent = new Intent(this, VaultActivity.class);
        startActivity(intent);
    }

}