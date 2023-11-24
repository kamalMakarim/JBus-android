package com.kamalMakarimJBusRD;

import static java.lang.String.valueOf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.kamalMakarimJBusRD.model.Account;
import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;


public class AboutMeActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private TextView username;
    private TextView email;
    private TextView balance;
    private TextView initial;
    private Button topUpButton;
    private EditText topUpAmount;
    private String nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        mApiService = UtilsApi.getApiService();
        mContext = this;

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        balance = findViewById(R.id.balance);
        initial = findViewById(R.id.initial);
        topUpButton = findViewById(R.id.top_up_button);
        topUpAmount = findViewById(R.id.top_up);
        nameText = LoginActivity.loggedAccount.name;

        username.setText(nameText);
        email.setText(LoginActivity.loggedAccount.email);
        balance.setText(valueOf(LoginActivity.loggedAccount.balance));
        initial.setText(nameText.substring(0,1));

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if(getActionBar() != null) {
            getActionBar().hide();
        }

        topUpButton.setOnClickListener(v -> {
            handleTopUP();
        });
    }

    protected void handleTopUP() {
        int id = LoginActivity.loggedAccount.id;
        double amount = Double.parseDouble(topUpAmount.getText().toString());
        if (topUpAmount.getText().toString().isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.topUp(id, amount).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Something went wrong" +
                            response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Double> res = response.body();
                LoginActivity.loggedAccount.balance = res.payload;
                balance.setText(valueOf(LoginActivity.loggedAccount.balance));
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}