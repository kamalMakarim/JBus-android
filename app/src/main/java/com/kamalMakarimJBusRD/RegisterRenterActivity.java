package com.kamalMakarimJBusRD;

import static com.kamalMakarimJBusRD.LoginActivity.loggedAccount;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Renter;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterRenterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText companyName,companyAddress, companyPhone;
    private Button registerButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);  // Move this line here

        companyName = findViewById(R.id.company_name);
        companyAddress = findViewById(R.id.company_address);
        companyPhone = findViewById(R.id.company_phone);
        registerButton = findViewById(R.id.register_company_button);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        registerButton.setOnClickListener(v -> {
            handleRegister();
        });
    }


    private void handleRegister(){
        String companyNameS = companyName.getText().toString();
        String companyAddressS = companyAddress.getText().toString();
        String companyPhoneS = companyPhone.getText().toString();

        if (companyNameS.isEmpty() || companyAddressS.isEmpty() || companyPhoneS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.registerRenter(loggedAccount.id, companyNameS, companyAddressS, companyPhoneS).enqueue(new Callback<BaseResponse<Renter>>() {
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response) {
                BaseResponse<Renter> renterResponse = response.body();
                if(Objects.requireNonNull(renterResponse).success){
                    loggedAccount.company = renterResponse.payload;
                    Toast.makeText(mContext, renterResponse.message, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(mContext, renterResponse.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                Toast.makeText(mContext, "Register Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}