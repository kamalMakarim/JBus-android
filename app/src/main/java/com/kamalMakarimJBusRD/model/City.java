package com.kamalMakarimJBusRD.model;
public enum City {
    JAKARTA,
    DEPOK,
    BANDUNG,
    YOGYAKARTA,
    SEMARANG,
    SURABAYA,
    BALI;
    public String toStringNoOV() {
        switch (this) {
            case JAKARTA:
                return "Jakarta";
            case DEPOK:
                return "Depok";
            case BANDUNG:
                return "Bandung";
            case YOGYAKARTA:
                return "Yogyakarta";
            case SEMARANG:
                return "Semarang";
            case SURABAYA:
                return "Surabaya";
            case BALI:
                return "Bali";
            default:
                return "Unknown";
        }
    }

}
