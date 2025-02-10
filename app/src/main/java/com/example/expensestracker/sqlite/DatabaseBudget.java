package com.example.expensestracker.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.expensestracker.model.Budget;

public class DatabaseBudget extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseTracker.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_BUDGET = "budget";

    // Column names
    private static final String KEY_ID = "id";
    private static final String KEY_BUDGET_TARGET = "budget_target";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_USER_ID = "user_id";

    public DatabaseBudget(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_BUDGET_TARGET + " REAL," // Use REAL for double
                + KEY_START_DATE + " TEXT,"    // Use TEXT for dates (ISO format)
                + KEY_END_DATE + " TEXT,"      // Use TEXT for dates (ISO format)
                + KEY_USER_ID + " INTEGER" + ")"; //Use INTEGER
        db.execSQL(CREATE_BUDGET_TABLE);
        Log.d("DatabaseBudget", "Table created: " + CREATE_BUDGET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }

    public void addBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BUDGET_TARGET, budget.getBudget_target());
        values.put(KEY_START_DATE, budget.getStart_date());
        values.put(KEY_END_DATE, budget.getEnd_date());
        values.put(KEY_USER_ID, budget.getUser_id());

        db.insert(TABLE_BUDGET, null, values);
        db.close();
        Log.d("DatabaseBudget", "Budget added: " + budget.getBudget_target());
    }

    // Get single budget by ID
    public Budget getBudget(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BUDGET, new String[]{KEY_ID,
                        KEY_BUDGET_TARGET, KEY_START_DATE, KEY_END_DATE, KEY_USER_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Budget budget = null; // Initialize to null in case no budget is found

        if (cursor != null && cursor.moveToFirst()) {
            budget = new Budget(
                    cursor.getInt(0),  // ID
                    cursor.getDouble(1),  // Budget_Target
                    cursor.getString(2),  // Start_Date
                    cursor.getString(3),  // End_Date
                    cursor.getInt(4)   // User_ID
            );
            cursor.close();
        }
        db.close();
        return budget;
    }

    public int updateBudget(Budget budget) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BUDGET_TARGET, budget.getBudget_target());
        values.put(KEY_START_DATE, budget.getStart_date());
        values.put(KEY_END_DATE, budget.getEnd_date());
        values.put(KEY_USER_ID, budget.getUser_id());

        // updating row
        return db.update(TABLE_BUDGET, values, KEY_ID + " = ?",
                new String[] { String.valueOf(budget.getId()) });
    }
}