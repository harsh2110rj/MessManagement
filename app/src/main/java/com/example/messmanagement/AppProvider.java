package com.example.messmanagement;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Provider for the MessManagement app. This is only that knows about {@link AppDatabase}
 */
public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.example.messmanagement.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int ITEMS = 100;
    private static final int ITEMS_ID = 101;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(CONTENT_AUTHORITY, ItemsContract.TABLE_NAME, ITEMS);
        matcher.addURI(CONTENT_AUTHORITY, ItemsContract.TABLE_NAME + "/#", ITEMS_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (match) {
            case ITEMS:
                queryBuilder.setTables(ItemsContract.TABLE_NAME);
                break;
            case ITEMS_ID:
                queryBuilder.setTables(ItemsContract.TABLE_NAME);
                long item_id = ItemsContract.getItemId(uri);
                queryBuilder.appendWhere(ItemsContract.Columns._ID + " = " + item_id);
                break;
           default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return ItemsContract.CONTENT_TYPE;
            case ITEMS_ID:
                return ItemsContract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(TAG, " Entering insert: called with uri:" + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        Uri returnUri;
        long recordId;
        switch (match) {
            case ITEMS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(ItemsContract.TABLE_NAME, null, values);
                if (recordId >= 0) {
                    returnUri = ItemsContract.buildItemUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into: " + uri.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Log.d(TAG, "Exiting insert: returning " + returnUri.toString());
        Log.d(TAG, "insert: Item added succesfully");
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, " Entering delete: called with uri:" + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;
        String selectionCriteria;

        switch (match) {
            case ITEMS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(ItemsContract.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEMS_ID:
                db = mOpenHelper.getWritableDatabase();
                long itemId = ItemsContract.getItemId(uri);
                selectionCriteria = ItemsContract.Columns._ID + " = " + itemId;
                if (selection != null) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.delete(ItemsContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Log.d(TAG, "Exiting delete: returning " + count);
        return count;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, " Entering update: called with uri:" + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "match is " + match);

        final SQLiteDatabase db;
        int count;
        String selectionCriteria;

        switch (match) {
            case ITEMS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(ItemsContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ITEMS_ID:
                db = mOpenHelper.getWritableDatabase();
                long itemId = ItemsContract.getItemId(uri);
                selectionCriteria = ItemsContract.Columns._ID + " = " + itemId;
                if (selection != null) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.update(ItemsContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Log.d(TAG, "Exiting update: returning " + count);
        return count;
    }
}
