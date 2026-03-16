package com.example.paisa.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.paisa.R;
import com.example.paisa.adapters.StockAdapter;
import com.example.paisa.databinding.FragmentHomeBinding;
import com.example.paisa.models.PortfolioStock;
import com.example.paisa.viewmodels.StockViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private StockViewModel viewModel;
    private StockAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);
        
        String email = FirebaseAuth.getInstance().getCurrentUser() != null ? 
                FirebaseAuth.getInstance().getCurrentUser().getEmail() : "Investor";
        binding.tvUsername.setText(email.split("@")[0]);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        binding.rvTrending.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void observeViewModel() {
        viewModel.getMarketStocks().observe(getViewLifecycleOwner(), stocks -> {
            adapter = new StockAdapter(stocks, stock -> {
                Bundle bundle = new Bundle();
                bundle.putString("symbol", stock.getSymbol());
                Navigation.findNavController(requireView()).navigate(R.id.nav_stock_detail, bundle);
            });
            binding.rvTrending.setAdapter(adapter);
        });

        viewModel.getPortfolio().observe(getViewLifecycleOwner(), portfolio -> {
            if (portfolio != null) {
                double totalValue = 0;
                double totalInvested = 0;
                for (PortfolioStock stock : portfolio) {
                    totalValue += (stock.getQuantity() * stock.getCurrentPrice());
                    totalInvested += (stock.getQuantity() * stock.getAvgPrice());
                }
                double returns = totalValue - totalInvested;
                double returnsPercent = totalInvested > 0 ? (returns / totalInvested) * 100 : 0;

                binding.tvTotalValue.setText(String.format(Locale.getDefault(), "₹ %.2f", totalValue));
                binding.tvTodayPnl.setText(String.format(Locale.getDefault(), "%s₹ %.2f", returns >= 0 ? "+" : "", returns));
                binding.tvTotalReturns.setText(String.format(Locale.getDefault(), "%.2f%%", returnsPercent));
                
                int color = returns >= 0 ? R.color.profit : R.color.loss;
                binding.tvTodayPnl.setTextColor(getContext().getColor(color));
                binding.tvTotalReturns.setTextColor(getContext().getColor(color));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}