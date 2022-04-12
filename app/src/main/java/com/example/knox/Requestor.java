package com.example.knox;
import java.util.Timer;

import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.FillCallback;
import android.service.autofill.FillRequest;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;

import androidx.annotation.NonNull;

public final class Requestor extends AutofillService{

    //Singleton creation pattern
    private static volatile Requestor instance = null;
    private static volatile Timer timer = null;
    public Requestor(){} //constructor must be public to implement autofill service

    @Override
    public void onFillRequest(@NonNull FillRequest fillRequest, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback fillCallback) {

    }

    @Override
    public void onSaveRequest(@NonNull SaveRequest saveRequest, @NonNull SaveCallback saveCallback) {

    }

    public static Requestor getInstance(){
        if(instance == null){
            instance = new Requestor();
        }
        return instance;
    }


}
