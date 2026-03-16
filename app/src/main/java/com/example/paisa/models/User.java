package com.example.paisa.models;

public class User {
    private String uid;
    private String email;
    private double balance;

    public User() {}

    public User(String uid, String email, double balance) {
        this.uid = uid;
        this.email = email;
        this.balance = balance;
    }

    public String getUid() { return uid; }
    public String getEmail() { return email; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}