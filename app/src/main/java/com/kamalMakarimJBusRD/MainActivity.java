package com.kamalMakarimJBusRD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.HorizontalScrollView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.model.Station;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private Button[] btns;
    private int currentPage = 0;
    private final int pageSize = 7; // kalian dapat bereksperimen dengan field ini
    private int listSize;
    private int noOfPages;
    public static List<Bus> listBus = new ArrayList<>();
    private Button prevButton = null;
    private Button nextButton = null;
    private ListView busListView = null;
    private HorizontalScrollView pageScroll = null;
    public static Bus selectedBus = null;
    public static List<Station> listStation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prevButton = findViewById(R.id.prev_page);
        nextButton = findViewById(R.id.next_page);
        pageScroll = findViewById(R.id.page_number_scroll);
        busListView = findViewById(R.id.main_busses_list);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        getAllBus();
// listener untuk button prev dan button
        prevButton.setOnClickListener(v -> {
            currentPage = currentPage != 0? currentPage-1 : 0;
            goToPage(currentPage);
        });
        nextButton.setOnClickListener(v -> {
            currentPage = currentPage != noOfPages -1? currentPage+1 : currentPage;
            goToPage(currentPage);
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
                btns[i].setTextColor(getResources().getColor(android.R.color.black));
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
        BusArrayAdapter arrayAdapter = new BusArrayAdapter(this, paginatedList);
        busListView.setAdapter(arrayAdapter);

    }

    private void scrollToItem(Button item) {
        int scrollX = item.getLeft() - (pageScroll.getWidth() - item.getWidth()) / 2;
        pageScroll.smoothScrollTo(scrollX, 0);
    }

    private SearchView searchView = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);

        // Setup SearchView
        MenuItem searchItem = menu.findItem(R.id.search_button);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Buses");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBusList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBusList(newText);
                return false;
            }
        });

        return true;
    }

    private void filterBusList(String query) {
        List<Bus> filteredList = new ArrayList<>();
        for (Bus bus : listBus) {
            if (bus.name.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(bus);
            }
        }
        BusArrayAdapter arrayAdapter = new BusArrayAdapter(MainActivity.this, filteredList);
        busListView.setAdapter(arrayAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.account_button) {
            moveActivity(this, AboutMeActivity.class);
        }
        if(item.getItemId() == R.id.payment_button) {
            moveActivity(this, PaymentActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    public void getAllBus(){
        mApiService.getAllBus().enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (response.isSuccessful()) {
                    listBus = response.body();
                    BusArrayAdapter arrayAdapter = new BusArrayAdapter(MainActivity.this,listBus);
                    busListView.setAdapter(arrayAdapter);
                    listSize = listBus.size();
                    paginationFooter();
                    goToPage(currentPage);
                }
                else{
                    Toast.makeText(mContext, "Something went wrong" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllStation (){
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful()) {
                    listStation = response.body();
                }
                else{
                    Toast.makeText(mContext, "Something went wrong" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class BusArrayAdapter extends ArrayAdapter<Bus>{
        public ImageView calendar;
        private TextView busName;
        private TextView busPrice;
        private TextView route;
        private View container;
        public BusArrayAdapter(Context context, List<Bus> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            Bus bus = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
            }
            busName = convertView.findViewById(R.id.bus_view_name_main);
            busPrice = convertView.findViewById(R.id.bus_view_price_main);
            route = convertView.findViewById(R.id.bus_view_route_main);
            container = convertView.findViewById(R.id.bus_view_container);
            assert bus != null;
            busName.setText(bus.name);
            busPrice.setText(bus.price.formatedPrice());
            String routeText = bus.departure.toString() + " to " + bus.arrival.toString();
            route.setText(routeText);

            container.setOnClickListener(v -> {
                MainActivity.selectedBus = bus;
                moveActivity(mContext, BusDetailActivity.class);
            });
            return convertView;
        }
    }
}



