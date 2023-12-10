package com.kamalMakarimJBusRD.model;

public class Station extends Serializable {
    public String stationName;
    public City city;
    public String address;

    @Override
    public String toString() {
        if(stationName.contains("Terminal ")){
            stationName = stationName.substring(9);
        }
        return stationName;
    }
}