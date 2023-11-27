package com.kamalMakarimJBusRD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.request.BaseApiService;

import java.util.ArrayList;
import java.util.List;

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

        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        busListView = findViewById(R.id.manage_busses_list);
        addBus = findViewById(R.id.add_button);

// membuat sample list
        busListHandler();
        listSize = listBus.size();
        BusArrayAdapter arrayAdapter = new BusArrayAdapter(this,listBus, R.layout.bus_view_manage);
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
        arrayAdapter.calendar.setOnClickListener(v -> {
            moveActivity(this, BusScheduleActivity.class);
        });
        addBus.setOnClickListener(v -> {
            moveActivity(this, AddBusActivity.class);
        });
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
        BusArrayAdapter arrayAdapter = new BusArrayAdapter(this, paginatedList, R.layout.bus_view_manage);
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
        mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<BaseResponse<List<Bus>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Bus>>> call, Response<BaseResponse<List<Bus>>> response) {
                BaseResponse<List<Bus>> res = response.body();
                if (!res.success) {
                    Toast.makeText(mContext, res.message + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    listBus = res.payload;
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<List<Bus>>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}