package com.example.knox.ui.debug;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.knox.R;

public class DebugFragment extends Fragment {

    private DebugViewModel mViewModel;

    public static DebugFragment newInstance() {
        return new DebugFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(DebugViewModel.class);
        return inflater.inflate(R.layout.debug_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DebugViewModel.class);
        // TODO: Use the ViewModel
    }

}