package com.example.knox;

import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;

import java.util.concurrent.Executor;

public final class Validator {
    private Executor executor;
    private BiometricPrompt bioPrompt;
    private static volatile Validator instance = null;
    private Validator(){}

    private void checkBioMetricSupported(){
        String info = "";
        BiometricManager manager;
        manager = BiometricManager.from(this); // No idea why this is giving me an error, chances are I'm referencing something that doesn't exist

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

    public static Validator getInstance(){
        if(instance == null){
            instance = new Validator();
        }
        return instance;
    }
}
