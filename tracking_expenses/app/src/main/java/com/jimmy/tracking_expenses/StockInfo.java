package com.jimmy.tracking_expenses;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

public class StockInfo {
    public StockInfo(String category, String name, String shares, String price, String percentage, String nowPrice,
                     String PLPrice, String PLPercentage){
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
    private String shares;
    private String price;
    private String percentage;
    String nowPrice;
    String PLPrice;
    String PLPercentage;
}
