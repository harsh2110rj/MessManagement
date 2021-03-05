package com.example.messmanagement;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.security.InvalidParameterException;
import java.util.Calendar;

public class BillsReport extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CursorRecyclerViewAdapter.OnShowDetailClickListener {
    private static final String TAG = "BillsReport";
    public static final int LOADER_ID = 0;
    private CursorRecyclerViewAdapter mAdapter;
    private AlertDialog mDialog = null;
    private static final String SELECTION_PARAM = "SELECTION";
    private static final String SELECTION_ARGS_PARAM = "SELECTION_ARGS";
    Bundle mArgs = new Bundle();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bills_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == R.id.menu_report_showBudget) {
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        String month_string = String.valueOf(month);
                        String year_string = String.valueOf(year);
                        if (month < 10) {
                            month_string = "0" + month_string;
                        }
                        String filter_month = year_string + "-" + month_string;
                        String[] selectionArgs = null;
                        mArgs.putString(SELECTION_PARAM, "date_added LIKE " + "'" + filter_month + "%" + "'");
                        mArgs.putStringArray(SELECTION_ARGS_PARAM, selectionArgs);
//                        CursorRecyclerViewAdapter.budget_this_month=0;
                        getSupportLoaderManager().restartLoader(LOADER_ID, mArgs, BillsReport.this);
                        ContentResolver contentResolver = getContentResolver();
                        String[] projection = {
                                ItemsContract.Columns.ITEMS_STATUS,
                                ItemsContract.Columns.ITEMS_AMOUNT};
                        Cursor cursor = contentResolver.query(ItemsContract.CONTENT_URI,
                                projection,
                                "date_added LIKE " + "'" + filter_month + "%" + "'",
                                null,
                                ItemsContract.Columns.ITEMS_NAME);
                        long budget = 0;
                        if (cursor != null) {
                            Log.d(TAG, "onCreate: number of rows = " + cursor.getCount());
                            while (cursor.moveToNext()) {
                                if (cursor.getString(0).equals(ItemsContract.AMOUNT_DEBITED)) {
                                    budget += Integer.parseInt(cursor.getString(1));
                                } else {
                                    budget -= Integer.parseInt(cursor.getString(1));
                                }
                                Log.d(TAG, "queryHoRahiHai: ===========================" + "Budget is :" + budget);

                            }
                        }
                        cursor.close();


                        TextView tv_filter_detail = (TextView) findViewById(R.id.tv_filter_detail);
                        tv_filter_detail.setText("Total Budget is: Rs. " + String.valueOf(budget));
                    }
                });
                pickerDialog.show(getSupportFragmentManager(), "MonthYearPickerDialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Log.d(TAG, "onCreateLoader: starts with id: " + id);
        String[] projection = {ItemsContract.Columns._ID,
                ItemsContract.Columns.ITEMS_NAME,
                ItemsContract.Columns.ITEMS_QUANTITY,
                ItemsContract.Columns.ITEMS_UNIT,
                ItemsContract.Columns.ITEMS_AMOUNT,
                ItemsContract.Columns.ITEMS_DATE_ADDED,
                ItemsContract.Columns.ITEMS_STATUS
        };
        String selection = null;
        String[] selectionArgs = null;
        if (args != null) {
            selection = args.getString(SELECTION_PARAM);
            selectionArgs = args.getStringArray(SELECTION_ARGS_PARAM);
        }
        String sortOrder = "date(" + ItemsContract.Columns.ITEMS_DATE_ADDED + ")," + ItemsContract.Columns.ITEMS_NAME;
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(this, ItemsContract.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
            default:
                throw new InvalidParameterException(TAG + ".onCreateLoader called with invalid loader id" + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: Entering");
        mAdapter.swapCursor(data);
        int count = mAdapter.getItemCount();


        Log.d(TAG, "onLoadFinished: count is: " + count);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: starts");
        mAdapter.swapCursor(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_report);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Log.d(TAG, "onCreate: starts");
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.items_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CursorRecyclerViewAdapter(null, (CursorRecyclerViewAdapter.OnShowDetailClickListener) BillsReport.this);
        recyclerView.setAdapter(mAdapter);
        Log.d(TAG, "onCreate: returning");
        final TextView tv_filter_detail = (TextView) findViewById(R.id.tv_filter_detail);
        Button btn_filter_detail = (Button) findViewById(R.id.button_filter_apply);
        btn_filter_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(BillsReport.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String day = String.valueOf(dayOfMonth);
                                if (dayOfMonth < 10) {
                                    day = ("0" + day);
                                }
                                monthOfYear += 1;
                                String month = String.valueOf(monthOfYear);
                                if (monthOfYear < 10) {
                                    month = "0" + month;
                                }
                                tv_filter_detail.setText("Showing Entries for: " + day + "/" + month + "/" + year);
                                String filter_date = String.valueOf(year) + "-" + month + "-" + day;
                                String[] selectionArgs = new String[]{filter_date};
                                mArgs.putString(SELECTION_PARAM, "date_added = ?");
                                mArgs.putStringArray(SELECTION_ARGS_PARAM, selectionArgs);
                                getSupportLoaderManager().restartLoader(LOADER_ID, mArgs, BillsReport.this);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }


        });

    }

    @Override
    public void onShowInfoClick(Item item) {


        View messageView = getLayoutInflater().inflate(R.layout.show_bill_detail, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(BillsReport.this);
        builder.setTitle(" Details ");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(messageView);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        TextView label_name = (TextView) messageView.findViewById(R.id.tv_show_details_name);
        TextView label_quantity = (TextView) messageView.findViewById(R.id.tv_show_detail_quantity);
        TextView label_amount = (TextView) messageView.findViewById(R.id.tv_show_detail_amount);
        TextView label_unit = (TextView) messageView.findViewById(R.id.tv_show_detail_unit);
        TextView label_status = (TextView) messageView.findViewById(R.id.tv_show_detail_status);
        TextView label_date = (TextView) messageView.findViewById(R.id.tv_show_detail_date);

        label_name.setText(item.getName());
        label_date.setText(item.getDate());
        label_status.setText(item.getStatus());
        label_amount.setText(item.getAmount());
        label_quantity.setText(item.getQuantity());
        label_unit.setText(item.getUnit());

        mDialog.show();

    }

}


