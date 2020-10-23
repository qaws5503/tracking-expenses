package com.jimmy.tracking_expenses.StockDataBase;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "account")
public class account {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String accountName;
    private float initialMoney;

    public account(String accountName, float initialMoney) {
        this.accountName = accountName;
        this.initialMoney = initialMoney;
    }
    
    @Ignore
    public account(int id, String accountName, float initialMoney) {
        this.id = id;
        this.accountName = accountName;
        this.initialMoney = initialMoney;
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
}
