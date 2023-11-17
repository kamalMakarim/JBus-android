package com.kamalMakarimJBusRD;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;


public class LoginActivity extends AppCompatActivity {
    private Button loginButton = null;
    private TextView registerNow = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.login_button);
        registerNow = findViewById(R.id.create_button);


        registerNow.setOnClickListener(v -> {
            moveActivity(this, RegisterActivity.class);
        });
        loginButton.setOnClickListener(v -> {
            moveActivity(this, MainActivity.class);
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if(getActionBar() != null) {
            getActionBar().hide();
        }
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
}