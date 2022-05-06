package com.example.knox.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.autofill.AutofillNode;
import androidx.compose.ui.autofill.AutofillType;
import androidx.compose.ui.geometry.Rect;
import androidx.room.Room;
import androidx.room.Room;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.knox.R;
import com.example.knox.systemComponents.Credentials;
import com.example.knox.systemComponents.Database;
import com.example.knox.systemComponents.PasswordDAO;

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
        Database db = Room.databaseBuilder(getApplicationContext(),
                Database.class, "shazbot").allowMainThreadQueries().build();
        PasswordDAO dao = db.passDao();
        //dao.insertAll(new Credentials("eller010", "shazbot", "google.com"));
        Credentials tester = dao.getFullCred("google.com");
        System.out.println(tester.toString());
        logButton.setOnClickListener(view ->{
            if (user.getText().toString().equals("eller010") &&
                pass.getText().toString().equals("password")){
                    //System.exit(0);

            }
        });

    }
}