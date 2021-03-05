package com.example.messmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Basic database class for the application
 * It is a singleton class only used by {@link AppProvider} class
 */
public class AppDatabase extends SQLiteOpenHelper {
    private static final String TAG = "AppDatabase";
    public static final String DATABASE_NAME = "MessManagement.db";
    public static final int DATABASE_VERSION = 1;
    //instance of app database
    private static AppDatabase instance = null;

    private AppDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "AppDatabase: constructor");
    }

    /**
     * Get instance of app's database helper object
     *
     * @param context context of content provider
     * @return a SQLite database helper project
     */
    static AppDatabase getInstance(Context context) {
        if (instance == null) {
            Log.d(TAG, "getInstance: create new instance");
            instance = new AppDatabase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: starts");
        String sSQL;
        sSQL = "CREATE TABLE " + ItemsContract.TABLE_NAME + " ("
                + ItemsContract.Columns._ID + " INTEGER PRIMARY KEY NOT NULL, "
                + ItemsContract.Columns.ITEMS_NAME + " TEXT NOT NULL, "
                + ItemsContract.Columns.ITEMS_AMOUNT + " INTEGER NOT NULL, "
                + ItemsContract.Columns.ITEMS_QUANTITY + " INTEGER NOT NULL, "
                + ItemsContract.Columns.ITEMS_UNIT + " TEXT NOT NULL, "
                + ItemsContract.Columns.ITEMS_DATE_ADDED + " TEXT NOT NULL, "
                + ItemsContract.Columns.ITEMS_STATUS + " TEXT NOT NULL);";

        Log.d(TAG, sSQL);
        db.execSQL(sSQL);
        Log.d(TAG, "onCreate: ITEMS Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: starts");
        switch (oldVersion) {
            case 1:
                break;
            default:
                throw new IllegalStateException("onUpgrade() with unknown new version" + newVersion);
        }
    }

}
