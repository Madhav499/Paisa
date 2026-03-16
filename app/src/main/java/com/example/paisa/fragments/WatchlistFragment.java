package com.example.paisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.paisa.adapters.StockAdapter;
import com.example.paisa.databinding.FragmentWatchlistBinding;
import com.example.paisa.models.Stock;
import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {

    private FragmentWatchlistBinding binding;
    private StockAdapter adapter;
    private List<Stock> watchlist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWatchlistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        loadWatchlist();
    }

    private void setupRecyclerView() {
        adapter = new StockAdapter(watchlist, stock -> {
            // Navigate to detail if needed
        });
        binding.rvWatchlist.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvWatchlist.setAdapter(adapter);
    }

    private void loadWatchlist() {
        // Dummy watchlist
        watchlist.add(new Stock("RELIANCE", "Reliance Industries Ltd.", "2,985.40", "+35.20", "+1.24%", true));
        watchlist.add(new Stock("INFY", "Infosys Limited", "1,650.00", "+15.00", "+0.91%", true));
        
        if (watchlist.isEmpty()) {
            binding.tvEmptyWatchlist.setVisibility(View.VISIBLE);
            binding.rvWatchlist.setVisibility(View.GONE);
        } else {
            binding.tvEmptyWatchlist.setVisibility(View.GONE);
            binding.rvWatchlist.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}