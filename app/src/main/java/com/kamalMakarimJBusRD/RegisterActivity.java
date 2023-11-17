package com.kamalMakarimJBusRD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if(getActionBar() != null) {
            getActionBar().hide();
        }
    }
}