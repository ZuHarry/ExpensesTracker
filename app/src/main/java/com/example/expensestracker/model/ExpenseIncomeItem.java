package com.example.expensestracker.model;

public class ExpenseIncomeItem {
    private int iconResId;
    private String categoryName;
    private String itemName;
    private double amount;

    public ExpenseIncomeItem(int iconResId, String categoryName, String itemName, double amount) {
        this.iconResId = iconResId;
        this.categoryName = categoryName;
        this.itemName = itemName;
        this.amount = amount;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getItemName() {
        return itemName;
    }

    public double getAmount() {
        return amount;
    }

    // Add this method
    public String getCategory() {
        return categoryName;
    }
}