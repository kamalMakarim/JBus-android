package com.kamalMakarimJBusRD;

import static com.kamalMakarimJBusRD.LoginActivity.loggedAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBusActivity extends AppCompatActivity {
    private Button[] btns;
    private int currentPage = 0;
    private int pageSize = 12; // kalian dapat bereksperimen dengan field ini
    private int listSize;
    private int noOfPages;
    private List<Bus> listBus = new ArrayList<>();
    public static Bus selectedBus;
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    private BaseApiService mApiService;
    private Context mContext;
    private ImageView addBus = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        prevButton = findViewById(R.id.prev_page_manage);
        nextButton = findViewById(R.id.next_page_manage);
        pageScroll = findViewById(R.id.page_number_scroll_manage);
        busListView = findViewById(R.id.manage_busses_list);
        addBus = findViewById(R.id.add_button);

        busListHandler();
        listSize = listBus.size();
        ManageBusAdapter arrayAdapter = new ManageBusAdapter(this, listBus);
        busListView.setAdapter(arrayAdapter);
// construct the footer
        paginationFooter();
        goToPage(currentPage);
// listener untuk button prev dan button
        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
        });
        addBus.setOnClickListener(v -> {
            moveActivity(this, AddBusActivity.class);
        });
        try {this.getSupportActionBar().hide();}
        catch (NullPointerException e){}
    }

    private void paginationFooter() {
        int val = listSize % pageSize;
        val = val == 0 ? 0:1;
        noOfPages = listSize / pageSize + val;
        LinearLayout ll = findViewById(R.id.btn_layout);
        btns = new Button[noOfPages];
        if (noOfPages <= 6) {
            ((FrameLayout.LayoutParams) ll.getLayoutParams()).gravity =
                    Gravity.CENTER;
        }
        for (int i = 0; i < noOfPages; i++) {
            btns[i]=new Button(this);
            btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
            btns[i].setText(""+(i+1));
// ganti dengan warna yang kalian mau
            btns[i].setTextColor(getResources().getColor(R.color.black));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,
                    100);
            ll.addView(btns[i], lp);
            final int j = i;
            btns[j].setOnClickListener(v -> {
                currentPage = j;
                goToPage(j);
            });
        }
    }

    private void goToPage(int index) {
        for (int i = 0; i< noOfPages; i++) {
            if (i == index) {
                btns[index].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
                btns[i].setTextColor(getResources().getColor(android.R.color.white));
                scrollToItem(btns[index]);
                viewPaginatedList(listBus, currentPage);
            } else {
                btns[i].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }

    private void viewPaginatedList(List<Bus> listBus, int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, listBus.size());
        List<Bus> paginatedList = listBus.subList(startIndex, endIndex);
        ManageBusAdapter arrayAdapter = new ManageBusAdapter(this, paginatedList);
        busListView.setAdapter(arrayAdapter);

    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.account_button) {
            moveActivity(this, AboutMeActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveActivity(Context ctx, Class<?> cls) { 
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    public void busListHandler() {
        mApiService.getMyBus(loggedAccount.id).enqueue(new Callback<BaseResponse<List<Bus>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<List<Bus>>> call, @NonNull Response<BaseResponse<List<Bus>>> response) {
                BaseResponse<List<Bus>> res = response.body();
                assert res != null;
                if (!res.success) {
                    Toast.makeText(mContext, "Not a success: " + response.code(), Toast.LENGTH_SHORT).show();
                } else {
                    listBus = res.payload;
                    onBusListReceived();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Bus>>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ManageBusActivity", "Network call failed", t);
            }
        });
    }

    private void onBusListReceived() {
        listSize = listBus.size();
        ManageBusAdapter arrayAdapter = new ManageBusAdapter(this, listBus);
        busListView.setAdapter(arrayAdapter);

        paginationFooter();
        goToPage(currentPage);
    }

    private class ManageBusAdapter extends ArrayAdapter<Bus>{
        public ManageBusAdapter (@NonNull Context context, @NonNull List<Bus> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            TextView busName;
            TextView busPrice;
            TextView capacity;
            ImageView calendar;

            Bus bus = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view_manage, parent, false);
            }

            busName = convertView.findViewById(R.id.bus_view_name);
            busPrice = convertView.findViewById(R.id.bus_view_price);
            capacity = convertView.findViewById(R.id.bus_view_capacity);
            assert bus != null;
            busName.setText(bus.name);
            int priceInIDR = (int) bus.price.price;
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formattedPrice = currencyFormat.format(priceInIDR);
            busPrice.setText(formattedPrice);
            String capacityText = "Capacity :" + String.valueOf(bus.capacity);
            capacity.setText(capacityText);

            calendar = convertView.findViewById(R.id.calendar_image);
            calendar.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, BusScheduleActivity.class);
                intent.putExtra("busID", bus.id);
                selectedBus = bus;
                startActivity(intent);
            });
            return convertView;
        }
    }
}