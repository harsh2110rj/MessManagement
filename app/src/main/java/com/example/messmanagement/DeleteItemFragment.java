package com.example.messmanagement;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;

public class DeleteItemFragment extends Fragment {
    Context context;
    private static final String TAG = "DeleteItemFragment";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_item, container, false);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_unit);
        TextView tv_unit = (TextView) view.findViewById(R.id.tv_unit);
        final EditText et_name = (EditText) view.findViewById(R.id.et_name);
        final EditText et_quantity = (EditText) view.findViewById(R.id.et_quantity);
        final EditText et_amount = (EditText) view.findViewById(R.id.et_amount);
        final EditText et_date_deleted = (EditText) view.findViewById(R.id.et_date_deleted);
        Button btn_delete = (Button) view.findViewById(R.id.button_delete);

        final String[] spinner_unit = new String[1];
        spinner = workingSpinner(view, spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner_unit[0] = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                spinner_unit[0] = "Kilogram(s)";

            }
        });
        et_date_deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                String day=String.valueOf(dayOfMonth);
                                if(dayOfMonth<10)
                                {
                                    day=("0"+day);
                                }
                                monthOfYear+=1;
                                String month=String.valueOf(monthOfYear);
                                if(monthOfYear<10)
                                {
                                    month="0"+month;
                                }
                                et_date_deleted.setText(year + "-"
                                        + month + "-" + day);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        final String unit = spinner_unit[0];

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog mAlertDialog = new AlertDialog.Builder(context)
                        .setTitle("Delete Item")
                        .setMessage("Do you really want to DELETE the item? Please double check the details.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    final String name_of_item = et_name.getText().toString().trim();
                                    final int quantity = Integer.parseInt(et_quantity.getText().toString());
                                    final int amount = Integer.parseInt(et_amount.getText().toString());
                                    final String date = et_date_deleted.getText().toString();
                                    final String unit = spinner_unit[0];
                                    final String status = ItemsContract.AMOUNT_CREDITED;
                                    if (name_of_item.equals("") || date.equals("")) {
                                        throw new Exception();
                                    }
                                    ContentResolver contentResolver = context.getContentResolver();
                                    ContentValues values = new ContentValues();
                                    values.put(ItemsContract.Columns.ITEMS_NAME, name_of_item);
                                    values.put(ItemsContract.Columns.ITEMS_AMOUNT, amount);
                                    values.put(ItemsContract.Columns.ITEMS_QUANTITY, quantity);
                                    values.put(ItemsContract.Columns.ITEMS_UNIT, unit);
                                    values.put(ItemsContract.Columns.ITEMS_DATE_ADDED, date);
                                    values.put(ItemsContract.Columns.ITEMS_STATUS, status);
                                    Uri uri = contentResolver.insert(ItemsContract.CONTENT_URI, values);
                                    Log.d(TAG, "onClick: Inserted(deleted) successfully with status: " + status);
                                    Toast.makeText(context, "The item has been deleted sucessfully and amount has been credited.", Toast.LENGTH_LONG).show();
                                    et_name.setText("");
                                    et_amount.setText("");
                                    et_date_deleted.setText("");
                                    et_quantity.setText("");
                                    FragmentManager fragmentManager = getFragmentManager();
                                    if (fragmentManager != null) {
                                        DeleteItemFragment deleteItemFragment = (DeleteItemFragment) fragmentManager.findFragmentByTag("DeleteItemFragment");
                                        FragmentTransaction ft = fragmentManager.beginTransaction();
                                        if (deleteItemFragment != null) {
                                            ft.remove(deleteItemFragment);
                                        }
                                        ft.commit();
                                    }
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(context, "No field should remain empty. Every field is mandatory.", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).setNegativeButton(android.R.string.no, null).show();


            }
        });
        return view;

    }

    private Spinner workingSpinner(View view, Spinner spinner) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Kilogram(s)");
        arrayList.add("Litre(s)");
        arrayList.add("Gram(s)");
        arrayList.add("Packet(s)");

        context = view.getContext();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        return spinner;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}