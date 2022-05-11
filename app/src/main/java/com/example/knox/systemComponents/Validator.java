package com.example.knox.systemComponents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.*;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.knox.R;
import com.example.knox.activities.MainActivity;
import com.example.knox.activities.VaultActivity;

import java.util.concurrent.Executor;

public final class Validator extends AppCompatActivity{
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private static volatile Validator instance = null;
    private Validator(){}
    private static boolean isValid = false;
    /**
     * Method ensures user has proper biometric authentication enabled
     * @param context When calling the method, pass in the current context
     */
    private void checkBioMetricSupported(Context context){
        String info = "";
        BiometricManager manager = BiometricManager.from(context);
        // No idea why this is giving me an error, chances are I'm referencing something that doesn't exist
        /**
         * different library was being used. androidx.biometric.* is what was needed
         */

        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG))
        {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "Biometric authentication is available";
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Biometric features are currently unavailable";
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "This device does not have biometric capabilities";
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "No fingerprint data currently registered";
                break;

            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                info = "Update required before sensor can be used";
                break;

            default:
                info = "Unknown Error";
                break;
        }
    }

    /**
     * Method creates, displays, and authenticates user biometrics
     * @param context current view user is on
     */
    public void createPrompt(Context context){
        //Biometric prompt to user before accessing rest of app
        executor = ContextCompat.getMainExecutor(context);
        biometricPrompt = new BiometricPrompt((FragmentActivity) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(context,
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
                //Validator.isValid = false;
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                //Validator.isValid = true;
                Requestor.setTimer();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(context, "Authentication failed",
                        Toast.LENGTH_SHORT).show();
                //Validator.isValid = false;
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("Log in with fingerprint")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);


        /**
         * old code, moved to Validator for encapsulation
         */
        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View customView = getLayoutInflater().inflate(R.layout.login_popup, null, true);
        Button scanner = customView.findViewById(R.id.bioButton);
        PopupWindow biometricLogin = new PopupWindow(customView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        biometricLogin.showAtLocation(findViewById(R.id.container), Gravity.CENTER, 0, 0);
        scanner.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);

        }); */

        finish();
    }

    public static Validator getInstance(){
        if(instance == null){
            instance = new Validator();
        }
        return instance;
    }


    public static boolean getIsValid(){
        return Validator.isValid;
    }

    /**
     * Checks Requestor timer to make sure session has been going for only < 60 seconds
     * Uses system time as a constant
     * @return true if < 60 seconds since last login, false otherwise
     */
    public static boolean isSessionValid(){
        return (System.currentTimeMillis() - Requestor.checkTimer() < 30000);
    }
}
