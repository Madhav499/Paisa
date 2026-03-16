package com.example.paisa.repositories;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.paisa.api.ApiService;
import com.example.paisa.api.RetrofitClient;
import com.example.paisa.models.PortfolioStock;
import com.example.paisa.models.Stock;
import com.example.paisa.models.User;
import com.example.paisa.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockRepository {
    private ApiService apiService;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static final String TAG = "StockRepository";

    public StockRepository() {
        apiService = RetrofitClient.getApiService();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<JsonObject> getStockQuote(String symbol) {
        MutableLiveData<JsonObject> data = new MutableLiveData<>();
        apiService.getGlobalQuote("GLOBAL_QUOTE", symbol, Constants.ALPHA_VANTAGE_API_KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body().getAsJsonObject("Global Quote"));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<List<Stock>> searchStocks(String keywords) {
        MutableLiveData<List<Stock>> data = new MutableLiveData<>();
        apiService.getSymbolSearch("SYMBOL_SEARCH", keywords, Constants.ALPHA_VANTAGE_API_KEY).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Stock> stocks = new ArrayList<>();
                    JsonArray bestMatches = response.body().getAsJsonArray("bestMatches");
                    if (bestMatches != null) {
                        for (int i = 0; i < bestMatches.size(); i++) {
                            JsonObject match = bestMatches.get(i).getAsJsonObject();
                            stocks.add(new Stock(
                                    match.get("1. symbol").getAsString(),
                                    match.get("2. name").getAsString(),
                                    "0.00", "0.00", "0.00%", true
                            ));
                        }
                    }
                    data.setValue(stocks);
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<User> getUserData() {
        MutableLiveData<User> data = new MutableLiveData<>();
        String userId = auth.getUid();
        if (userId != null) {
            db.collection("users").document(userId).addSnapshotListener((value, error) -> {
                if (value != null && value.exists()) {
                    data.setValue(value.toObject(User.class));
                } else {
                    User newUser = new User(userId, auth.getCurrentUser().getEmail(), 100000.0);
                    db.collection("users").document(userId).set(newUser);
                    data.setValue(newUser);
                }
            });
        }
        return data;
    }

    public void updateBalance(double newBalance) {
        String userId = auth.getUid();
        if (userId != null) {
            db.collection("users").document(userId)
                    .update("balance", newBalance)
                    .addOnFailureListener(e -> Log.e(TAG, "Update balance failed", e));
        }
    }

    public void buyStock(String symbol, int quantity, double price) {
        String userId = auth.getUid();
        if (userId == null) return;

        DocumentReference portfolioRef = db.collection("users").document(userId)
                .collection("portfolio").document(symbol);
        DocumentReference userRef = db.collection("users").document(userId);

        db.runTransaction(transaction -> {
            // All reads first
            PortfolioStock current = transaction.get(portfolioRef).toObject(PortfolioStock.class);
            User user = transaction.get(userRef).toObject(User.class);

            if (user == null) return null;
            double totalCost = quantity * price;

            // All writes second
            if (current == null) {
                current = new PortfolioStock(symbol, quantity, price, price);
            } else {
                int newQty = current.getQuantity() + quantity;
                double newAvgPrice = ((current.getAvgPrice() * current.getQuantity()) + (price * quantity)) / newQty;
                current = new PortfolioStock(symbol, newQty, newAvgPrice, price);
            }
            
            transaction.set(portfolioRef, current);
            transaction.update(userRef, "balance", user.getBalance() - totalCost);
            return null;
        }).addOnFailureListener(e -> Log.e(TAG, "Buy transaction failed", e));
    }

    public void sellStock(String symbol, int quantity, double price) {
        String userId = auth.getUid();
        if (userId == null) return;

        DocumentReference portfolioRef = db.collection("users").document(userId)
                .collection("portfolio").document(symbol);
        DocumentReference userRef = db.collection("users").document(userId);

        db.runTransaction(transaction -> {
            // All reads first
            PortfolioStock current = transaction.get(portfolioRef).toObject(PortfolioStock.class);
            User user = transaction.get(userRef).toObject(User.class);

            if (current != null && current.getQuantity() >= quantity && user != null) {
                int newQty = current.getQuantity() - quantity;
                
                // All writes second
                if (newQty == 0) {
                    transaction.delete(portfolioRef);
                } else {
                    current.setQuantity(newQty);
                    current.setCurrentPrice(price);
                    transaction.set(portfolioRef, current);
                }
                transaction.update(userRef, "balance", user.getBalance() + (quantity * price));
            }
            return null;
        }).addOnFailureListener(e -> Log.e(TAG, "Sell transaction failed", e));
    }

    public LiveData<List<PortfolioStock>> getPortfolio() {
        MutableLiveData<List<PortfolioStock>> data = new MutableLiveData<>();
        String userId = auth.getUid();
        if (userId == null) return data;

        db.collection("users").document(userId).collection("portfolio")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        List<PortfolioStock> list = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            list.add(doc.toObject(PortfolioStock.class));
                        }
                        data.setValue(list);
                    }
                });
        return data;
    }
}