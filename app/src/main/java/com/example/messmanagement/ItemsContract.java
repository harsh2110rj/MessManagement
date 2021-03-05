package com.example.messmanagement;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.messmanagement.AppProvider.CONTENT_AUTHORITY;
import static com.example.messmanagement.AppProvider.CONTENT_AUTHORITY_URI;

public class ItemsContract {
    static final String TABLE_NAME = "Items";
    static final String AMOUNT_CREDITED="Credited";
    static final String AMOUNT_DEBITED=" Debited";
    // Items Table Fields

    public static class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String ITEMS_NAME = "name";
        public static final String ITEMS_AMOUNT = "amount";
        public static final String ITEMS_QUANTITY = "quantity";
        public static final String ITEMS_UNIT = "unit";
        public static final String ITEMS_DATE_ADDED = "date_added";
        public static final String ITEMS_STATUS ="status";

        private Columns() {
            //singleton class
        }
    }

    /**
     * The URI to access the Items Table
     */
    public static final Uri CONTENT_URI = Uri.withAppendedPath(CONTENT_AUTHORITY_URI, TABLE_NAME);

    static final String CONTENT_TYPE ="vnd.android.cursor.dir/vnd."+CONTENT_AUTHORITY+"."+TABLE_NAME;
    static final String CONTENT_ITEM_TYPE="vnd.android.cursor.item/vnd"+CONTENT_AUTHORITY+"."+TABLE_NAME;

    static Uri buildItemUri(long itemId) {
        return ContentUris.withAppendedId(CONTENT_URI, itemId);
    }

    static long getItemId(Uri uri) {
        return ContentUris.parseId(uri);
    }
}
