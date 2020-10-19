package com.jimmy.tracking_expenses.StockDataBase;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "USStockTable")
public class StockData {


    @PrimaryKey(autoGenerate = true)//設置是否使ID自動累加
    private int id;
    private String category;
    private String name;
    private float buyShares;
    private float buyPrice;
    private float total;
    private String account;

    public StockData(String category, String name, float buyShares, float buyPrice, float total, String account) {
        this.category = category;
        this.name = name;
        this.buyShares = buyShares;
        this.buyPrice = buyPrice;
        this.total = total;
        this.account = account;
    }
    @Ignore//如果要使用多形的建構子，必須加入@Ignore
    public StockData(int id, String category, String name, float buyShares, float buyPrice, float total, String account) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.buyShares = buyShares;
        this.buyPrice = buyPrice;
        this.total = total;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBuyShares() {
        return buyShares;
    }

    public void setBuyShares(float buyShares) {
        this.buyShares = buyShares;
    }

    public float getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public float getTotal(){ return total; }

    public void setTotal(float total) { this.total = total; }
}
