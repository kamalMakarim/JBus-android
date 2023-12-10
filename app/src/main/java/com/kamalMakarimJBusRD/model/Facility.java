package com.kamalMakarimJBusRD.model;
public enum Facility {
    WIFI,
    AC,
    TOILET,
    LUNCH,
    LARGE_BAGGAGE,
    ELECTRIC_SOCKET,
    LCD_TV,
    COOL_BOX;

    public String toStringNoOV() {
        switch (this) {
            case WIFI:
                return "Wifi";
            case AC:
                return "AC";
            case TOILET:
                return "Toilet";
            case LUNCH:
                return "Lunch";
            case LARGE_BAGGAGE:
                return "Large Baggage";
            case ELECTRIC_SOCKET:
                return "Electric Socket";
            case LCD_TV:
                return "LCD TV";
            case COOL_BOX:
                return "Cool Box";
            default:
                return "Unknown";
        }
    }
}
