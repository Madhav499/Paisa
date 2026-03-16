package com.example.paisa.models;

public class PortfolioStock {
    private String symbol;
    private int quantity;
    private double avgPrice;
    private double currentPrice;

    public PortfolioStock() {} // Required for Firestore

    public PortfolioStock(String symbol, int quantity, double avgPrice, double currentPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
        this.currentPrice = currentPrice;
    }

    public String getSymbol() { return symbol; }
    public int getQuantity() { return quantity; }
    public double getAvgPrice() { return avgPrice; }
    public double getCurrentPrice() { return currentPrice; }
    
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setAvgPrice(double avgPrice) { this.avgPrice = avgPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }

    public double getInvestedValue() { return quantity * avgPrice; }
    public double getCurrentValue() { return quantity * currentPrice; }
    public double getProfitLoss() { return getCurrentValue() - getInvestedValue(); }
}