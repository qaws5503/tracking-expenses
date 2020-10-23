package com.jimmy.tracking_expenses.StockDataBase;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DataUao {

    String tableName = "USStockTable";
    /**=======================================================================================*/
    /**簡易新增所有資料的方法*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)//預設萬一執行出錯怎麼辦，REPLACE為覆蓋
    void insertData(StockData myStockData);

    /**複雜(?)新增所有資料的方法*/
    @Query("INSERT INTO "+tableName+"(category, name, buyShares, buyPrice, total, account) VALUES(:category, :name, :buyShares, :buyPrice, :total, :account)")
    void insertData(String category, String name, float buyShares, float buyPrice, float total, String account);

    /**=======================================================================================*/
    /**撈取全部資料*/
    @Query("SELECT * FROM " + tableName)
    List<StockData> displayAll();

    @Query("SELECT * FROM " + tableName +" ORDER BY LOWER(name) ASC")
    List<StockData> displayAllByOrder();

    /**撈取某個類別的相關資料*/
    @Query("SELECT * FROM " + tableName +" WHERE category = :category ORDER BY total DESC, LOWER(name) ASC")
    List<StockData> findDataByCategory(String category);

    @Query("SELECT * FROM " + tableName +" WHERE id = :id")
    StockData findDataById(int id);


    @Query("SELECT * FROM " + tableName +" WHERE name = :name")
    StockData findStockDataByName(String name);

    @Query("SELECT DISTINCT category FROM " + tableName)
    List<String> findDistinctCategory();

    @Query("SELECT name FROM " + tableName)
    List<String> displayAllStockName();

    /**=======================================================================================*/
    /**簡易更新資料的方法*/
    @Update
    void updateData(StockData myStockData);

    /**複雜(?)更新資料的方法*/
    @Query("UPDATE "+tableName+" SET category = :category,name=:name,buyShares=:buyShares,buyPrice=:buyPrice,total=:total,account=:account WHERE id = :id" )
    void updateData(int id, String category, String name, float buyShares, float buyPrice,float total, String account);

    /**=======================================================================================*/
    /**簡單刪除資料的方法*/
    @Delete
    void deleteData(StockData myStockData);

    /**複雜(?)刪除資料的方法*/
    @Query("DELETE  FROM " + tableName + " WHERE id = :id")
    void deleteData(int id);

    @Query("DELETE FROM "+ tableName)
    void delete();

    /***/
    @Query("INSERT INTO Category(categoryName, classification) VALUES(:categoryName, :classification)")
    void insertCategory(String categoryName, String classification);

    @Query("UPDATE Category SET categoryName = :categoryName,classification = :classification WHERE id = :id")
    void updateCategory(int id, String categoryName, String classification);

    @Query("SELECT * FROM Category WHERE classification = :classification")
    List<Category> getClassificationCategory(String classification);

    @Query("INSERT INTO Account(accountName, initialMoney, classification) VALUES(:accountName, :initialMoney, :classification)")
    void insertAccount(String accountName, float initialMoney, String classification);

    @Query("UPDATE Account SET accountName = :accountName,initialMoney = :initialMoney,classification = :classification WHERE id = :id")
    void updateAccount(int id, String accountName, float initialMoney, String classification);

    @Query("SELECT * FROM Account WHERE classification = :classification")
    List<Account> getClassificationAccount(String classification);

}

