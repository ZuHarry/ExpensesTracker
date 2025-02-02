package com.example.expensestracker.model;

import com.example.expensestracker.R;

import java.util.HashMap;
import java.util.Map;

public class Expense {
    int id;
    String description;
    double amount;
    String category;
    String date;
    private String itemName;

    // Empty constructor
    public Expense() {
    }

    private static final Map<String, Integer> CATEGORY_ICONS = new HashMap<>();

    static {
        CATEGORY_ICONS.put("Food", R.drawable.food);
        CATEGORY_ICONS.put("Rent", R.drawable.rent);
        CATEGORY_ICONS.put("Transportation", R.drawable.transportation);
        CATEGORY_ICONS.put("Entertainment", R.drawable.entertainment);
        CATEGORY_ICONS.put("Utilities", R.drawable.utilities);
        CATEGORY_ICONS.put("Other", R.drawable.other);
        CATEGORY_ICONS.put("Salary", R.drawable.salary);
    }
    public static Map<String, Integer> getCategoryIconMap() {
        return CATEGORY_ICONS;
    }
    public int getCategoryIcon() {
        Integer iconId = CATEGORY_ICONS.get(category);
        return (iconId != null) ? iconId : R.drawable.other; // Default icon if not found
    }

    // Constructor with ID
    public Expense(int id, String description, double amount, String category, String date) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Constructor without ID (for adding new expenses)
    public Expense(String description, double amount, String category, String date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}