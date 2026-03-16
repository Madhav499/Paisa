package com.example.paisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.paisa.R;
import com.example.paisa.adapters.PortfolioAdapter;
import com.example.paisa.databinding.FragmentPortfolioBinding;
import com.example.paisa.models.PortfolioStock;
import com.example.paisa.viewmodels.StockViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PortfolioFragment extends Fragment {

    private FragmentPortfolioBinding binding;
    private PortfolioAdapter adapter;
    private StockViewModel viewModel;
    private List<PortfolioStock> portfolioList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPortfolioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);

        setupRecyclerView();
        observePortfolio();
    }

    private void setupRecyclerView() {
        adapter = new PortfolioAdapter(portfolioList);
        binding.rvPortfolio.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvPortfolio.setAdapter(adapter);
    }

    private void observePortfolio() {
        viewModel.getPortfolio().observe(getViewLifecycleOwner(), stocks -> {
            if (stocks != null) {
                portfolioList.clear();
                portfolioList.addAll(stocks);
                adapter.notifyDataSetChanged();
                calculateSummary();
                
                // Fetch latest prices for each stock in portfolio
                for (PortfolioStock stock : stocks) {
                    viewModel.getStockDetail(stock.getSymbol()).observe(getViewLifecycleOwner(), quote -> {
                        if (quote != null && quote.has("05. price")) {
                            double latestPrice = Double.parseDouble(quote.get("05. price").getAsString());
                            stock.setCurrentPrice(latestPrice);
                            adapter.notifyDataSetChanged();
                            calculateSummary();
                        }
                    });
                }
            }
        });
    }

    private void calculateSummary() {
        double totalInvested = 0;
        double currentValue = 0;
        for (PortfolioStock stock : portfolioList) {
            totalInvested += (stock.getQuantity() * stock.getAvgPrice());
            currentValue += (stock.getQuantity() * stock.getCurrentPrice());
        }
        
        double totalReturns = currentValue - totalInvested;
        
        binding.tvPortfolioValue.setText(String.format(Locale.getDefault(), "₹ %.2f", currentValue));
        binding.tvInvestedValue.setText(String.format(Locale.getDefault(), "₹ %.2f", totalInvested));
        binding.tvTotalReturnsVal.setText(String.format(Locale.getDefault(), "%s₹ %.2f", totalReturns >= 0 ? "+" : "", totalReturns));
        
        int color = totalReturns >= 0 ? R.color.profit : R.color.loss;
        binding.tvTotalReturnsVal.setTextColor(ContextCompat.getColor(requireContext(), color));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}