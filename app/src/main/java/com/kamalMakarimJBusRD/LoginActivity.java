package com.kamalMakarimJBusRD;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.create_button);

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Tolong isi email dan password", Toast.LENGTH_SHORT).show();
                } else {
                    performLogin(email, password);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(LoginActivity.this, "Register", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void performLogin(String email, String password){
        Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
    }
}