package com.jimmy.tracking_expenses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jimmy.tracking_expenses.StockDataBase.StockData;
import com.jimmy.tracking_expenses.StockDataBase.DataBase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.round;

public class ViewStockActivity extends AppCompatActivity {
    private SmartTable table;
    Map categoryList = new HashMap();
    float total = 0;
    float stockCost = 0,marketValue = 0,PL = 0,ROI = 0;
    Map<String,Float> percentageList = new LinkedHashMap<>();
    List<String> allCategoryListByDES = new ArrayList<>();
    DecimalFormat df_float = new DecimalFormat("#.#");
    DecimalFormat df_money = new DecimalFormat("#,###.##");
    TextView tv_marketValue,tv_stockCost,tv_PL,tv_ROI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stock);
        Toolbar toolbar = findViewById(R.id.viewStockToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewStockActivity.this, AddStockActivity.class);
                startActivity(intent);
            }
        });

        tv_marketValue = findViewById(R.id.textViewMarketValue);
        tv_stockCost = findViewById(R.id.textViewStockCost);
        tv_PL = findViewById(R.id.textViewPL);
        tv_ROI = findViewById(R.id.textViewROI);
        table = findViewById(R.id.table);
        Stetho.initializeWithDefaults(this);

        new GetStockPrice().execute();
    }

    public class GetStockPrice extends AsyncTask<Void, Integer, List<StockInfo>> {
        private ProgressDialog progressBar;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar = new ProgressDialog(ViewStockActivity.this);
            progressBar.setMessage("Loading...");
            progressBar.setCancelable(false);
            progressBar.show();
        }
        @Override
        protected List<StockInfo> doInBackground(Void... voids) {
            List<StockInfo> list = new ArrayList<>();
            List<StockData> Data = DataBase.getInstance(ViewStockActivity.this).getDataUao().displayAllByOrder();
            if(!Data.isEmpty()){
                getCategoryPercentage();

                StringBuilder quote = new StringBuilder();
                for (StockData nowData : Data) {
                    String nowName = nowData.getName();
                    quote.append(nowName).append(",");
                }
                quote.delete(quote.length()-1,quote.length());

                String url = "https://financialmodelingprep.com/api/v3/quote/" + quote.toString() + "?apikey=8ff27eca2701fd8bcbdfdd364ea9b75f";



                RequestQueue requestQueue = Volley.newRequestQueue(ViewStockActivity.this);

                RequestFuture<JSONArray> future = RequestFuture.newFuture();
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new JSONArray(), future, future);
                requestQueue.add(request);

                    try {
                        JSONArray response = future.get(30, TimeUnit.SECONDS);
                        Map<String,String> stockPriceMap = getStockPrice(response);
                        for (String nowCategory : allCategoryListByDES){
                            List<StockData> categoryStockData = DataBase.getInstance(ViewStockActivity.this).getDataUao().findDataByCategory(nowCategory);
                            for (StockData nowData : categoryStockData) {
                                percentageList.get(nowData.getCategory());
                                float nowPrice = Float.parseFloat(stockPriceMap.get(nowData.getName()));
                                float profit = (float) (round(100*(nowPrice-nowData.getBuyPrice()))/100.0*nowData.getBuyShares());
                                String a = df_money.format(10000);
                                marketValue += nowPrice*nowData.getBuyShares();
                                stockCost += nowData.getTotal();

                                list.add(new StockInfo(nowData.getCategory()+"\n"+df_float.format(percentageList.get(nowData.getCategory()))+" %",nowData.getName()
                                        ,df_money.format(nowData.getBuyShares()),df_money.format(nowData.getBuyPrice()), df_float.format(nowData.getBuyShares() * nowData.getBuyPrice() / total * 100) +" %",
                                        df_money.format(nowPrice), df_money.format(profit), df_money.format(profit / nowData.getTotal() * 100)+" %"));
                            }
                        }
                        PL = marketValue - stockCost;
                        ROI = PL/stockCost*100;


                    } catch (InterruptedException | ExecutionException | TimeoutException e) {
                        // exception handling
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



            }
            return list;
        }
        @Override
        protected void onPostExecute(List<StockInfo> Stock) {
            super.onPostExecute(Stock);
                Column<String> categoryColumn = new Column<>("類別", "category");
                Column<String> nameColumn = new Column<>("股名", "name");
                Column<String> sharesColumn = new Column<>("持有股數", "shares");
                Column<String> priceColumn = new Column<>("購買股價", "price");
                Column<String> percentageColumn = new Column<>("持倉比率", "percentage");
                Column<String> nowPriceColumn = new Column<>("現價", "nowPrice");
                Column<String> PLPriceColumn = new Column<>("$", "PLPrice");
                Column<String> PLPercentageColumn = new Column<>("%", "PLPercentage");
                Column PLColumn = new Column("未實現損益", PLPriceColumn, PLPercentageColumn);
                categoryColumn.setAutoMerge(true);
                nameColumn.setFixed(true);

                TableData tableData = new TableData<>("持股狀況", Stock, categoryColumn, nameColumn, sharesColumn, priceColumn, percentageColumn, nowPriceColumn, PLColumn);
                table.setTableData(tableData);

                TableConfig();


                setTextViewOnTop();
                progressBar.dismiss();

        }
    }

    public Map<String,String> getStockPrice(JSONArray response) throws JSONException {
        Map<String,String> stockPriceMap = new LinkedHashMap<>();
        for (int i=0; i < response.length(); i++){
            JSONObject Stock = response.getJSONObject(i);
            stockPriceMap.put(Stock.getString("symbol"),Stock.getString("price"));
        }
        return stockPriceMap;
    }

    public void TableConfig(){
        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);

        /**
         * setup context color and background
         */
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {

            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                return 0;
            }

            @Override
            public int getTextColor(CellInfo cellInfo) {
                if (cellInfo.col == 6 || cellInfo.col == 7){
                    float value = 0;
                    switch (cellInfo.col){
                        case 6:
                            value = Float.parseFloat(cellInfo.value.replace(",",""));
                            break;
                        case 7:
                            value = Float.parseFloat(cellInfo.value.substring(0,cellInfo.value.length()-2));
                            break;
                    }
                    if(value <0) {
                        return ContextCompat.getColor(ViewStockActivity.this, R.color.colorRed);
                    }
                    else if (value>0){
                        return ContextCompat.getColor(ViewStockActivity.this, R.color.colorGreen);
                    }
                }

                return TableConfig.INVALID_COLOR;
            }
        };
        table.getConfig().setContentCellBackgroundFormat(backgroundFormat);
    }

    public void CheckMarketClose(String day){
        String holiday ="2020-11-26,2020-12-25,2021-01-01,2021-01-18,2021-02-15,2021-04-02,2021-05-31,2021-07-05,2021-09-06,2021-11-25,2021-12-24,2021-01-17,2021-02-21,2021-04-15,2021-05-30,2021-07-04,2021-09-05,2021-11-24,2021-12-26";
    }

    public void getCategoryPercentage(){


        categoryList.clear();
        percentageList.clear();
        total = 0;
        List<String> category = DataBase.getInstance(ViewStockActivity.this).getDataUao().findDistinctCategory();
        for (String now : category){
            float categoryTotal=0;
            List<StockData> nowData = DataBase.getInstance(ViewStockActivity.this).getDataUao().findDataByCategory(now);
            for (int i=0; i<nowData.size(); i++){
                float price = nowData.get(i).getBuyPrice();
                float shares = nowData.get(i).getBuyShares();
                float nowAdd = price*shares;

                total += nowAdd;
                categoryTotal += nowAdd;
            }
            categoryList.put(now,categoryTotal);
        }

        List<Map.Entry> entryList = new ArrayList<>(categoryList.entrySet());

        Comparator< Map.Entry> sortByValue = (e2, e1)->{ return ((Float)e1.getValue()).compareTo( (Float)e2.getValue()); };
        Collections.sort(entryList, sortByValue );

        for(Map.Entry e : entryList) {
            percentageList.put((String) e.getKey(), (Float) e.getValue() / total*100);
            allCategoryListByDES.add((String) e.getKey());
        }

    }

    public void setTextViewOnTop(){
        tv_marketValue.setText(df_money.format(marketValue));
        tv_stockCost.setText(df_money.format(stockCost));
        setTextBackGround(tv_PL, df_money.format(PL), PL);
        setTextBackGround(tv_ROI, df_money.format(ROI)+"%", ROI);

    }

    public void setTextBackGround(TextView textView, String string, float value){
        Spannable spanna = new SpannableString(string);
        if (value>0){
            spanna.setSpan(new BackgroundColorSpan(0xffd50000),0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if(value<0) {
            spanna.setSpan(new BackgroundColorSpan(0xff00c853), 0, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(spanna);
    }
}
