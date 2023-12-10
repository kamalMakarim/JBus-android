package com.kamalMakarimJBusRD.model;

import java.sql.Timestamp;
import java.util.List;
import com.kamalMakarimJBusRD.MainActivity;
public class Payment extends Invoice {
    private int busId;
    public Timestamp departureDate;
    public List<String> busSeats;

    public Bus getBus() {
        for(Bus bus : MainActivity.listBus) {
            if(bus.id == busId) {
                return bus;
            }
        }
        return null;
    }
}
