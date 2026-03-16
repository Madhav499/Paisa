package com.example.paisa.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.paisa.R;
import com.example.paisa.databinding.DialogTradeBinding;
import com.example.paisa.databinding.FragmentStockDetailBinding;
import com.example.paisa.models.User;
import com.example.paisa.viewmodels.StockViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StockDetailFragment extends Fragment {

    private FragmentStockDetailBinding binding;
    private StockViewModel viewModel;
    private String symbol;
    private double currentPrice = 0.0;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStockDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(StockViewModel.class);

        if (getArguments() != null) {
            symbol = getArguments().getString("symbol");
            binding.tvDetailSymbol.setText(symbol);
            fetchStockData();
        }

        viewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            this.currentUser = user;
        });

        setupChart();
        
        binding.btnBuy.setOnClickListener(v -> showTradeDialog(true));
        binding.btnSell.setOnClickListener(v -> showTradeDialog(false));
    }

    private void fetchStockData() {
        viewModel.getStockDetail(symbol).observe(getViewLifecycleOwner(), quote -> {
            if (quote != null) {
                try {
                    currentPrice = Double.parseDouble(quote.get("05. price").getAsString());
                    String change = quote.get("09. change").getAsString();
                    String changePercent = quote.get("10. change percent").getAsString();

                    binding.tvDetailPrice.setText(String.format(Locale.getDefault(), "₹ %.2f", currentPrice));
                    binding.tvDetailChange.setText(String.format("%s (%s)", change, changePercent));
                    
                    boolean isUp = !change.startsWith("-");
                    binding.tvDetailChange.setTextColor(isUp ? 
                        getContext().getColor(R.color.profit) : getContext().getColor(R.color.loss));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showTradeDialog(boolean isBuy) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        DialogTradeBinding dialogBinding = DialogTradeBinding.inflate(getLayoutInflater());
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvDialogTitle.setText((isBuy ? "BUY " : "SELL ") + symbol);
        dialogBinding.tvDialogPrice.setText(String.format(Locale.getDefault(), "Price: ₹ %.2f", currentPrice));
        dialogBinding.btnConfirmTrade.setText(isBuy ? "CONFIRM BUY" : "CONFIRM SELL");
        dialogBinding.btnConfirmTrade.setBackgroundColor(isBuy ? 
            getContext().getColor(R.color.profit) : getContext().getColor(R.color.loss));

        if (currentUser != null) {
            dialogBinding.tvWalletBalance.setText(String.format(Locale.getDefault(), "Available Balance: ₹ %.2f", currentUser.getBalance()));
        }

        dialogBinding.etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int qty = Integer.parseInt(s.toString());
                    double total = qty * currentPrice;
                    dialogBinding.tvTotalValue.setText(String.format(Locale.getDefault(), "Total Value: ₹ %.2f", total));
                    
                    if (isBuy && total > currentUser.getBalance()) {
                        dialogBinding.tvTotalValue.setTextColor(Color.RED);
                        dialogBinding.btnConfirmTrade.setEnabled(false);
                    } else {
                        dialogBinding.tvTotalValue.setTextColor(Color.WHITE);
                        dialogBinding.btnConfirmTrade.setEnabled(true);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        dialogBinding.btnConfirmTrade.setOnClickListener(v -> {
            String qtyStr = dialogBinding.etQuantity.getText().toString();
            if (qtyStr.isEmpty()) return;
            
            int qty = Integer.parseInt(qtyStr);
            if (isBuy) {
                viewModel.buyStock(symbol, qty, currentPrice);
            } else {
                viewModel.sellStock(symbol, qty, currentPrice);
            }
            dialog.dismiss();
            Toast.makeText(getContext(), (isBuy ? "Buy" : "Sell") + " Order Successful", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private void setupChart() {
        LineChart chart = binding.lineChart;
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 2900));
        entries.add(new Entry(1, 2920));
        entries.add(new Entry(2, 2910));
        entries.add(new Entry(3, 2950));
        entries.add(new Entry(4, (float)currentPrice));

        LineDataSet dataSet = new LineDataSet(entries, "Price");
        dataSet.setColor(Color.parseColor("#00D09C"));
        dataSet.setCircleColor(Color.parseColor("#00D09C"));
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#00D09C"));
        dataSet.setFillAlpha(50);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setEnabled(false);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisRight().setEnabled(false);
        chart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
