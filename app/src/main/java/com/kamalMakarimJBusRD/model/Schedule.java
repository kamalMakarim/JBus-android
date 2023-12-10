package com.kamalMakarimJBusRD.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.function.Predicate;

public class Schedule {
    public Timestamp departureSchedule;
    public Map<String, Boolean> seatAvailability;
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-d HH:mm:ss");
        String formattedDepartureSchedule = dateFormat.format(this.departureSchedule.getTime());
        int all = seatAvailability.size();

        return "Departure time\t: " + formattedDepartureSchedule + "\nOccupied\t: " + getOccupied() + "/" + all;
    }

    public int getOccupied() {
        int occupied = 0;
        for (Map.Entry<String, Boolean> entry : seatAvailability.entrySet()) {
            if (!entry.getValue()) {
                occupied++;
            }
        }
        return occupied;
    }
    public int getAvailable() {
        int available = 0;
        for (Map.Entry<String, Boolean> entry : seatAvailability.entrySet()) {
            if (entry.getValue()) {
                available++;
            }
        }
        return available;
    }
}
