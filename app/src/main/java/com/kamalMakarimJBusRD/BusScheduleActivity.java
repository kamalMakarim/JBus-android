package com.kamalMakarimJBusRD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.model.Schedule;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusScheduleActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private Bus bus;
    private LinearLayout header;
    private ListView schduleList;
    private LinearLayout addScheduleLayout;
    private EditText date, month, year, hour, minute, second;
    private ImageView addButton;
    private Button addSchedule;
    private String selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_schedule);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        header = findViewById(R.id.addbus_header);
        schduleList = findViewById(R.id.bus_schedule_list);
        addScheduleLayout = findViewById(R.id.add_schedule_layout);
        date = findViewById(R.id.departure_date);
        month = findViewById(R.id.departure_month);
        year = findViewById(R.id.departure_year);
        hour = findViewById(R.id.departure_hour);
        minute = findViewById(R.id.departure_minute);
        second = findViewById(R.id.departure_second);
        addSchedule = findViewById(R.id.add_bus_schedule_button);
        addButton = findViewById(R.id.add_schedule_image);
        getSupportActionBar().hide();

        addButton.setOnClickListener(v -> {
            addScheduleLayout.setVisibility(LinearLayout.VISIBLE);
            header.setVisibility(LinearLayout.GONE);
            schduleList.setVisibility(LinearLayout.GONE);
        });

        addSchedule.setOnClickListener(v -> {
            addScheduleHandler();
        });
        bus = ManageBusActivity.selectedBus;
        ScheduleAdapter arrayAdapter = new ScheduleAdapter(this, bus.schedules);
        schduleList.setAdapter(arrayAdapter);

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

            assert schedule != null;

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            scheduleDate.setText(dateFormat.format(schedule.departureSchedule));
            scheduleTime.setText(timeFormat.format(schedule.departureSchedule));
            String occupancyText = "Occupancy: " + String.valueOf(schedule.getOccupied()) + "/" + String.valueOf(bus.capacity);
            scheduleOccupancy.setText(occupancyText);
            return convertView;
        }

    }

    private void addScheduleHandler(){
        String day = date.getText().toString();
        String mon = month.getText().toString();
        String yr = year.getText().toString();
        String hr = hour.getText().toString();
        String min = minute.getText().toString();
        String sec = second.getText().toString();
        day = (day.length() == 1 ? "0" : "") + day;
        mon = (mon.length() == 1 ? "0" : "") + mon;
        hr = (hr.length() == 1 ? "0" : "") + hr;
        min = (min.length() == 1 ? "0" : "") + min;
        if(sec.equals("")){
            sec = "00";
        }
        else {
            sec = (sec.length() == 1 ? "0" : "") + sec;
        }
        selectedDate = String.format(Locale.getDefault(), "%s-%s-%s %s:%s:%s", yr, mon, day, hr, min, sec);
        mApiService.addSchedule(bus.id, selectedDate).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if(response.isSuccessful()){
                    BaseResponse<Bus> res = response.body();
                    if(res.success){
                        Toast.makeText(mContext, "Schedule added", Toast.LENGTH_SHORT).show();
                        bus = response.body().payload;
                        MainActivity.listBus.remove(ManageBusActivity.selectedBus);
                        MainActivity.listBus.add(bus);
                        ScheduleAdapter arrayAdapter = new ScheduleAdapter(mContext, bus.schedules);
                        schduleList.setAdapter(arrayAdapter);
                        addScheduleLayout.setVisibility(LinearLayout.GONE);
                        header.setVisibility(LinearLayout.VISIBLE);
                        schduleList.setVisibility(LinearLayout.VISIBLE);
                        Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(mContext, res.message ,Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.println(Log.ERROR, "selectedDate", selectedDate);
                    System.out.println(selectedDate);
                    Toast.makeText(mContext, "Failed to add schedule" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}