package com.example.knox;

import androidx.biometric.*;

import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;

import java.util.concurrent.Executor;

public final class Validator {
    private Executor executor;
    private BiometricPrompt bioPrompt;
    private static volatile Validator instance = null;
    private Validator(){}

    /**
     * Method ensures user has proper biometric authentication enabled
     * @param context When calling the method, pass in the current
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

    public static Validator getInstance(){
        if(instance == null){
            instance = new Validator();
        }
        return instance;
    }
}
