package com.example.expensestracker.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ExpenseTrackerDB";
    private static final String TABLE_NAME = "user";
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_FULLNAME = "fullname";
    private Context context;  // Add this line

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;  // Store context to use it for SharedPreferences
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUser = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_FULLNAME + " TEXT)";
        db.execSQL(createTableUser);

        // Tambah pengguna default untuk testing
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULLNAME, "Test User");
        contentValues.put(COL_USERNAME, "testuser");
        contentValues.put(COL_EMAIL, "test@example.com");
        contentValues.put(COL_PASSWORD, "password123");
        db.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert New User
    public boolean insertUserData(String fullname, String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULLNAME, fullname);
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Add this method to the DatabaseHelper class
    public boolean updateUserProfile(int userId, String fullName, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Use ContentValues to update the fields
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULLNAME, fullName);
        contentValues.put(COL_USERNAME, name);  // Add the "Name" field
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);

        // Update the row where the ID matches the userId
        int rowsAffected = db.update(TABLE_NAME, contentValues, COL_ID + " = ?", new String[]{String.valueOf(userId)});

        // Return whether the update was successful (rowsAffected > 0 means success)
        return rowsAffected > 0;
    }


    // Get User for Login
    public Cursor getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int userId = cursor.getInt(cursor.getColumnIndex(COL_ID));
            SharedPreferences sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("user_id", userId); // Store the user ID
            editor.apply();
            cursor.close();
        }

        return cursor;
    }

    @SuppressLint("Range")
    public List<String> getProfile() {
        List<String> profileData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get the logged-in user's ID from SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", -1); // Default to -1 if no user_id is saved

        if (userId != -1) {
            // Query to select user data based on the logged-in user's ID
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID + " = ?", new String[]{String.valueOf(userId)});

            if (cursor != null && cursor.moveToFirst()) {
                // Add data to the list based on columns
                profileData.add(cursor.getString(cursor.getColumnIndex(COL_USERNAME)));  // name
                profileData.add(cursor.getString(cursor.getColumnIndex(COL_EMAIL)));     // email
                profileData.add(cursor.getString(cursor.getColumnIndex(COL_FULLNAME)));  // full name
                // Add any other columns you need here

                cursor.close();
            }
        }

        return profileData;
    }
}
