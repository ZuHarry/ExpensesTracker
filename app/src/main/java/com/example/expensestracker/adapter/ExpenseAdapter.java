package com.example.expensestracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.expensestracker.R;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<ExpenseItem> expenseItems;

    public ExpenseAdapter(List<ExpenseItem> expenseItems) {
        this.expenseItems = expenseItems;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item_v2, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int position) {
        ExpenseItem item = expenseItems.get(position);
        holder.expenseName.setText(item.name);
        holder.expenseAmount.setText(String.format("- $%d", item.amount));
        holder.colorIndicator.setBackgroundColor(item.color);
    }

    @Override
    public int getItemCount() {
        return expenseItems.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        public View colorIndicator;
        public TextView expenseName;
        public TextView expenseAmount;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.color_indicator);
            expenseName = itemView.findViewById(R.id.expense_name);
            expenseAmount = itemView.findViewById(R.id.expense_amount);
        }
    }
}
