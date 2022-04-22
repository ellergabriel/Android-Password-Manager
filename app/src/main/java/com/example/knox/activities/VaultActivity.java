package com.example.knox.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.knox.R;
import com.example.knox.ui.credentialUI.CredentialFragment;
import com.example.knox.ui.generation.GenerationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

//Todo: make Credential list appear with correct title and editable
public class VaultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        BottomNavigationView bottomNav = findViewById(R.id.vault_nav);
        CredentialFragment credFrag = new CredentialFragment();
        GenerationFragment genFrag = new GenerationFragment();

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