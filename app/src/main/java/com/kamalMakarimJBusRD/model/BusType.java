package com.kamalMakarimJBusRD.model;
public enum BusType {
    REGULER,
    DOUBLE_DECKER,
    HIGH_DECKER,
    MINIBUS;

    public String toStringNoOV() {
        switch (this) {
            case REGULER:
                return "Reguler";
            case DOUBLE_DECKER:
                return "Double Decker";
            case HIGH_DECKER:
                return "High Decker";
            case MINIBUS:
                return "Minibus";
            default:
                return "Unknown";
        }
    }
}
