package com.jimmy.tracking_expenses.StockDataBase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "accountCategory")
public class accountCategory {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public accountCategory(String category) {
        this.category = category;
    }

    @Ignore
    public accountCategory(int id, String category) {
        this.id = id;
        this.category = category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    private String category;
}
