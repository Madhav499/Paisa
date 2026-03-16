package com.example.paisa.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.paisa.R;
import com.example.paisa.databinding.ItemStockBinding;
import com.example.paisa.models.Stock;
import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    private List<Stock> stockList;
    private OnStockClickListener listener;

    public interface OnStockClickListener {
        void onStockClick(Stock stock);
    }

    public StockAdapter(List<Stock> stockList, OnStockClickListener listener) {
        this.stockList = stockList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStockBinding binding = ItemStockBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StockViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock stock = stockList.get(position);
        holder.binding.tvStockSymbol.setText(stock.getSymbol());
        holder.binding.tvCompanyName.setText(stock.getName());
        holder.binding.tvStockPrice.setText(String.format("₹ %s", stock.getPrice()));
        holder.binding.tvStockChange.setText(stock.getChangePercent());

        int color = stock.isUp() ? R.color.profit : R.color.loss;
        holder.binding.tvStockChange.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), color));

        holder.itemView.setOnClickListener(v -> listener.onStockClick(stock));
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder {
        ItemStockBinding binding;
        public StockViewHolder(ItemStockBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}