package com.example.expensestracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensestracker.R;
import com.example.expensestracker.model.ExpenseCategory;
import java.util.List;

public class ExpenseCategoryAdapter extends RecyclerView.Adapter<ExpenseCategoryAdapter.CategoryViewHolder> {

    private List<ExpenseCategory> categories;
    private OnCategoryClickListener listener;

    public ExpenseCategoryAdapter(List<ExpenseCategory> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        ExpenseCategory category = categories.get(position);
        holder.categoryIcon.setImageResource(category.getIconResId());
        holder.categoryName.setText(category.getName());
        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryName;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.ivCategoryIcon);
            categoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(ExpenseCategory category);
    }
}