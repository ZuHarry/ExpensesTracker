package com.example.expensestracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.expensestracker.sqlite.DatabaseHelper;

public class SignUp extends AppCompatActivity {

    TextView backLogin;
    DatabaseHelper db;
    EditText editTextUsername, editTextFullname, editTextEmail, editTextPassword;
    Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backLogin = findViewById(R.id.textViewBackLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        editTextFullname = findViewById(R.id.editTextSignFullName);
        editTextUsername = findViewById(R.id.editTextSignUsername);
        editTextEmail = findViewById(R.id.editTextSignUsername);
        editTextPassword = findViewById(R.id.editTextSignPassword);

        // Initialize the DatabaseHelper instance
        db = new DatabaseHelper(this);

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        buttonSignUp.setOnClickListener(this::insertUser);
    }

    public void insertUser(View view) {
        boolean isInserted = db.insertUserData(editTextUsername.getText().toString(),
                editTextFullname.getText().toString(), editTextEmail.getText().toString(),
                editTextPassword.getText().toString());
        if (isInserted)
            Toast.makeText(SignUp.this, "Data Inserted", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(SignUp.this, "Data Not Inserted", Toast.LENGTH_SHORT).show();
    }
}
