package com.kamalMakarimJBusRD.model;

import java.text.NumberFormat;
import java.util.Locale;

public class Price {
    public double rebate;
    public double price;

    public String formatedPrice() {
        int priceInIDR = (int) price;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return currencyFormat.format(priceInIDR);
    }

}
