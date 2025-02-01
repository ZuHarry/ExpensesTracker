package com.example.expensestracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.DatePicker;
import java.util.Calendar;

import com.example.expensestracker.adapter.ExpenseCategoryAdapter;
import com.example.expensestracker.adapter.ExpenseIncomeAdapter;
import com.example.expensestracker.model.Expense;
import com.example.expensestracker.model.ExpenseCategory;
import com.example.expensestracker.model.ExpenseIncomeItem;
import com.example.expensestracker.sqlite.DatabaseExpense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HomePage extends AppCompatActivity implements ExpenseCategoryAdapter.OnCategoryClickListener {
    private TextView tvBottomSheetTitle;
    private Button btnExpense, btnIncome;
    private ExpenseIncomeAdapter adapter;
    private ExpenseCategoryAdapter categoryAdapter;
    private RecyclerView rvExpenseIncomeList, rvExpenseCategoryList;
    private List<ExpenseCategory> expenseCategories;
    private DatabaseExpense databaseExpense;
    private FloatingActionButton fabAdd;
    private TextView tvTotalBalanceValue;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        databaseExpense = new DatabaseExpense(this);

        btnExpense = findViewById(R.id.btnExpense);
        btnIncome = findViewById(R.id.btnIncome);
        tvBottomSheetTitle = findViewById(R.id.tvBottomSheetTitle);

        rvExpenseIncomeList = findViewById(R.id.rvExpenseIncomeList);
        rvExpenseIncomeList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExpenseIncomeAdapter(new ArrayList<>());
        rvExpenseIncomeList.setAdapter(adapter);
        tvTotalBalanceValue = findViewById(R.id.tvTotalBalanceValue);
        rvExpenseCategoryList = findViewById(R.id.rvExpenseCategoryList);
        rvExpenseCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        expenseCategories = new ArrayList<>();
        expenseCategories.add(new ExpenseCategory("Food", R.drawable.food, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Rent", R.drawable.rent, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Transportation", R.drawable.transportation, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Utilities", R.drawable.utilities, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Entertainment", R.drawable.entertainment, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Other", R.drawable.other, new ArrayList<>()));

        categoryAdapter = new ExpenseCategoryAdapter(expenseCategories, this);
        rvExpenseCategoryList.setAdapter(categoryAdapter);

        btnExpense.setOnClickListener(v -> showExpenseSection());
        btnIncome.setOnClickListener(v -> showIncomeSection());

        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddExpenseIncomeDialog());

        showExpenseSection();
        updateTotalBalance();
    }

    private void showAddExpenseIncomeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_expense_income, null);
        builder.setView(dialogView);

        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etAmount = dialogView.findViewById(R.id.etAmount);
        AutoCompleteTextView spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        RadioGroup radioGroupType = dialogView.findViewById(R.id.radioGroupType);
        RadioButton radioExpense = dialogView.findViewById(R.id.radioExpense);
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        EditText etDate = dialogView.findViewById(R.id.etDate);

        // Set up Date Picker for etDate
        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    HomePage.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            etDate.setText(dateFormat.format(calendar.getTime()));
                        }
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Set up category AutoCompleteTextView
        List<String> categoryNames = expenseCategories.stream()
                .map(ExpenseCategory::getName)
                .collect(Collectors.toList());
        categoryNames.add("Income"); // Add "Income" as a category
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categoryNames);
        // No need to setDropDownViewResource for AutoCompleteTextView
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Use the selectedCategory
            }
        });

        AlertDialog dialog = builder.create();

        btnAdd.setOnClickListener(v -> {
            String description = etDescription.getText().toString().trim();
            String amountString = etAmount.getText().toString().trim();
            // Get the selected item from the AutoCompleteTextView
            String category = spinnerCategory.getText().toString();
            String date = etDate.getText().toString().trim();
            boolean isExpense = radioExpense.isChecked();

            if (description.isEmpty() || amountString.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(HomePage.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountString);
            } catch (NumberFormatException e) {
                Toast.makeText(HomePage.this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Set category to "Income" if income is selected
            if (!isExpense) {
                category = "Income";
            }

            Expense newExpense = new Expense();
            newExpense.setDescription(description);
            newExpense.setAmount(amount);
            newExpense.setDate(date);
            newExpense.setCategory(category);
            newExpense.setDate(dateFormat.format(new Date()));

            databaseExpense.addExpense(newExpense);

            if (isExpense) {
                showExpenseSection();
            } else {
                showIncomeSection();
            }

            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showExpenseSection() {
        tvBottomSheetTitle.setText("Expenses");

        btnExpense.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        btnIncome.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
        btnExpense.setTextColor(ContextCompat.getColor(this, R.color.greenz));
        btnIncome.setTextColor(ContextCompat.getColor(this, R.color.greenz));

        rvExpenseCategoryList.setVisibility(View.VISIBLE);

        if (!expenseCategories.isEmpty()) {
            onCategoryClick(expenseCategories.get(0));
        }
    }

    private void showIncomeSection() {
        tvBottomSheetTitle.setText("Income");

        btnIncome.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        btnExpense.setBackgroundColor(ContextCompat.getColor(this, R.color.light_gray));
        btnIncome.setTextColor(ContextCompat.getColor(this, R.color.greenz));
        btnExpense.setTextColor(ContextCompat.getColor(this, R.color.greenz));

        rvExpenseCategoryList.setVisibility(View.GONE);

        List<Expense> incomes = databaseExpense.getExpensesByCategory("Income");
        List<ExpenseIncomeItem> incomeItems = incomes.stream()
                .map(expense -> new ExpenseIncomeItem(
                        Expense.getCategoryIconMap().getOrDefault(expense.getCategory(), R.drawable.other),
                        expense.getCategory(),
                        expense.getDescription(),
                        expense.getAmount()))
                .collect(Collectors.toList());
        adapter.updateItems(incomeItems);
    }

    @Override
    public void onCategoryClick(ExpenseCategory category) {
        Log.d("HomePage", "Category clicked: " + category.getName());

        List<Expense> expenses = databaseExpense.getExpensesByCategory(category.getName());
        List<ExpenseIncomeItem> filteredItems = expenses.stream()
                .map(expense -> new ExpenseIncomeItem(
                        Expense.getCategoryIconMap().getOrDefault(expense.getCategory(), R.drawable.other),
                        expense.getCategory(),
                        expense.getDescription(),
                        expense.getAmount()))
                .collect(Collectors.toList());

        adapter.updateItems(filteredItems);
    }

    private void updateTotalBalance() {
        double totalIncome = databaseExpense.getAllExpenses().stream()
                .filter(expense -> expense.getCategory().equals("Income"))
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalExpenses = databaseExpense.getAllExpenses().stream()
                .filter(expense -> !expense.getCategory().equals("Income"))
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalBalance = totalIncome - totalExpenses;

        tvTotalBalanceValue.setText(String.format("RM %.2f", totalBalance));
    }
}