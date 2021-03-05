package com.example.messmanagement;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView tv_instructions;
    private boolean mTwoPane = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean checkIfCalledFromAnotherFragment = false;
        switch (item.getItemId()) {
            case R.id.menumain_addItem:
                checkIfCalledFromAnotherFragment = checkAlreadyPresentFragments();
                if (checkIfCalledFromAnotherFragment) {
                    new AlertDialog.Builder(this)
                            .setTitle("Confirm exit")
                            .setMessage("Do you really want to leave?  All entered details will be lost.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    itemAddRequest();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    itemAddRequest();
                }
                break;
            case R.id.menumain_deleteItem:
                checkIfCalledFromAnotherFragment = checkAlreadyPresentFragments();
                if (checkIfCalledFromAnotherFragment) {
                    new AlertDialog.Builder(this)
                            .setTitle("Confirm exit")
                            .setMessage("Do you really want to leave?  All entered details will be lost.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    itemDeleteRequest();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                } else {
                    itemDeleteRequest();
                }
                break;
            case R.id.menumain_showEntries:
                startActivity(new Intent(MainActivity.this, BillsReport.class));
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //        AppDatabase appDatabase=AppDatabase.getInstance(this);
        // final SQLiteDatabase db=appDatabase.getReadableDatabase();
        mTwoPane = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        Log.d(TAG, "onCreate: mTwoPane is " + mTwoPane);
        FragmentManager fragmentManager = getSupportFragmentManager();
        //If AddEditActivityFragment exists then we are editing .SO.......
        Boolean editing = fragmentManager.findFragmentById(R.id.main_layout) != null;
        Log.d(TAG, "onCreate: editing is " + editing);
        //Now we need references to fragments to show and hide them according to the orientation.
        //No need to cast them ,just create two View objects as all methods which we are using are applicable to all fragments in general.
        View mainFragment = findViewById(R.id.tv_instructions);

        if (mTwoPane && editing) {
            Log.d(TAG, "onCreate: Entering in landscape mode");
            //Both are visible
            mainFragment.setVisibility(View.GONE);
        } else if (editing) {
            Log.d(TAG, "onCreate: Single pane And Editing mode is ON");
            //Only addEditLayout is visible
            mainFragment.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "onCreate: Single Pane And Editing Mode is OFF");
            //only mainFragment is visible
            mainFragment.setVisibility(View.VISIBLE);
        }

        String[] projection = {ItemsContract.Columns._ID,
                ItemsContract.Columns.ITEMS_NAME,
                ItemsContract.Columns.ITEMS_QUANTITY,
                ItemsContract.Columns.ITEMS_UNIT,
                ItemsContract.Columns.ITEMS_DATE_ADDED,
                ItemsContract.Columns.ITEMS_STATUS};
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();

//        values.put(ItemsContract.Columns.ITEMS_NAME,"Potato");
//        values.put(ItemsContract.Columns.ITEMS_QUANTITY,200);
//        int count=contentResolver.update(ItemsContract.buildItemUri(1),values,null,null);
//        values.put(ItemsContract.Columns.ITEMS_QUANTITY, 729);
//
//        String selection = ItemsContract.Columns.ITEMS_NAME+ " = ?";
//        String[] args={"Tomato"};
//        int count = contentResolver.update(ItemsContract.CONTENT_URI, values, selection, args);
//        String selection=ItemsContract.Columns.ITEMS_QUANTITY+" =?";
//        String[] args={"529"};
//        int count=contentResolver.delete(ItemsContract.CONTENT_URI,selection,args);

//        Log.d(TAG, "onCreate: " + count + " record(s) deleted");
//        values.put(ItemsContract.Columns.ITEMS_NAME,"Tomato");
//        values.put(ItemsContract.Columns.ITEMS_AMOUNT,10000);
//        values.put(ItemsContract.Columns.ITEMS_QUANTITY,100);
//        values.put(ItemsContract.Columns.ITEMS_UNIT,"Kilograms");
//        values.put(ItemsContract.Columns.ITEMS_DATE_ADDED,20210301);
//        Uri uri=contentResolver.insert(ItemsContract.CONTENT_URI,values);

//        Cursor cursor = contentResolver.query(ItemsContract.CONTENT_URI,
//                projection,
//                null,
//                null,
//                ItemsContract.Columns.ITEMS_NAME);
//
//        if (cursor != null) {
//            Log.d(TAG, "onCreate: number of rows = " + cursor.getCount());
//            while (cursor.moveToNext()) {
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + " : " + cursor.getString(i));
//                }
//                Log.d(TAG, "onCreate: ===========================");
//            }
//            cursor.close();
//        }


        tv_instructions = (TextView) findViewById(R.id.tv_instructions);
        tv_instructions.setMovementMethod(new ScrollingMovementMethod());

    }

    private void itemAddRequest() {

        Log.d(TAG, "itemAddRequest: starts");
        removeAlreadyPresentFragments();
        AddItemFragment fragment = new AddItemFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, fragment, "AddItemFragment");
        fragmentTransaction.commit();
        View mainFragment = findViewById(R.id.tv_instructions);
        mainFragment.setVisibility(View.GONE);


        Log.d(TAG, "itemAddRequest: Exiting now ....");
    }

    private void itemDeleteRequest() {
        Log.d(TAG, "itemDeleteRequest: starts");
        removeAlreadyPresentFragments();
        DeleteItemFragment fragment = new DeleteItemFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, fragment, "DeleteItemFragment");
        fragmentTransaction.commit();
        View mainFragment = findViewById(R.id.tv_instructions);
        mainFragment.setVisibility(View.GONE);
        Log.d(TAG, "itemDeleteRequest: Exiting now ....");
    }

    private boolean checkAlreadyPresentFragments() {
        Log.d(TAG, "removeAlreadyPresentFragments: called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment alreadyPresentAddItemFragment = fragmentManager.findFragmentByTag("AddItemFragment");
        Fragment alreadyPresentDeleteItemFragment = fragmentManager.findFragmentByTag("DeleteItemFragment");
        if (alreadyPresentAddItemFragment != null) {
//            getSupportFragmentManager().beginTransaction().remove(alreadyPresentAddItemFragment).commit();
            Log.d(TAG, "removeAlreadyPresentFragments: Add Item was already present!");
            return true;
        }
        if (alreadyPresentDeleteItemFragment != null) {
//            getSupportFragmentManager().beginTransaction().remove(alreadyPresentDeleteItemFragment).commit();
            Log.d(TAG, "removeAlreadyPresentFragments: Delete Item was already present!");
            return true;

        }
        return false;

    }

    private void removeAlreadyPresentFragments() {
        Log.d(TAG, "removeAlreadyPresentFragments: called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment alreadyPresentAddItemFragment = fragmentManager.findFragmentByTag("AddItemFragment");
        Fragment alreadyPresentDeleteItemFragment = fragmentManager.findFragmentByTag("DeleteItemFragment");
        if (alreadyPresentAddItemFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(alreadyPresentAddItemFragment).commit();
            Log.d(TAG, "removeAlreadyPresentFragments: Add Item was already present!");
        }
        if (alreadyPresentDeleteItemFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(alreadyPresentDeleteItemFragment).commit();
            Log.d(TAG, "removeAlreadyPresentFragments: Delete Item was already present!");
        }

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        final AddItemFragment fragment1 = (AddItemFragment) fragmentManager.findFragmentByTag("AddItemFragment");
        final DeleteItemFragment fragment2 = (DeleteItemFragment) fragmentManager.findFragmentByTag("DeleteItemFragment");
        if (fragment1 == null && fragment2 == null) {
            Log.d(TAG, "onBackPressed: both are null");
            this.finishAffinity(); //waah taj!
        } else {//show dialogue to get confirmation to quit editing
            new AlertDialog.Builder(this)
                    .setTitle("Confirm exit")
                    .setMessage("Do you really want to leave?  All unsaved changes will be lost.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (fragment1 != null) {
                                getSupportFragmentManager().beginTransaction().remove(fragment1).commit();
                            }
                            if (fragment2 != null) {
                                getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
                            }
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
}