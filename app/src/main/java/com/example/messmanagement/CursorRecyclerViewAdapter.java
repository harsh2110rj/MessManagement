package com.example.messmanagement;

import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.ItemViewHolder> {
    public static long budget_this_month = 0;
    private static final String TAG = "CursorRecyclerViewAdapt";
    private Cursor mCursor;
    private AlertDialog mDialog = null;
    private OnShowDetailClickListener mListener;


    interface OnShowDetailClickListener {
        void onShowInfoClick(Item item);
    }

    public CursorRecyclerViewAdapter(Cursor cursor, OnShowDetailClickListener listener) {
        Log.d(TAG, "CursorRecyclerViewAdapter: constructor called");
        mCursor = cursor;
        mListener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int i) {
        Log.d(TAG, "onBindViewHolder: starts");
        if ((mCursor == null) || mCursor.getCount() == 0) {
            holder.name.setText("Please add some items into the inventory.");

        } else {
            if (!mCursor.moveToPosition(i)) {
                throw new IllegalStateException("OnViewBindHolder has problem");
            }

            final Item item = new Item(mCursor.getLong(mCursor.getColumnIndex(ItemsContract.Columns._ID)),
                    mCursor.getString(mCursor.getColumnIndex(ItemsContract.Columns.ITEMS_NAME)),
                    mCursor.getString(mCursor.getColumnIndex(ItemsContract.Columns.ITEMS_QUANTITY)),
                    mCursor.getString(mCursor.getColumnIndex(ItemsContract.Columns.ITEMS_UNIT)),
                    mCursor.getString(mCursor.getColumnIndex(ItemsContract.Columns.ITEMS_AMOUNT)),
                    mCursor.getString(mCursor.getColumnIndex(ItemsContract.Columns.ITEMS_DATE_ADDED)),
                    mCursor.getString(mCursor.getColumnIndex(ItemsContract.Columns.ITEMS_STATUS))
            );

            holder.name.setText(item.getName());
            holder.date.setText(item.getDate());
            holder.status.setText(item.getStatus());


            View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onShowInfoClick(item);
                    }
                    Log.d(TAG, "onClick: starts");
                    Log.d(TAG, "onClick: button with id " + view.getId() + "  clicked");
                    Log.d(TAG, "onClick: item Name is " + item.getName());

                }
            };
            holder.showInfo.setOnClickListener(buttonListener);


        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.
     * The returned old cursor is `not` closed.
     *
     * @param newCursor
     * @return the previously set cursor or null if there was'nt one.
     */
    Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        mCursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        return oldCursor;

    }

    @Override
    public int getItemCount() {

        Log.d(TAG, "getItemCount: starts");
        if ((mCursor == null) || (mCursor.getCount() == 0)) {
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ItemViewHolder";
        TextView name = null;
        TextView date = null;
        TextView status = null;
        ImageButton showInfo = null;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.tv_item_name);
            this.date = (TextView) itemView.findViewById(R.id.tv_item_date);
            this.status = (TextView) itemView.findViewById(R.id.tv_item_status);
            this.showInfo = (ImageButton) itemView.findViewById(R.id.imageButton_item_details);


        }
    }
    void printCursorElements(Cursor cursor)
    {
//        Cursor cursor=mCursor;
        if (cursor != null) {
            Log.d(TAG, "onCreate: number of rows = " + cursor.getCount());
            while (cursor.moveToNext()) {
                for (int j = 0; j < cursor.getColumnCount(); j++) {
                    Log.d(TAG, "onCreate: " + cursor.getColumnName(j) + " : " + cursor.getString(j));
                }
                Log.d(TAG, "Madarchod choot: ***************===========================**********");
            }
            cursor.close();
        }

    }
}
