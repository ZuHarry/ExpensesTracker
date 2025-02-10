package com.example.expensestracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.expensestracker.sqlite.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button loginButton;
    TextView textViewSignUp;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);  // Pastikan nama XML betul

        // Sambungkan elemen dengan ID dari XML
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        databaseHelper = new DatabaseHelper(this); // Panggil database

        // Fungsi Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter all fields!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = checkUser(username, password);
                    if (isValid) {

                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomePage.class);
                        startActivity(intent);
                        finish();  // Tutup LoginActivity
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Pergi ke halaman Sign Up
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    // Fungsi untuk semak user dalam database
    private boolean checkUser(String username, String password) {
        Cursor cursor = databaseHelper.getUser(username, password);
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
