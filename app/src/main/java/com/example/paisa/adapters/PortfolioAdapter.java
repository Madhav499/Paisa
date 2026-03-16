package com.example.paisa.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.paisa.R;
import com.example.paisa.databinding.ItemPortfolioStockBinding;
import com.example.paisa.models.PortfolioStock;
import java.util.List;
import java.util.Locale;

public class PortfolioAdapter extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {

    private List<PortfolioStock> portfolioList;

    public PortfolioAdapter(List<PortfolioStock> portfolioList) {
        this.portfolioList = portfolioList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPortfolioStockBinding binding = ItemPortfolioStockBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PortfolioStock stock = portfolioList.get(position);
        holder.binding.tvPortfolioSymbol.setText(stock.getSymbol());
        holder.binding.tvPortfolioQty.setText(String.format(Locale.getDefault(), "%d Qty", stock.getQuantity()));
        holder.binding.tvPortfolioAvg.setText(String.format(Locale.getDefault(), "₹ %.2f", stock.getAvgPrice()));
        holder.binding.tvPortfolioLtp.setText(String.format(Locale.getDefault(), "₹ %.2f", stock.getCurrentPrice()));
        
        double pnl = stock.getProfitLoss();
        holder.binding.tvPortfolioPnl.setText(String.format(Locale.getDefault(), "%s₹ %.2f", pnl >= 0 ? "+" : "", pnl));
        
        int color = pnl >= 0 ? R.color.profit : R.color.loss;
        holder.binding.tvPortfolioPnl.setTextColor(ContextCompat.getColor(holder.binding.getRoot().getContext(), color));
    }

    @Override
    public int getItemCount() {
        return portfolioList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemPortfolioStockBinding binding;
        public ViewHolder(ItemPortfolioStockBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}