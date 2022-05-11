package com.example.knox.ui.generation;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.knox.R;
import com.example.knox.systemComponents.Database;

public class GenerationFragment extends Fragment {

    private GenerationViewModel mViewModel;
    private Button pwdGenButton;
    private TextView newPassword;
    private TextView charAmountView;
    //private EditText editTextNumber;
    private SeekBar seekBar;

    public static GenerationFragment newInstance() {
        return new GenerationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.generation_fragment, container, false);
        pwdGenButton = rootView.findViewById(R.id.pwdGenButton);
        charAmountView = rootView.findViewById(R.id.charAmountView);
        newPassword = rootView.findViewById(R.id.newPassword);
        seekBar = rootView.findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                charAmountView.setText("Password Length (8-32): " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pwdGenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = Database.generate(seekBar.getProgress());
                newPassword.setText(password);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(GenerationViewModel.class);
    }

}