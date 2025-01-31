package com.example.expensestracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {

    private Button expenseButton, incomeButton;
    private CustomPieChartView pieChartView;
    private RecyclerView recyclerView;
    private TextView balanceTextView;
    private ExpenseAdapter adapter;
    private List<ExpenseItem> expenseItems;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        expenseButton = view.findViewById(R.id.expense_button);
        incomeButton = view.findViewById(R.id.income_button);
        pieChartView = view.findViewById(R.id.pie_chart);
        recyclerView = view.findViewById(R.id.expenses_recycler_view);
        balanceTextView = view.findViewById(R.id.total_balance_value);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        expenseButton.setOnClickListener(v -> setExpenseMode());
        incomeButton.setOnClickListener(v -> setIncomeMode());

        setExpenseMode(); // Initial mode
        return view;
    }

    private void setExpenseMode() {
        expenseButton.setBackgroundResource(R.drawable.toggle_left);
        expenseButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        incomeButton.setBackgroundResource(R.drawable.toggle_right);
        incomeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));

        balanceTextView.setText("$9400");

        int yellow = ContextCompat.getColor(requireContext(), R.color.yellow);
        int purple = ContextCompat.getColor(requireContext(), R.color.purple);
        int red = ContextCompat.getColor(requireContext(), R.color.red);

        List<CustomPieChartView.PieData> pieDataList = new ArrayList<>();
        pieDataList.add(new CustomPieChartView.PieData(33.3f, yellow));
        pieDataList.add(new CustomPieChartView.PieData(33.3f, purple));
        pieDataList.add(new CustomPieChartView.PieData(33.4f, red));
        pieChartView.setPieData(pieDataList);

        expenseItems = new ArrayList<>();
        expenseItems.add(new ExpenseItem("Shopping", 120, yellow));
        expenseItems.add(new ExpenseItem("Subscription", 80, purple));
        expenseItems.add(new ExpenseItem("Food", 32, red));
        adapter = new ExpenseAdapter(expenseItems);
        recyclerView.setAdapter(adapter);
    }

    private void setIncomeMode() {
        incomeButton.setBackgroundResource(R.drawable.toggle_left);
        incomeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        expenseButton.setBackgroundResource(R.drawable.toggle_right);
        expenseButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));

        balanceTextView.setText("$6000");

        int black = ContextCompat.getColor(requireContext(), R.color.black);
        int green = ContextCompat.getColor(requireContext(), R.color.green);

        List<CustomPieChartView.PieData> pieDataList = new ArrayList<>();
        pieDataList.add(new CustomPieChartView.PieData(50f, green));
        pieDataList.add(new CustomPieChartView.PieData(50f, black));
        pieChartView.setPieData(pieDataList);

        expenseItems = new ArrayList<>();
        expenseItems.add(new ExpenseItem("Salary", 5000, green));
        expenseItems.add(new ExpenseItem("Passive Income", 1000, black));
        adapter = new ExpenseAdapter(expenseItems);
        recyclerView.setAdapter(adapter);
    }

    static class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
        private List<ExpenseItem> expenseItems;

        public ExpenseAdapter(List<ExpenseItem> expenseItems) {
            this.expenseItems = expenseItems;
        }

        @Override
        public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
            return new ExpenseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ExpenseViewHolder holder, int position) {
            ExpenseItem item = expenseItems.get(position);
            holder.expenseName.setText(item.name);
            holder.expenseAmount.setText(String.format("- $%d", item.amount));
            holder.colorIndicator.setBackgroundColor(item.color);
        }

        @Override
        public int getItemCount() {
            return expenseItems.size();
        }

        static class ExpenseViewHolder extends RecyclerView.ViewHolder {
            public View colorIndicator;
            public TextView expenseName;
            public TextView expenseAmount;

            public ExpenseViewHolder(View itemView) {
                super(itemView);
                colorIndicator = itemView.findViewById(R.id.color_indicator);
                expenseName = itemView.findViewById(R.id.expense_name);
                expenseAmount = itemView.findViewById(R.id.expense_amount);
            }
        }
    }

    static class ExpenseItem {
        String name;
        int amount;
        int color;

        public ExpenseItem(String name, int amount, int color) {
            this.name = name;
            this.amount = amount;
            this.color = color;
        }
    }
}
