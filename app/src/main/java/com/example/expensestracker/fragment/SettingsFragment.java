package com.example.expensestracker.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.expensestracker.HomePage;
import com.example.expensestracker.R;

public class SettingsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Find the Budget Setting button
        Button budgetSettingButton = view.findViewById(R.id.budget_setting_button);

        // Set onClickListener for the Budget Setting button
        budgetSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the BudgetFragment
                navigateToBudgetFragment();
            }
        });

        // Find the Budget Setting button
        Button profileButton = view.findViewById(R.id.profile_setting_button);

        // Set onClickListener for the Budget Setting button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the BudgetFragment
                navigateToProfileFragment();
            }
        });

        return view;
    }

    private void navigateToBudgetFragment() {
        // Create a new instance of the BudgetFragment
        BudgetFragment budgetFragment = new BudgetFragment();

        // Get the Home Activity
        // Call `loadFragment` function created
        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).loadFragment(budgetFragment);
        }
    }

    private void navigateToProfileFragment() {
        // Create a new instance of the BudgetFragment
        ProfileFragment profileFragment = new ProfileFragment();

        // Get the Home Activity
        // Call `loadFragment` function created
        if (getActivity() instanceof HomePage) {
            ((HomePage) getActivity()).loadFragment(profileFragment);
        }
    }
}