package com.example.messmanagement;

import androidx.annotation.NonNull;

import java.io.Serializable;

class Item implements Serializable {
    public static final long serialVersionUID = 20161120L;
    private long m_Id;
    private final String mName;
    private final String mQuantity;
    private final String mUnit;
    private final String mAmount;
    private final String mDate;
    private final String mStatus;

    public Item(long id, String name, String quantity, String unit, String amount, String date, String status) {
        this.m_Id = id;
        mName = name;
        mQuantity = quantity;
        mUnit = unit;
        mAmount = amount;
        mDate = date;
        mStatus = status;
    }

    public void setM_Id(long id) {
        this.m_Id = id;
    }

    public long getM_Id() {
        return m_Id;
    }

    public String getName() {
        return mName;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public String getUnit() {
        return mUnit;
    }

    public String getAmount() {
        return mAmount;
    }

    public String getDate() {
        return mDate;
    }

    public String getStatus() {
        return mStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item{" +
                "m_Id=" + m_Id +
                ", mName='" + mName + '\'' +
                ", mQuantity='" + mQuantity + '\'' +
                ", mUnit=" + mUnit + '\'' +
                ", mAmount='" + mAmount + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mStatus=" + mStatus +
                '}';
    }
}
