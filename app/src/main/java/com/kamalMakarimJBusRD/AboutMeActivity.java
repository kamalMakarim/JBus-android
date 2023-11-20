package com.kamalMakarimJBusRD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class AboutMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView username = findViewById(R.id.username);
        TextView email = findViewById(R.id.email);
        TextView balance = findViewById(R.id.balance);
        TextView initial = findViewById(R.id.initial);
        String nameText = "Kamal Makarim";

        username.setText(nameText);
        email.setText("kamal.makarim@gmail.com");
        balance.setText("Rp. 100.000.000");
        initial.setText(nameText.substring(0,1));

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if(getActionBar() != null) {
            getActionBar().hide();
        }

    }


}