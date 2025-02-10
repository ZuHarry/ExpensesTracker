package com.example.expensestracker.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.expensestracker.R;
import com.example.expensestracker.sqlite.DatabaseHelper;

import java.util.List;

public class ProfileFragment extends Fragment {

    private EditText editTextFullName, editTextEmail, editTextName, editTextPassword;
    private Button buttonSave;
    DatabaseHelper db;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI components
        editTextFullName = view.findViewById(R.id.editTextFullName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextName = view.findViewById(R.id.editTextName);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonSave = view.findViewById(R.id.buttonSave);

        // Load profile data when the fragment is created
        loadProfileData();

        // Save button click listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        return view;
    }

    // Method to load profile data from the database
    public void loadProfileData() {
        // Initialize the DatabaseHelper with the context
        db = new DatabaseHelper(getContext());

        // Get the profile data (this returns a List with the username, email, and fullname)
        List<String> data = db.getProfile();

        // Check if the data is not empty
        if (data.size() > 0) {
            // Set the retrieved values into your UI elements
            editTextName.setText(data.get(0));  // Username at index 0
            editTextEmail.setText(data.get(1)); // Email at index 1
            editTextFullName.setText(data.get(2)); // Full name at index 2
        } else {
            // Handle case where no data was found
            Toast.makeText(getContext(), "Profile data not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to save the profile data (for now, just a Toast message)
    private void saveProfileData() {
        String fullName = editTextFullName.getText().toString();
        String name = editTextName.getText().toString();  // This field corresponds to the "Name" in the XML
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        // Initialize the DatabaseHelper
        db = new DatabaseHelper(getContext());

        // Get the logged-in user's ID (You can modify this based on your implementation)
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // Default to -1 if no user_id is saved

        if (userId != -1) {
            // Update the profile data in the database
            boolean isUpdated = db.updateUserProfile(userId, fullName, name, email, password);

            if (isUpdated) {
                Toast.makeText(getContext(), "Profile Saved!", Toast.LENGTH_SHORT).show();
                // Optionally, you can reload the profile data to reflect the changes
                loadProfileData();
            } else {
                Toast.makeText(getContext(), "Failed to save profile", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
        }
    }
}
