package com.kamalMakarimJBusRD;

import static com.kamalMakarimJBusRD.LoginActivity.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Invoice;
import com.kamalMakarimJBusRD.model.Payment;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private List<Payment> shownPayment;
    private List<Payment> allPayment;
    private Invoice.PaymentStatus paymentStatus = Invoice.PaymentStatus.WAITING;
    private TextView paymentInitial;
    private TextView paymentUsername;
    private TextView paymentBalance;
    private ListView paymentList;
    private Button paymentWaiting;
    private Button paymentSuccess;
    private Button paymentFailed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mApiService = UtilsApi.getApiService();
        mContext = this;
        paymentStatus = Invoice.PaymentStatus.WAITING;
        paymentInitial = findViewById(R.id.payment_initial);
        paymentUsername = findViewById(R.id.payment_username);
        paymentBalance = findViewById(R.id.payment_balance);
        paymentList = findViewById(R.id.payment_list);
        paymentWaiting = findViewById(R.id.payment_wait_button);
        paymentSuccess = findViewById(R.id.payment_success_button);
        paymentFailed = findViewById(R.id.payment_failed_button);
        paymentInitial.setText(loggedAccount.name.substring(0, 1).toUpperCase());
        paymentUsername.setText(loggedAccount.name);
        paymentBalance.setText(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(loggedAccount.balance));
        getAllPayment();

        paymentWaiting.setOnClickListener(v -> {
            paymentStatus = Invoice.PaymentStatus.WAITING;
            filterPayment();
            paymentList.setAdapter(new PaymentAdapter(mContext, R.layout.payment_view, shownPayment));
        });
        paymentSuccess.setOnClickListener(v -> {
            paymentStatus = Invoice.PaymentStatus.SUCCESS;
            filterPayment();
            paymentList.setAdapter(new PaymentAdapter(mContext, R.layout.payment_view, shownPayment));
        });
        paymentFailed.setOnClickListener(v -> {
            paymentStatus = Invoice.PaymentStatus.FAILED;
            filterPayment();
            paymentList.setAdapter(new PaymentAdapter(mContext, R.layout.payment_view, shownPayment));
        });
    }

    private void filterPayment(){
        if(shownPayment == null){
            shownPayment = new ArrayList<>();
        }
        shownPayment.clear();
        for(Payment payment : allPayment){
            if(payment.status == paymentStatus){
                shownPayment.add(payment);
            }
        }
    }

    private class PaymentAdapter extends ArrayAdapter<Payment> {
        private List<Payment> payments;
        private PaymentAdapter(Context context, int resource, List<Payment> payments) {
            super(context, resource, payments);
            this.payments = payments;
        }

        @Override
        public int getCount() {
            return payments.size();
        }

        @Override
        public Payment getItem(int position) {
            return payments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return payments.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Payment payment = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.payment_view, parent, false);
            }

            TextView busName = convertView.findViewById(R.id.payment_BusName);
            TextView busRoute = convertView.findViewById(R.id.payment_BusRoute);
            TextView paymentSeats = convertView.findViewById(R.id.payment_seats);
            TextView paymentDate = convertView.findViewById(R.id.payment_departureDate);
            Button paymentButton = convertView.findViewById(R.id.payment_button);

            Format formatter = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm");
            paymentDate.setText(formatter.format(payment.departureDate));
            busName.setText(payment.getBus().name);
            busRoute.setText(payment.getBus().departure.toString() + " to " + payment.getBus().arrival.toString());
            paymentSeats.setText(payment.busSeats.size() + " seats");


            if(paymentStatus == Invoice.PaymentStatus.WAITING){
                paymentButton.setText("Pay");
                paymentDate.setText(formatter.format(payment.departureDate));
                paymentButton.setOnClickListener(v -> {
                    mApiService.pay(payment.id).enqueue(new Callback<BaseResponse<Payment>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                            if (response.isSuccessful()) {
                                BaseResponse<Payment> res = response.body();
                                if (res.success) {
                                    getAllPayment();
                                    loggedAccount.balance -= payment.busSeats.size() * payment.getBus().price.price;
                                    Toast.makeText(mContext, "Payment success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Failed to pay", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                            Toast.makeText(mContext, "Failed to pay", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }

            if(paymentStatus == Invoice.PaymentStatus.SUCCESS){
                paymentButton.setText("Cancel");
                paymentDate.setText(formatter.format(payment.departureDate));
                paymentButton.setOnClickListener(v -> {
                    mApiService.cancel(payment.id).enqueue(new Callback<BaseResponse<Payment>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                            if (response.isSuccessful()) {
                                BaseResponse<Payment> res = response.body();
                                if (res.success) {
                                    getAllPayment();
                                    loggedAccount.balance += payment.busSeats.size() * payment.getBus().price.price;
                                    Toast.makeText(mContext, "Cancel success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Failed to cancel", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                            Toast.makeText(mContext, "Failed to pay", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }

            if(paymentStatus == Invoice.PaymentStatus.FAILED){
                paymentButton.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
    private void getAllPayment() {
        mApiService.getMyPayment(loggedAccount.id).enqueue(new Callback<BaseResponse<List<Payment>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Payment>>> call, Response<BaseResponse<List<Payment>>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<List<Payment>> res = response.body();
                    if (res.success) {
                        allPayment = res.payload;
                        filterPayment();
                    } else {
                        Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext, "Failed to get payment data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Payment>>> call, Throwable t) {
                Toast.makeText(mContext, "Failed to get payment data", Toast.LENGTH_SHORT).show();
            }
        });
    }


}