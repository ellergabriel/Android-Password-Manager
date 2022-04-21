package com.example.knox.ui.debug;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DebugViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public DebugViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("Please don't break me");
    }

    public LiveData<String> getText() {
        return mText;
    }
}