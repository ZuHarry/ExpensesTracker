package com.example.expensestracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expensestracker.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class DatabaseExpense extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ExpenseTrackerDB";
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
                + KEY_DATE + " TEXT" + ")";
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