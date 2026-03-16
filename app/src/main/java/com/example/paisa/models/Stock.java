package com.example.paisa.models;

import com.google.gson.annotations.SerializedName;

public class Stock {
    private String symbol;
    private String name;
    private String price;
    private String change;
    private String changePercent;
    private boolean isUp;

    public Stock(String symbol, String name, String price, String change, String changePercent, boolean isUp) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.isUp = isUp;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getChange() { return change; }
    public String getChangePercent() { return changePercent; }
    public boolean isUp() { return isUp; }
}