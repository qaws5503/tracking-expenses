package com.jimmy.tracking_expenses.StockDataBase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expenses {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    private float amount;
    private String remarks;
}
