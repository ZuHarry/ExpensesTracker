package com.example.expensestracker.model;

import java.util.List;

public class ExpenseCategory {
    private String name;
    private int iconResId;
    private List<ExpenseIncomeItem> items;

    public ExpenseCategory(String name, int iconResId, List<ExpenseIncomeItem> items) {
        this.name = name;
        this.iconResId = iconResId;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public List<ExpenseIncomeItem> getItems() {
        return items;
    }

    public void setItems(List<ExpenseIncomeItem> items) {
        this.items = items;
    }
}