package com.example.paisa.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.paisa.models.PortfolioStock;
import com.example.paisa.models.Stock;
import com.example.paisa.models.User;
import com.example.paisa.repositories.StockRepository;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class StockViewModel extends ViewModel {
    private StockRepository repository;
    private MutableLiveData<List<Stock>> marketStocks = new MutableLiveData<>();
    private LiveData<User> userData;

    public StockViewModel() {
        repository = new StockRepository();
        userData = repository.getUserData();
        loadInitialStocks();
    }

    private void loadInitialStocks() {
        // You can still keep some featured stocks or load from an API
        List<Stock> list = new ArrayList<>();
        list.add(new Stock("RELIANCE", "Reliance Industries Ltd.", "2985.40", "+35.20", "+1.24%", true));
        list.add(new Stock("TCS", "Tata Consultancy Services", "4120.15", "-12.40", "-0.30%", false));
        list.add(new Stock("INFY", "Infosys Limited", "1650.00", "+15.00", "+0.91%", true));
        marketStocks.setValue(list);
    }

    public LiveData<List<Stock>> getMarketStocks() {
        return marketStocks;
    }

    public LiveData<JsonObject> getStockDetail(String symbol) {
        return repository.getStockQuote(symbol);
    }

    public LiveData<List<Stock>> searchStocks(String query) {
        return repository.searchStocks(query);
    }

    public LiveData<User> getUserData() {
        return userData;
    }

    public void updateBalance(double newBalance) {
        repository.updateBalance(newBalance);
    }

    public void buyStock(String symbol, int quantity, double price) {
        repository.buyStock(symbol, quantity, price);
    }

    public void sellStock(String symbol, int quantity, double price) {
        repository.sellStock(symbol, quantity, price);
    }

    public LiveData<List<PortfolioStock>> getPortfolio() {
        return repository.getPortfolio();
    }
}