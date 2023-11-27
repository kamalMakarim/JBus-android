package com.kamalMakarimJBusRD;

import static com.kamalMakarimJBusRD.LoginActivity.loggedAccount;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.kamalMakarimJBusRD.model.BaseResponse;
import com.kamalMakarimJBusRD.model.Bus;
import com.kamalMakarimJBusRD.model.BusType;
import com.kamalMakarimJBusRD.model.Facility;
import com.kamalMakarimJBusRD.model.Station;
import com.kamalMakarimJBusRD.request.BaseApiService;
import com.kamalMakarimJBusRD.request.UtilsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText busName, busCapacity, busPrice;
    private BusType[] busType = BusType.values();
    private BusType selectedBusType;
    private Spinner busTypeDropdown;
    private List<Station> stationList = new ArrayList<>();
    private int selectedDepartureStation;
    private int selectedArrivalStation;
    private Spinner departureStationDropdown;
    private Spinner arrivalStationDropdown;
    private TableLayout facilitiesSection;
    private CheckBox[] facilitiesCheckBox;
    private List<Facility> selectedFacilities = new ArrayList<>();
    private Button addBusButton;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {this.getSupportActionBar().hide();}
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        busName = findViewById(R.id.bus_name);
        busCapacity = findViewById(R.id.bus_capacity);
        busPrice = findViewById(R.id.bus_price);
        busTypeDropdown = findViewById(R.id.bus_type_spinner);
        departureStationDropdown = findViewById(R.id.departure_spinner);
        arrivalStationDropdown = findViewById(R.id.arrival_spinner);
        facilitiesSection = findViewById(R.id.facilities_tableLayout);
        addBusButton = findViewById(R.id.add_button);

        ArrayAdapter adapterBusType = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, busType);
        adapterBusType.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeDropdown.setAdapter(adapterBusType);
        busTypeDropdown.setOnItemSelectedListener(setBusType());

        setStationList();
        setFacilitiesCheckbox();

        addBusButton.setOnClickListener(x -> {
            handleAddBus();
        });
    }

    private void moreActivity (Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    private void viewToast (Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

    protected AdapterView.OnItemSelectedListener setBusType(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBusType = busType[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    protected AdapterView.OnItemSelectedListener setDepartureStation(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Station dept = (Station) adapterView.getSelectedItem();
                selectedDepartureStation = dept.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    protected AdapterView.OnItemSelectedListener setArrivalStation(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Station arr = (Station) adapterView.getSelectedItem();
                selectedArrivalStation = arr.id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    protected void setStationList(){
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(!response.isSuccessful()){
                    viewToast(mContext, "Application error " + response.code());
                    return;
                }

                stationList = response.body();

                ArrayAdapter adapterDepartureStation = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, stationList);
                adapterDepartureStation.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureStationDropdown.setAdapter(adapterDepartureStation);
                departureStationDropdown.setOnItemSelectedListener(setDepartureStation());

                ArrayAdapter adapterArrivalStation = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, stationList);
                adapterArrivalStation.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                arrivalStationDropdown.setAdapter(adapterArrivalStation);
                arrivalStationDropdown.setOnItemSelectedListener(setArrivalStation());
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                viewToast(mContext, "Ada problem pada server");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void setFacilitiesCheckbox(){
        Facility[] facilityList = Facility.values();
        facilitiesCheckBox = new CheckBox[Facility.values().length];

        for (int i = 0; i < Facility.values().length; i++) {
            facilitiesCheckBox[i] = new CheckBox(mContext);
            facilitiesCheckBox[i].setText(Facility.values()[i].toString());
            facilitiesCheckBox[i].setTextColor(getResources().getColor(R.color.white));
            facilitiesCheckBox[i].setButtonTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkBlue)));

            final int k = i;
            facilitiesCheckBox[i].setOnCheckedChangeListener((x, isChecked) ->{
                if(isChecked){
                    selectedFacilities.add(Facility.values()[k]);
                }
                else {
                    selectedFacilities.remove(Facility.values()[k]);
                }
            });
        }

        int row = (Facility.values().length % 3 == 0) ? Facility.values().length / 3 : (Facility.values().length / 3) + 1;
        Iterator checkIt = Arrays.stream(facilitiesCheckBox).iterator();
        TableRow[] rowList = new TableRow[row];
        for (TableRow thisRow : rowList) {
            thisRow = new TableRow(mContext);
            thisRow.setGravity(Gravity.CENTER_VERTICAL);

            for(int i = 0; i < 3; i++){
                if(checkIt.hasNext()){
                    CheckBox thisCheck = (CheckBox) checkIt.next();
                    thisRow.addView(thisCheck);
                }
            }

            facilitiesSection.addView(thisRow);
        }
    }

    protected void handleAddBus(){
        String busNameValue = busName.getText().toString();
        String busCapacityValue = busCapacity.getText().toString();
        String busPriceValue = busPrice.getText().toString();

        if(busNameValue.isEmpty() || busCapacityValue.isEmpty() || busPriceValue.isEmpty()){
            viewToast(mContext, "Field Tidak Boleh Kosong");
            return;
        }

        if(!busCapacityValue.matches("\\d+")){
            viewToast(mContext, "Field Capacity harus berupa angka");
        }

        if(!busPriceValue.matches("\\d+")){
            viewToast(mContext, "Field Harga harus berupa angka");
        }

        int capacity = Integer.valueOf(busCapacityValue);
        int price = Integer.valueOf(busPriceValue);
        mApiService.createBus(loggedAccount.id, busNameValue, capacity, selectedFacilities, selectedBusType, price, selectedDepartureStation, selectedArrivalStation)
                .enqueue(new Callback<BaseResponse<Bus>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                        if(!response.isSuccessful()){
                            viewToast(mContext, "Application error " + response.code());
                            return;
                        }

                        BaseResponse<Bus> res = response.body();
                        if(res.success) {
                            finish();
                            moreActivity(mContext, ManageBusActivity.class);
                        }
                        viewToast(mContext, res.message);
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                        viewToast(mContext, "Ada problem pada server");
                    }
                });
    }
}