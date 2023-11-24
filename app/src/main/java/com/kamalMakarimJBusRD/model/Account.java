package com.kamalMakarimJBusRD.model;

public class Account extends Serializable {
    public String name;
    public String email;
    public String password;
    public double balance;
    public Renter company;

    public Account() {
        this.name = "";
        this.email = "";
        this.password = "";
        this.balance = 0;
        this.company = null;
    }
}
