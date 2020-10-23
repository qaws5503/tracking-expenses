package com.jimmy.tracking_expenses.StockDataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {StockData.class,category.class,account.class},version = 1,exportSchema = true)
public abstract class DataBase extends RoomDatabase {
    public static final String DB_NAME = "RecordData.db";
    private static volatile DataBase instance;

    public static synchronized DataBase getInstance(Context context){
        if(instance == null){
            instance = create(context);//創立新的資料庫
        }
        return instance;
    }

    private static DataBase create(final Context context){
        String[] stockCategory = {"科技","金融","傳統"};
        String[] account = {"現金","銀行","股票"};
        return Room.databaseBuilder(context, DataBase.class,DB_NAME)
                // prepopulate the database after onCreate was called
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        new Thread(()->{
                            for (String text :stockCategory) {
                                getInstance(context).getDataUao().insertCategory(text, "stock");
                            }
                            for (String text :account) {
                                getInstance(context).getDataUao().insertAccount(text, (float) 0);
                            }
                        }).start();
                    }
                }).build();
    }



    public abstract DataUao getDataUao();//設置對外接口
}

