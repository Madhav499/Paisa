package com.example.paisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.paisa.R;
import com.example.paisa.adapters.StockAdapter;
import com.example.paisa.databinding.FragmentMarketsBinding;
import com.example.paisa.models.Stock;
import com.example.paisa.viewmodels.StockViewModel;
import java.util.ArrayList;
import java.util.List;

public class MarketsFragment extends Fragment {

    private FragmentMarketsBinding binding;
    private StockAdapter adapter;
    private StockViewModel viewModel;
    private List<Stock> stockList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarketsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);

        setupRecyclerView();
        setupSearch();

        viewModel.getMarketStocks().observe(getViewLifecycleOwner(), stocks -> {
            stockList.clear();
            stockList.addAll(stocks);
            adapter.notifyDataSetChanged();
            binding.swipeRefresh.setRefreshing(false);
        });

        binding.swipeRefresh.setOnRefreshListener(() -> {
            // In a real app, you'd trigger a refresh in ViewModel
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void setupRecyclerView() {
        adapter = new StockAdapter(stockList, stock -> {
            Bundle bundle = new Bundle();
            bundle.putString("symbol", stock.getSymbol());
            Navigation.findNavController(requireView()).navigate(R.id.nav_stock_detail, bundle);
        });
        binding.rvMarkets.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvMarkets.setAdapter(adapter);
    }

    private void setupSearch() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    searchStocks(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    searchStocks(newText);
                } else if (newText.isEmpty()) {
                    // Restore initial list
                    viewModel.getMarketStocks().observe(getViewLifecycleOwner(), stocks -> {
                        stockList.clear();
                        stockList.addAll(stocks);
                        adapter.notifyDataSetChanged();
                    });
                }
                return true;
            }
        });
    }

    private void searchStocks(String query) {
        viewModel.searchStocks(query).observe(getViewLifecycleOwner(), stocks -> {
            if (stocks != null) {
                stockList.clear();
                stockList.addAll(stocks);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}