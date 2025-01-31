package com.example.expensestracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the ImageView using view
        ImageView imageViewAddExpenses = view.findViewById(R.id.imageViewAddExpenses);
        ImageView imageViewEditExpenses = view.findViewById(R.id.imageViewEditExpenses);
        Fragment selectedFragment = null;

        // Example: Set an onClickListener
        imageViewAddExpenses.setOnClickListener(v -> {
            // Handle click event
            loadFragment(new AddExpenseFragment());
            //BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        });

        // Example: Set an onClickListener
        imageViewEditExpenses.setOnClickListener(v -> {
            // Handle click event
            loadFragment(new EditExpenseFragment());
            //BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        });
        return view;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            requireActivity().getSupportFragmentManager()
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