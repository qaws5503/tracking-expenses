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
    @Query("SELECT * FROM " + tableName +" WHERE category = :category ORDER BY total ASC, LOWER(name) ASC")
    List<StockData> findDataByCategory(String category);

    @Query("SELECT * FROM " + tableName +" WHERE id = :id")
    List<StockData> findDataById(int id);

    @Query("SELECT DISTINCT category FROM " + tableName)
    List<String> findDistinctCategory();

    @Query("SELECT name FROM " + tableName)
    String[] displayAllName();

    /**=======================================================================================*/
    /**簡易更新資料的方法*/
    @Update
    void updateData(StockData myStockData);

    /**複雜(?)更新資料的方法*/
    @Query("UPDATE "+tableName+" SET category = :category,name=:name,buyShares=:buyShares,buyPrice=:buyPrice,account=:account WHERE id = :id" )
    void updateData(int id, String category, String name, float buyShares, float buyPrice, String account);

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
    @Query("INSERT INTO accountCategory(category) VALUES(:category)")
    void insertCategory(String category);

}

