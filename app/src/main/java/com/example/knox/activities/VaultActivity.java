package com.example.knox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.knox.R;
import com.example.knox.systemComponents.Credentials;
import com.example.knox.systemComponents.Database;
import com.example.knox.ui.credentialUI.CredentialFragment;
import com.example.knox.ui.generation.GenerationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

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
        db.passDao().insert(new Credentials("test1", "hello1", "youtube.com"));
        db.passDao().insert(new Credentials("test2", "hello2", "google.com"));
        db.passDao().insert(new Credentials("test3", "hello3", "facebook.com"));
        db.passDao().insert(new Credentials("test4", "hello4", "instagram.com"));
        db.passDao().insert(new Credentials("test5", "hello5", "cc.csusm.edu"));
        db.passDao().insert(new Credentials("test6", "hello7", "my.csusm.edu"));
// insert commented out because already in the database and crashes because there's duplicate entries
        List<Credentials> testing= db.passDao().getAllCreds();
        System.out.println(testing);


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
}