package com.jimmy.tracking_expenses.StockDataBase;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "account")
public class Account {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String accountName;
    private float initialMoney;
    private String classification;

    public Account(String accountName, float initialMoney, String classification) {
        this.accountName = accountName;
        this.initialMoney = initialMoney;
        this.classification = classification;
    }
    
    @Ignore
    public Account(int id, String accountName, float initialMoney, String classification) {
        this.id = id;
        this.accountName = accountName;
        this.initialMoney = initialMoney;
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int i) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public float getInitialMoney() {
        return initialMoney;
    }

    public void setInitialMoney(float initialMoney) {
        this.initialMoney = initialMoney;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
