package com.kamalMakarimJBusRD.model;

import java.sql.Timestamp;
import java.util.List;

public class Payment extends Invoice {
    private int busId;
    private Timestamp departureDate;
    private List<String> busSeats;
}
