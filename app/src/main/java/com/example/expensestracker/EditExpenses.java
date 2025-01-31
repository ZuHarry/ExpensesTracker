package com.example.expensestracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditExpenses extends AppCompatActivity {
    EditText amountEditText, dateEditText, descriptionEditText;
    Spinner categorySpinner;
    Button addExpenseButton, deleteExpenseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expenses);

        amountEditText = findViewById(R.id.amountEditText);
        dateEditText = findViewById(R.id.dateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        addExpenseButton = findViewById(R.id.editExpenseButton);
        deleteExpenseButton = findViewById(R.id.deleteExpenseButton);

        // Date Picker
        dateEditText.setOnClickListener(v -> showDatePicker());

        // Add Expense Button Click
        addExpenseButton.setOnClickListener(v -> addExpense());

    }

    private void showDatePicker() {
        final Calendar myCalendar = Calendar.getInstance();
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year1, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year1);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel(myCalendar);
        };

        new DatePickerDialog(this, dateSetListener, year, month, day).show();
    }

    private void updateDateLabel(Calendar myCalendar) {
        String myFormat = "dd/MM/yyyy"; // You can change the format
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    private void addExpense() {
        String amount = amountEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String date = dateEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Perform your logic to save the expense data here.
        // You can use a database, file, or API to store the data.

        // For now, let's just show a Toast message
        String message = "Amount: " + amount + "\nCategory: " + category +
                "\nDate: " + date + "\nDescription: " + description;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        // Clear the input fields after adding expense (Optional)
        amountEditText.setText("");
        descriptionEditText.setText("");
    }
}