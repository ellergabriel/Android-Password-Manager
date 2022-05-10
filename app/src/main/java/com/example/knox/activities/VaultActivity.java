package com.example.knox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.knox.R;
import com.example.knox.systemComponents.Credentials;
import com.example.knox.systemComponents.Database;
import com.example.knox.ui.credentialUI.CredentialFragment;
import com.example.knox.ui.generation.GenerationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

//Todo: make Credential list appear with correct title and editable
public class VaultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        BottomNavigationView bottomNav = findViewById(R.id.vault_nav);
        CredentialFragment credFrag = new CredentialFragment();
        GenerationFragment genFrag = new GenerationFragment();

        setFragment((credFrag)); //added so credentials so up
        Database db = Database.getInstance(getApplicationContext());
//        db.passDao().insert(new Credentials("test1", "hello1", "youtube.com"));
//        db.passDao().insert(new Credentials("test2", "hello2", "google.com"));
//        db.passDao().insert(new Credentials("test3", "hello3", "facebook.com"));
//        db.passDao().insert(new Credentials("test4", "hello4", "instagram.com"));
//        db.passDao().insert(new Credentials("test5", "hello5", "cc.csusm.edu"));
//        db.passDao().insert(new Credentials("test6", "hello7", "my.csusm.edu"));
// insert commented out because already in the database and crashes because there's duplicate entries
        List<Credentials> testing= db.passDao().getAllCreds();
        System.out.println(testing + " - VaultActivity.java");

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showCustomDialog();
            }
        });

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case(R.id.vault_tab):
                        setFragment((credFrag));
                        return true;
                    case(R.id.generation_tab):
                        setFragment(genFrag);
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    private void setFragment(Fragment frag){
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.vault_gen_frame, frag);
        fragTrans.commit();
    }

    void showCustomDialog(){
        final Dialog dialog = new Dialog(VaultActivity.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.password_dialog);

        //Initializing the views of the dialog.
        final EditText urlEt = dialog.findViewById(R.id.etURL);
        final EditText userEt = dialog.findViewById(R.id.etEmail);
        final EditText passwordEt = dialog.findViewById(R.id.etPassword);
        Button submitButton = dialog.findViewById(R.id.add_password);
        Button cancelButton = dialog.findViewById(R.id.cancel_password);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials cred = new Credentials (userEt.getText().toString(), passwordEt.getText().toString(), urlEt.getText().toString());
                Database db = Database.getInstance(getApplicationContext());
                db.passDao().insert(cred);
                Toast.makeText(getApplicationContext(), "Added credential successfully", Toast.LENGTH_SHORT).show();
                CredentialFragment credFrag = new CredentialFragment();
                setFragment((credFrag));
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}