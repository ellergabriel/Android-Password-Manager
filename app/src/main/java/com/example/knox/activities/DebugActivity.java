package com.example.knox.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.autofill.AutofillNode;
import androidx.compose.ui.autofill.AutofillType;
import androidx.compose.ui.geometry.Rect;

import android.os.Bundle;
import android.service.autofill.SaveRequest;
import android.view.View;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.knox.R;
import com.example.knox.systemComponents.Requestor;

public class DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        EditText pass = findViewById(R.id.TextPassword);
        EditText user = findViewById(R.id.TextEmailAddress);

        pass.setAutofillHints(View.AUTOFILL_HINT_PASSWORD);
        user.setAutofillHints(View.AUTOFILL_HINT_USERNAME);
        Button logButton = findViewById(R.id.logButton);

        logButton.setOnClickListener(view ->{
            //System.out.println("holder\n");
            if (user.getText().toString().equals("eller010") &&
                pass.getText().toString().equals("password")){
                    //System.exit(0);

            }
        });

    }
}