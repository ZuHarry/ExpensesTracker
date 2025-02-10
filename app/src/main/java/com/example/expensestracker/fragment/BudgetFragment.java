package com.example.expensestracker.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.expensestracker.HomePage;
import com.example.expensestracker.R;
import com.example.expensestracker.model.Budget;
import com.example.expensestracker.sqlite.DatabaseBudget;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BudgetFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText targetBudgetEditText;
    private Button startDateButton;
    private Button endDateButton;
    private CheckBox notificationCheckBox;
    private Button saveBudgetButton;
    private TextView dateRangeTextView;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private BottomSheetBehavior<View> bottomSheetBehavior;

    private DatabaseBudget dbBudget;  // Database helper

    public BudgetFragment() {
        // Required empty public constructor
    }

    public static BudgetFragment newInstance(String param1, String param2) {
        BudgetFragment fragment = new BudgetFragment();
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
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        // Initialize DatabaseBudget
        dbBudget = new DatabaseBudget(getContext());

        FloatingActionButton fabBack = view.findViewById(R.id.fabBack);

        // Set onClickListener for the Budget Setting button
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the BudgetFragment
                navigateToBack();
            }
        });

        BarChart barChart = view.findViewById(R.id.budget_bar_chart);

        // Sample data (replace with your actual budget data)
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 30f)); // Day 1: Spent 30
        entries.add(new BarEntry(2, 80f)); // Day 2: Spent 80
        entries.add(new BarEntry(3, 60f)); // Day 3: Spent 60
        entries.add(new BarEntry(4, 50f)); // Day 4: Spent 50
        entries.add(new BarEntry(5, 70f)); // Day 5: Spent 70
        entries.add(new BarEntry(6, 60f)); // Day 6: Spent 60
        entries.add(new BarEntry(7, 20f)); // Day 7: Spent 20

        BarDataSet dataSet = new BarDataSet(entries, "Daily Spending");  // Label the data
        dataSet.setColor(Color.WHITE); // Set the color of the bars

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false); // Remove description label
        barChart.invalidate(); // Refresh the chart

        // Initialize BottomSheetBehavior
        View bottomSheet = view.findViewById(R.id.budget_setting_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Initialize UI elements
        targetBudgetEditText = view.findViewById(R.id.target_budget_edittext);
        startDateButton = view.findViewById(R.id.start_date_button);
        endDateButton = view.findViewById(R.id.end_date_button);
        notificationCheckBox = view.findViewById(R.id.notification_checkbox);
        saveBudgetButton = view.findViewById(R.id.save_budget_button);
        dateRangeTextView = view.findViewById(R.id.date_range_textview);

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        // Date Picker for Start Date
        DatePickerDialog.OnDateSetListener startDateSetListener = (v, year, month, dayOfMonth) -> {
            startDateCalendar.set(Calendar.YEAR, year);
            startDateCalendar.set(Calendar.MONTH, month);
            startDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateRangeTextView();
        };

        startDateButton.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), startDateSetListener,
                    startDateCalendar.get(Calendar.YEAR),
                    startDateCalendar.get(Calendar.MONTH),
                    startDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Date Picker for End Date
        DatePickerDialog.OnDateSetListener endDateSetListener = (v, year, month, dayOfMonth) -> {
            endDateCalendar.set(Calendar.YEAR, year);
            endDateCalendar.set(Calendar.MONTH, month);
            endDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateRangeTextView();
        };

        endDateButton.setOnClickListener(v -> {
            new DatePickerDialog(getContext(), endDateSetListener,
                    endDateCalendar.get(Calendar.YEAR),
                    endDateCalendar.get(Calendar.MONTH),
                    endDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Save Budget Button
        saveBudgetButton.setOnClickListener(v -> saveBudgetSettings());

        return view;
    }

    private void navigateToBack() {
        // Create a new instance of the BudgetFragment
        SettingsFragment settingsFragment = new SettingsFragment();

        // Get the Home Activity
        // Call `loadFragment` function created
        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).loadFragment(settingsFragment);
        }
    }

    private void updateDateRangeTextView() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = dateFormat.format(startDateCalendar.getTime());
        String endDate = dateFormat.format(endDateCalendar.getTime());
        dateRangeTextView.setText("Start Date: " + startDate + "\nEnd Date: " + endDate);
    }

    private void saveBudgetSettings() {
        String targetBudgetString = targetBudgetEditText.getText().toString();
        if (targetBudgetString.isEmpty()) {
            Toast.makeText(getContext(), "Please enter a target budget.", Toast.LENGTH_SHORT).show();
            return;
        }

        double targetBudget = Double.parseDouble(targetBudgetString);
        boolean enableNotifications = notificationCheckBox.isChecked();

        // Get user ID from SharedPreferences (assuming it's stored there)
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1); // -1 is the default value if not found

        if (userId == -1) {
            Toast.makeText(getContext(), "User ID not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = dateFormat.format(startDateCalendar.getTime());
        String endDate = dateFormat.format(endDateCalendar.getTime());

        // Create Budget object
        Budget budget = new Budget();
        budget.setBudget_target(targetBudget);
        budget.setStart_date(startDate);
        budget.setEnd_date(endDate);
        budget.setUser_id(userId);

        // Add to database
        try {
            dbBudget.addBudget(budget);
            Toast.makeText(getContext(), "Budget settings saved!", Toast.LENGTH_SHORT).show();

            // Optionally clear the input fields after saving
            targetBudgetEditText.setText("");
            notificationCheckBox.setChecked(false);
            updateDateRangeTextView();  // Refresh date range display

        } catch (Exception e) {
            Log.e("BudgetFragment", "Error saving budget: " + e.getMessage());
            Toast.makeText(getContext(), "Error saving budget.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbBudget != null) {
            dbBudget.close(); // Close the database connection when the fragment is destroyed
        }
    }

    public void showBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public void hideBottomSheet() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}