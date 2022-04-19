package com.example.knox;

import android.hardware.biometrics.BiometricPrompt;

import java.util.concurrent.Executor;

public final class Validator {
    private Executor executor;
    private BiometricPrompt bioPrompt;
    private static volatile Validator instance = null;
    private Validator(){}
    public static Validator getInstance(){
        if(instance == null){
            instance = new Validator();
        }
        return instance;
    }
}
