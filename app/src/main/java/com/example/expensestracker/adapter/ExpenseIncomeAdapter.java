package com.example.expensestracker.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensestracker.R;
import com.example.expensestracker.model.ExpenseIncomeItem;

import java.util.List;

public class ExpenseIncomeAdapter extends RecyclerView.Adapter<ExpenseIncomeAdapter.ViewHolder> {

    private List<ExpenseIncomeItem> items;

    public ExpenseIncomeAdapter(List<ExpenseIncomeItem> items) {
        this.items = items;
    }

    public void updateItems(List<ExpenseIncomeItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_income_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExpenseIncomeItem item = items.get(position);
        holder.ivCategoryIcon.setImageResource(item.getIconResId());
        holder.tvCategoryName.setText(item.getItemName());

        // Set amount text with +/- sign and color based on category
        if (item.getCategory().equals("Income")) {
            holder.tvAmount.setText(String.format("+ RM %.2f", item.getAmount()));
            holder.tvAmount.setTextColor(Color.parseColor("#008000")); // Green
        } else {
            holder.tvAmount.setText(String.format("- RM %.2f", item.getAmount()));
            holder.tvAmount.setTextColor(Color.parseColor("#FF0000")); // Red
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCategoryIcon;
        TextView tvCategoryName;
        TextView tvAmount;

        ViewHolder(View itemView) {
            super(itemView);
            ivCategoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
        }
    }
}