 package com.example.expensestracker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.expensestracker.fragment.ExpenseFragment;
import com.example.expensestracker.fragment.HomeExpenseFragment;
import com.example.expensestracker.fragment.HomeFragment;
import com.example.expensestracker.fragment.ProfileFragment;
import com.example.expensestracker.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

 public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set default fragment
        loadFragment(new HomeExpenseFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeExpenseFragment();
            } else if (item.getItemId() == R.id.nav_search) {
                selectedFragment = new ExpenseFragment();
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            return loadFragment(selectedFragment);
        });

    }

     public boolean loadFragment(Fragment fragment) {
         if (fragment != null) {
             getSupportFragmentManager()
                     .beginTransaction()
                     .setCustomAnimations(
                             R.anim.slide_in_right,  // Enter animation
                             R.anim.slide_out_left,  // Exit animation
                             R.anim.pop_enter,       // Pop backstack enter animation
                             R.anim.pop_exit         // Pop backstack exit animation
                     )
                     .replace(R.id.frame_layout, fragment)
                     .addToBackStack(null) // Enables back navigation with animation
                     .commit();
             return true;
         }
         return false;
     }
}