package com.example.expensestracker.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensestracker.R;
import com.example.expensestracker.adapter.ExpenseCategoryAdapter;
import com.example.expensestracker.adapter.ExpenseIncomeAdapter;
import com.example.expensestracker.model.Expense;
import com.example.expensestracker.model.ExpenseCategory;
import com.example.expensestracker.model.ExpenseIncomeItem;
import com.example.expensestracker.sqlite.DatabaseExpense;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class HomeExpenseFragment extends Fragment {

    private TextView tvBottomSheetTitle;
    private Button btnExpense, btnIncome;
    private ExpenseIncomeAdapter adapter;
    private ExpenseCategoryAdapter categoryAdapter;
    private RecyclerView rvExpenseIncomeList, rvExpenseCategoryList;
    private List<ExpenseCategory> expenseCategories;
    private DatabaseExpense databaseExpense;
    private FloatingActionButton fabAdd;
    private TextView tvTotalBalanceValue;
    private TextView dateTimeTextView;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private BottomSheetDialog bottomSheetDialog; // Declare BottomSheetDialog


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeExpenseFragment newInstance(String param1, String param2) {
        HomeExpenseFragment fragment = new HomeExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_expense, container, false);
        return view; // Do not re-inflate again

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseExpense = new DatabaseExpense(requireActivity());


        dateTimeTextView = view.findViewById(R.id.dateTime);
        updateCurrentDate();
        btnExpense = view.findViewById(R.id.btnExpense);
        btnIncome = view.findViewById(R.id.btnIncome);
        fabAdd = view.findViewById(R.id.fabAdd);
        tvBottomSheetTitle = view.findViewById(R.id.tvBottomSheetTitle);
        rvExpenseIncomeList = view.findViewById(R.id.rvExpenseIncomeList);
        rvExpenseCategoryList = view.findViewById(R.id.rvExpenseCategoryList);
        tvTotalBalanceValue = view.findViewById(R.id.tvTotalBalanceValue);

        rvExpenseIncomeList.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ExpenseIncomeAdapter(new ArrayList<>());
        rvExpenseIncomeList.setAdapter(adapter);

        rvExpenseCategoryList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        expenseCategories = new ArrayList<>();
        expenseCategories.add(new ExpenseCategory("Food", R.drawable.food, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Rent", R.drawable.rent, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Transportation", R.drawable.transportation, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Utilities", R.drawable.utilities, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Entertainment", R.drawable.entertainment, new ArrayList<>()));
        expenseCategories.add(new ExpenseCategory("Other", R.drawable.other, new ArrayList<>()));

        categoryAdapter = new ExpenseCategoryAdapter(expenseCategories, this::onCategoryClick);
        rvExpenseCategoryList.setAdapter(categoryAdapter);

        // Set click listeners properly
        btnExpense.setOnClickListener(v -> showExpenseSection());
        btnIncome.setOnClickListener(v -> showIncomeSection());
        fabAdd.setOnClickListener(v -> showAddExpenseIncomeBottomSheet());

        showExpenseSection();
        updateTotalBalance();
    }


    private void updateCurrentDate() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        dateTimeTextView.setText(formattedDate);
    }


    private void showAddExpenseIncomeBottomSheet() { // Update to use BottomSheet
        bottomSheetDialog = new BottomSheetDialog(requireActivity());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.dialog_add_expense_income, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        EditText etDescription = bottomSheetView.findViewById(R.id.etDescription);
        EditText etAmount = bottomSheetView.findViewById(R.id.etAmount);
        AutoCompleteTextView spinnerCategory = bottomSheetView.findViewById(R.id.spinnerCategory);
        RadioButton radioExpense = bottomSheetView.findViewById(R.id.radioExpense);
        Button btnAdd = bottomSheetView.findViewById(R.id.btnAdd);
        Button btnCancel = bottomSheetView.findViewById(R.id.btnCancel);
        EditText etDate = bottomSheetView.findViewById(R.id.etDate);

        // Set up Date Picker for etDate
        etDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
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
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, categoryNames);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // Use the selectedCategory
            }
        });

        btnAdd.setOnClickListener(v -> {
            String description = etDescription.getText().toString().trim();
            String amountString = etAmount.getText().toString().trim();
            String category = spinnerCategory.getText().toString();
            String date = etDate.getText().toString().trim();
            boolean isExpense = radioExpense.isChecked();

            if (description.isEmpty() || amountString.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountString);
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Invalid amount format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isExpense) {
                category = "Income";
            }

            Expense newExpense = new Expense();
            newExpense.setDescription(description);
            newExpense.setAmount(amount);
            newExpense.setDate(date);
            newExpense.setCategory(category);
            //  newExpense.setDate(dateFormat.format(new Date())); //Use selected date instead

            databaseExpense.addExpense(newExpense);

            if (isExpense) {
                showExpenseSection();
            } else {
                showIncomeSection();
            }

            bottomSheetDialog.dismiss(); // Dismiss the BottomSheet
        });

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss()); // Dismiss the BottomSheet

        bottomSheetDialog.show(); // Show the BottomSheet
        updateTotalBalance();
    }


    private void showExpenseSection() {
        tvBottomSheetTitle.setText("Expenses");

        btnExpense.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        btnIncome.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_gray));
        btnExpense.setTextColor(ContextCompat.getColor(getContext(), R.color.greenz));
        btnIncome.setTextColor(ContextCompat.getColor(getContext(), R.color.greenz));

        rvExpenseCategoryList.setVisibility(View.VISIBLE);

        if (!expenseCategories.isEmpty()) {
            onCategoryClick(expenseCategories.get(0));
        }
    }

    private void showIncomeSection() {
        tvBottomSheetTitle.setText("Income");

        btnIncome.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        btnExpense.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_gray));
        btnIncome.setTextColor(ContextCompat.getColor(getContext(), R.color.greenz));
        btnExpense.setTextColor(ContextCompat.getColor(getContext(), R.color.greenz));

        rvExpenseCategoryList.setVisibility(View.GONE);

        // Always show all income, regardless of the week
        List<Expense> incomes = databaseExpense.getExpensesByCategory("Income");
        List<ExpenseIncomeItem> incomeItems = incomes.stream()
                .map(expense -> new ExpenseIncomeItem(
                        Expense.getCategoryIconMap().getOrDefault(expense.getCategory(), R.drawable.salary),
                        expense.getCategory(),
                        expense.getDescription(),
                        expense.getAmount()))
                .collect(Collectors.toList());
        adapter.updateItems(incomeItems);
    }


    public void onCategoryClick(ExpenseCategory category) {
        Log.d("HomePage", "Category clicked: " + category.getName());

        List<Expense> expenses = getExpensesForCurrentWeekByCategory(category.getName());

        List<ExpenseIncomeItem> filteredItems = expenses.stream()
                .map(expense -> new ExpenseIncomeItem(
                        Expense.getCategoryIconMap().getOrDefault(expense.getCategory(), R.drawable.other),
                        expense.getCategory(),
                        expense.getDescription(),
                        expense.getAmount()))
                .collect(Collectors.toList());

        adapter.updateItems(filteredItems);
    }

    private List<Expense> getExpensesForCurrentWeekByCategory(String category) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();

        cal.add(Calendar.DAY_OF_WEEK, 7);
        Date endOfWeek = cal.getTime();

        List<Expense> allExpenses = databaseExpense.getExpensesByCategory(category);
        List<Expense> filteredExpenses = new ArrayList<>();

        for (Expense expense : allExpenses) {
            try {
                Date expenseDate = dateFormat.parse(expense.getDate());
                if (expenseDate != null && expenseDate.compareTo(startOfWeek) >= 0 && expenseDate.compareTo(endOfWeek) < 0) {
                    filteredExpenses.add(expense);
                }
            } catch (ParseException e) {
                Log.e("HomeExpenseFragment", "Error parsing date: " + expense.getDate(), e);
            }
        }

        return filteredExpenses;
    }

    private List<Expense> getExpensesForCurrentWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();

        cal.add(Calendar.DAY_OF_WEEK, 7);
        Date endOfWeek = cal.getTime();

        List<Expense> allExpenses = databaseExpense.getAllExpenses();
        List<Expense> filteredExpenses = new ArrayList<>();

        for (Expense expense : allExpenses) {
            try {
                Date expenseDate = dateFormat.parse(expense.getDate());
                if (expenseDate != null && expenseDate.compareTo(startOfWeek) >= 0 && expenseDate.compareTo(endOfWeek) < 0) {
                    filteredExpenses.add(expense);
                }
            } catch (ParseException e) {
                Log.e("HomeExpenseFragment", "Error parsing date: " + expense.getDate(), e);
            }
        }

        return filteredExpenses;
    }


    private void updateTotalBalance() {
        List<Expense> currentWeekExpenses = getExpensesForCurrentWeek();

        // Total income is *always* all income, not just this week
        double totalIncome = databaseExpense.getAllExpenses().stream()
                .filter(expense -> expense.getCategory().equals("Income"))
                .mapToDouble(Expense::getAmount)
                .sum();

        // Total expenses consider expenses since the start of the app, or total expense.
        double totalExpenses = databaseExpense.getAllExpenses().stream()
                .filter(expense -> !expense.getCategory().equals("Income"))
                .mapToDouble(Expense::getAmount)
                .sum();

        double totalBalance = totalIncome - totalExpenses;

        tvTotalBalanceValue.setText(String.format("RM %.2f", totalBalance));
    }
}