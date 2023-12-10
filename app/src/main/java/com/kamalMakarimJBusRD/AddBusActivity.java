package com.kamalMakarimJBusRD;

import static com.kamalMakarimJBusRD.LoginActivity.loggedAccount;
import static com.kamalMakarimJBusRD.MainActivity.*;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.model.BusType;
import com.kamalMakarimJBusRD.model.Facility;
import com.kamalMakarimJBusRD.model.Station;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText busName, busCapacity, busPrice;
    private final BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDepartureID;
    private int selectedArrivalID;
    private Spinner departureSpinner;
    private Spinner arrivalSpinner;
    private final List<Facility> selectedFacilities = new ArrayList<>();
    private Button addBusButton;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private final AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedBusType =  busType[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private final AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedDepartureID = stationList.get(position).id;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private final AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedArrivalID = stationList.get(position).id;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Toast.makeText(mContext, "Please select a station", Toast.LENGTH_SHORT).show();
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        mApiService = UtilsApi.getApiService();
        mContext = this;
        acCheckBox = findViewById(R.id.ac_checkbox);
        wifiCheckBox = findViewById(R.id.wifi_checkbox);
        toiletCheckBox = findViewById(R.id.toilet_checkbox);
        lcdCheckBox = findViewById(R.id.lcd_checkbox);
        coolboxCheckBox = findViewById(R.id.coolBox_checkbox);
        lunchCheckBox = findViewById(R.id.lunch_checkbox);
        baggageCheckBox = findViewById(R.id.largeBaggage_checkbox);
        electricCheckBox = findViewById(R.id.electricSocket_checkbox);
        busName = findViewById(R.id.bus_name);
        busCapacity = findViewById(R.id.bus_capacity);
        busPrice = findViewById(R.id.bus_price);
        Spinner busTypeDropdown = findViewById(R.id.bus_type_spinner);
        departureSpinner = findViewById(R.id.departure_spinner);
        arrivalSpinner = findViewById(R.id.arrival_spinner);
        addBusButton = findViewById(R.id.add_button);

        ArrayAdapter<BusType> adapterBusType = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, busType);
        adapterBusType.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeDropdown.setAdapter(adapterBusType);
        busTypeDropdown.setOnItemSelectedListener(busTypeOISL);


        getAllStation();
        ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationList);
        deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        departureSpinner.setAdapter(deptBus);
        departureSpinner.setOnItemSelectedListener(deptOISL);
        ArrayAdapter arrBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, stationList);
        arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        arrivalSpinner.setAdapter(arrBus);
        arrivalSpinner.setOnItemSelectedListener(arrOISL);

        setupFacilitiesCheckboxes();
        setupAddBusButton();
    }

    private void setupAddBusButton() {
        addBusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String busNameStr = busName.getText().toString().trim();
                    int capacity = Integer.parseInt(busCapacity.getText().toString().trim());
                    double price = Double.parseDouble(busPrice.getText().toString().trim());

                    if (selectedDepartureID == selectedArrivalID) {
                        Toast.makeText(mContext, "Departure and arrival cannot be the same", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (busNameStr.isEmpty() || capacity <= 0 || price <= 0 || selectedFacilities.isEmpty()) {
                        Toast.makeText(mContext, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addBus(busNameStr, capacity, price);
                } catch (NumberFormatException e) {
                    Toast.makeText(mContext, "Invalid number format", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addBus(String name, int capacity, double price) {
        mApiService.createBus(loggedAccount.id, name, capacity, selectedFacilities,selectedBusType,price,selectedDepartureID,selectedArrivalID).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<Bus> body = response.body();
                    if (body.success) {
                        Toast.makeText(mContext, "Bus added", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, "Error: " + body.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorMessage = "Server error: " + response.code();
                    if(response.code() == 404) {
                        errorMessage = "Not found";
                    } else if(response.code() == 500) {
                        errorMessage = "Internal server error";
                    }
                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                Toast.makeText(mContext, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllStation() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stationList.clear();
                    stationList.addAll(response.body());
                    ((ArrayAdapter) departureSpinner.getAdapter()).notifyDataSetChanged();
                    ((ArrayAdapter) arrivalSpinner.getAdapter()).notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Failed to load stations", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(mContext, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setupFacilitiesCheckboxes() {
        acCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.AC);
            } else {
                selectedFacilities.remove(Facility.AC);
            }
        });
        wifiCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.WIFI);
            } else {
                selectedFacilities.remove(Facility.WIFI);
            }
        });
        toiletCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.TOILET);
            } else {
                selectedFacilities.remove(Facility.TOILET);
            }
        });
        lcdCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.LCD_TV);
            } else {
                selectedFacilities.remove(Facility.LCD_TV);
            }
        });
        coolboxCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.COOL_BOX);
            } else {
                selectedFacilities.remove(Facility.COOL_BOX);
            }
        });
        lunchCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.LUNCH);
            } else {
                selectedFacilities.remove(Facility.LUNCH);
            }
        });
        baggageCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.LARGE_BAGGAGE);
            } else {
                selectedFacilities.remove(Facility.LARGE_BAGGAGE);
            }
        });
        electricCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selectedFacilities.add(Facility.ELECTRIC_SOCKET);
            } else {
                selectedFacilities.remove(Facility.ELECTRIC_SOCKET);
            }
        });
    }
}