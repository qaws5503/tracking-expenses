package com.jimmy.tracking_expenses;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

public class StockInfo {
    public StockInfo(String category, String name, float shares, float price, String percentage, float nowPrice,
                     float PLPrice, String PLPercentage){
        this.category = category;
        this.name = name;
        this.shares = shares;
        this.price = price;
        this.percentage = percentage;
        this.nowPrice = nowPrice;
        this.PLPrice = PLPrice;
        this.PLPercentage = PLPercentage;
    }


    private String category;
    private String name;
    private float shares;
    private float price;
    private String percentage;
    float nowPrice;
    float PLPrice;
    String PLPercentage;
}
