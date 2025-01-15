package com.example.expensestracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensestracker.databinding.ActivityExpenseModuleBinding;

public class ActivityExpenseModule extends AppCompatActivity {

    ActivityExpenseModuleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_module);
        binding = ActivityExpenseModuleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.fabAdd.setOnClickListener(view -> {
            // Inflate the popup layout
            LayoutInflater inflater = LayoutInflater.from(this);
            View popupView = inflater.inflate(R.layout.popupexpense, null);

            // Create AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(popupView);

            // Show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

    }
}