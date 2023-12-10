package com.kamalMakarimJBusRD;

import static com.kamalMakarimJBusRD.LoginActivity.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.model.Payment;
import com.kamalMakarimJBusRD.model.Schedule;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusDetailActivity extends AppCompatActivity {
    private Context mContext;
    private BaseApiService mApiService;
    private List<Schedule> schedules;
    private TextView busName;
    private TextView busPrice;
    private TextView busRoute;
    private TextView busFacilitiesView;
    private TextView busType;
    private TextView chooseYour;
    private ListView listView;
    private Bus bus;
    private Button bookButton;
    private Schedule selectedSchedule;
    private List<String> selectedSeats;
    private String formattedSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_detail);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        mApiService = UtilsApi.getApiService();
        bus = MainActivity.selectedBus;
        mContext = this;
        bookButton = findViewById(R.id.bus_makeBooking);
        busName = findViewById(R.id.bus_detail_name);
        busPrice = findViewById(R.id.bus_detail_price);
        busRoute = findViewById(R.id.bus_detail_route);
        busFacilitiesView = findViewById(R.id.bus_detail_facilities);
        busType = findViewById(R.id.bus_detail_bus_type);
        chooseYour = findViewById(R.id.bus_detail_chooseYour);
        schedules = bus.schedules;
        String busNameText = bus.name + " detail";
        busName.setText(busNameText);
        String routeText = bus.departure.toString() + " to " + bus.arrival.toString();
        busRoute.setText(routeText);
        busPrice.setText(bus.price.formatedPrice());
        busType.setText(bus.busType.toStringNoOV());
        String busFacilities = "";
        for(int i = 0; i < bus.facilities.size(); i++) {
            busFacilities += bus.facilities.get(i).toStringNoOV();
            if(i != bus.facilities.size() - 1) {
                busFacilities += ", ";
            }
        }
        busFacilitiesView.setText(busFacilities);
        listView = findViewById(R.id.bus_detail_listView);
        bookButton.setVisibility(View.GONE);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(mContext, schedules);
        listView.setAdapter(scheduleAdapter);
        chooseYour.setText("Choose your schedule");
        bookButton.setOnClickListener(v -> makeBookingHandler());
    }

    private class SeatAdapter extends ArrayAdapter<Map.Entry<String, Boolean>> {
        private List<Map.Entry<String, Boolean>> seats;
        private Context mContext;

        public SeatAdapter(Context context, Map<String, Boolean> seatsMap) {
            super(context, 0, new ArrayList<>());
            this.seats = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : seatsMap.entrySet()) {
                if (entry.getValue()) {
                    this.seats.add(entry);
                }
            }
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return seats.size();
        }

        @Override
        public Map.Entry<String, Boolean> getItem(int position) {
            return seats.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Map.Entry<String, Boolean> seatEntry = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.seat_view, parent, false);
            }

            CheckBox seatCheckBox = convertView.findViewById(R.id.seat_view_checkbox);
            TextView seatNumber = convertView.findViewById(R.id.seat_view_seatName);

            if (seatEntry != null) {
                seatNumber.setText(seatEntry.getKey());
                seatCheckBox.setChecked(false);
            }

            seatCheckBox.setOnClickListener(v -> {
                if (seatCheckBox.isChecked()) {
                    if (!selectedSeats.contains(seatEntry.getKey())) {
                        selectedSeats.add(seatEntry.getKey());
                    }
                } else {
                    selectedSeats.remove(seatEntry.getKey());
                }
            });

            return convertView;
        }
    }

    private class ScheduleAdapter extends ArrayAdapter<Schedule> {
        private List<Schedule> schedules;
        private Context mContext;

        public ScheduleAdapter(Context context, List<Schedule> schedules) {
            super(context,0, schedules);
            this.schedules = schedules;
            this.mContext = context;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            Schedule schedule = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.schedule_view, parent, false);
            }

            TextView scheduleDate = convertView.findViewById(R.id.schedule_view_date);
            TextView scheduleTime = convertView.findViewById(R.id.schedule_view_time);
            TextView scheduleOccupancy = convertView.findViewById(R.id.schedule_view_occupancy);
            View scheduleView = convertView.findViewById(R.id.schedule_container);

            assert schedule != null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            scheduleDate.setText(dateFormat.format(schedule.departureSchedule));
            scheduleTime.setText(timeFormat.format(schedule.departureSchedule));

            if(schedule.getAvailable() == 0){
                scheduleOccupancy.setText("Full");
            } else{
                scheduleOccupancy.setText(schedule.getAvailable() + " seats are available");
                scheduleView.setOnClickListener(v -> {
                    selectedSchedule = schedule;
                    selectedSeats = new ArrayList<>(); // Reset the selected seats
                    if (selectedSchedule != null) {
                        SimpleDateFormat sentFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        formattedSchedule = sentFormat.format(selectedSchedule.departureSchedule);
                        SeatAdapter seatAdapter = new SeatAdapter(mContext, schedule.seatAvailability);
                        listView.setAdapter(seatAdapter);
                        chooseYour.setText("Choose your seat");
                        bookButton.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(mContext, "No schedule selected", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return convertView;
        }
    }

    private void makeBookingHandler() {
        if (selectedSeats.isEmpty()) {
            Toast.makeText(mContext, "Please select at least one seat", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.makeBooking(loggedAccount.id, bus.accountId, bus.id, selectedSeats, formattedSchedule).enqueue(new Callback<BaseResponse<Payment>>() {

            @Override
            public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
                if(response.isSuccessful()){
                    BaseResponse<Payment> res = response.body();
                    if(res.success){
                        Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);
                    } else{
                        Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(mContext, response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
                Toast.makeText(mContext,"Error: " + t.getCause(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
