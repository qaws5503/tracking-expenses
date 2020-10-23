package com.jimmy.tracking_expenses.StockDataBase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String categoryName;
    private String classification;

    public Category(String categoryName, String classification) {
        this.categoryName = categoryName;
        this.classification = classification;
    }

    @Ignore
    public Category(int id, String categoryName, String classification) {
        this.id = id;
        this.categoryName = categoryName;
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

}
