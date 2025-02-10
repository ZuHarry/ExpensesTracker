package com.example.expensestracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import com.example.expensestracker.adapter.ExpenseItem;
import com.example.expensestracker.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class DatabaseExpense extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final String TABLE_EXPENSES = "expenses";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DATE = "date";

    public DatabaseExpense(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_AMOUNT + " REAL,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_DATE + " TEXT,"
                + "type TEXT" + ")";
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // Add new expense
    public void addExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DATE, expense.getDate());

        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    // Get single expense by ID
    public Expense getExpense(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES, new String[]{KEY_ID,
                        KEY_DESCRIPTION, KEY_AMOUNT, KEY_CATEGORY, KEY_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Expense expense = new Expense(
                Integer.parseInt(cursor.getString(0)), // ID
                cursor.getString(1), // Description
                Double.parseDouble(cursor.getString(2)), // Amount
                cursor.getString(3), // Category
                cursor.getString(4) // Date
        );
        cursor.close();
        return expense;
    }

    // Get all expenses
    public List<Expense> getExpensesByCategory(String category) {
        List<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + KEY_CATEGORY + " = ?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(Integer.parseInt(cursor.getString(0)));
                expense.setDescription(cursor.getString(1));
                expense.setAmount(Double.parseDouble(cursor.getString(2)));
                expense.setCategory(cursor.getString(3));
                expense.setDate(cursor.getString(4));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }

    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(Integer.parseInt(cursor.getString(0)));
                expense.setDescription(cursor.getString(1));
                expense.setAmount(Double.parseDouble(cursor.getString(2)));
                expense.setCategory(cursor.getString(3));
                expense.setDate(cursor.getString(4));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }

    public List<Expense> getExpensesByMonth(String month, String year) {
        List<Expense> expenseList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EXPENSES +
                " WHERE strftime('%m', " + KEY_DATE + ") = ? AND strftime('%Y', " + KEY_DATE + ") = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.format("%02d", Integer.parseInt(month)), year});

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getInt(0));
                expense.setDescription(cursor.getString(1));
                expense.setAmount(cursor.getDouble(2));
                expense.setCategory(cursor.getString(3));
                expense.setDate(cursor.getString(4));
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseList;
    }


    public List<ExpenseItem> getIncomeListByMonth(int month, int year) {
        List<ExpenseItem> incomeList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT category, amount FROM transactions WHERE type='income' AND month=? AND year=?",
                new String[]{String.valueOf(month), String.valueOf(year)});

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(0);
                int amount = cursor.getInt(1);
                incomeList.add(new ExpenseItem(category, amount, Color.GREEN)); // Warna boleh ubah ikut kategori
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return incomeList;
    }


    public double getTotalExpenseByMonth(String month, String year) {
        double total = 0;
        String query = "SELECT SUM(amount) FROM " + TABLE_EXPENSES +
                " WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{month, year});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    public List<ExpenseItem> getIncomeListByMonth(String month, String year) {
        List<ExpenseItem> incomeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Pastikan bulan dalam format 2 digit
        String formattedMonth = String.format("%02d", Integer.parseInt(month));

        // Ambil hanya kategori income
        String query = "SELECT category, amount FROM " + TABLE_EXPENSES +
                " WHERE strftime('%m', date) = ? AND strftime('%Y', date) = ? AND category = 'Income'";

        Cursor cursor = db.rawQuery(query, new String[]{formattedMonth, year});

        while (cursor.moveToNext()) {
            String category = cursor.getString(0);
            int amount = cursor.getInt(1);
            incomeList.add(new ExpenseItem(category, amount, Color.GREEN)); // Set warna hijau untuk income
        }
        cursor.close();
        return incomeList;
    }



    // Update an expense
    public int updateExpense(Expense expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DESCRIPTION, expense.getDescription());
        values.put(KEY_AMOUNT, expense.getAmount());
        values.put(KEY_CATEGORY, expense.getCategory());
        values.put(KEY_DATE, expense.getDate());

        return db.update(TABLE_EXPENSES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
    }

    // Delete an expense
    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

}