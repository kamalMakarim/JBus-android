package com.kamalMakarimJBusRD;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;
import com.kamalMakarimJBusRD.model.Account;
import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    public static Account loggedAccount;
    private Button loginButton = null;
    private TextView registerNow = null;
    private EditText email, password;
    private BaseApiService mApiService;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.login_button);
        registerNow = findViewById(R.id.create_button);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        mApiService = UtilsApi.getApiService();
        mContext = this;


        registerNow.setOnClickListener(v -> {
            moveActivity(this, RegisterActivity.class);
        });
        loginButton.setOnClickListener(v -> {
            handleLogin();
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        if(getActionBar() != null) {
            getActionBar().hide();
        }

    }

    protected void handleLogin() {
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();
        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                // handle the potential 4xx & 5xx error
                BaseResponse<Account> res = response.body();
                if (!res.success) {
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    loggedAccount = res.payload;
                    Toast.makeText(mContext, "Welcome to JBus", Toast.LENGTH_SHORT).show();
                    moveActivity(mContext, MainActivity.class);
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
}