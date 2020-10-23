package com.jimmy.tracking_expenses;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jakewharton.rxbinding4.widget.RxSearchView;
import com.jimmy.tracking_expenses.StockDataBase.Category;
import com.jimmy.tracking_expenses.StockDataBase.DataBase;
import com.jimmy.tracking_expenses.StockDataBase.StockData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyStockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyStockFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Runnable runnable;
    Handler handler = new Handler();
    TextView tv_stockSymbol;
    EditText ed_stockPrice,ed_stockShares;
    List<String> allStockName = new ArrayList<>();
    Map<String,String> map_StockToCategory = new LinkedHashMap<>();
    ArrayList stockCategoryList = new ArrayList<String>();

    public BuyStockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyStockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyStockFragment newInstance(String param1, String param2) {
        BuyStockFragment fragment = new BuyStockFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        new Thread(() -> {
            List<StockData> AllData = DataBase.getInstance(getActivity()).getDataUao().displayAll();
            for (StockData now:AllData){
                allStockName.add(now.getName());
                map_StockToCategory.put(now.getName(),now.getCategory());
            }
            List<Category> category = DataBase.getInstance(getActivity()).getDataUao().getClassificationCategory("stock");
            stockCategoryList.add("請選擇股票類別");
            for (Category now:category){
                stockCategoryList.add(now.getCategoryName());
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_buy_stock, container, false);
        tv_stockSymbol = v.findViewById(R.id.textViewStockSymbol);
        Button bt_cancel = v.findViewById(R.id.buttonCancel);
        Button bt_add = v.findViewById(R.id.buttonAdd);
        ed_stockPrice = v.findViewById(R.id.editTextStockPrice);
        ed_stockShares = v.findViewById(R.id.editTextStockShares);

        Spinner spinner = v.findViewById(R.id.spinner);



        ArrayAdapter adapter = new  ArrayAdapter(getContext()
                ,android.R.layout.simple_dropdown_item_1line,stockCategoryList);
        spinner.setAdapter(adapter);



        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_stockPrice.getText().toString())){
                    ed_stockPrice.setError("請填入股票價格");
                }
                else if (TextUtils.isEmpty(ed_stockShares.getText().toString())){
                    ed_stockShares.setError("請填入股票數");
                }
                else {
                    String category = spinner.getSelectedItem().toString();
                    String name = tv_stockSymbol.getText().toString();
                    String account = "";
                    float buyPrice = Float.parseFloat(ed_stockPrice.getText().toString());
                    float buyShares = Float.parseFloat(ed_stockShares.getText().toString());
                    float total = buyPrice * buyShares;
                    if (category.equals("請選擇股票類別")) {
                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(getContext());
                        builder.setTitle("錯誤");
                        builder.setMessage("請選擇股票類別");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        new Thread(() -> {
                            if (allStockName.contains(name)) {
                                StockData oldData = DataBase.getInstance(getActivity()).getDataUao().findStockDataByName(name);
                                float oldShares = oldData.getBuyShares();
                                float oldTotal = oldData.getTotal();
                                float allShares = oldShares + buyShares;
                                float newTotal = oldTotal + total;
                                float newPrice = (newTotal) / allShares;
                                DataBase.getInstance(getActivity()).getDataUao().updateData(
                                        oldData.getId(), oldData.getCategory(), oldData.getName(), allShares, newPrice, newTotal, oldData.getAccount());
                            } else {
                                DataBase.getInstance(getActivity()).getDataUao().insertData(category, name, buyShares, buyPrice, total, account);
                            }

                            getActivity().finish();
                            Intent intent = new Intent(getContext(), ViewStockActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }).start();
                    }
                }
            }
        });


        tv_stockSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder
                        = new AlertDialog.Builder(getContext());
                builder.setTitle("Name");

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.dialog,null);
                builder.setView(customLayout);
                AlertDialog dialog = builder.create();
                ListView listView = customLayout.findViewById(R.id.list_view);
                SearchView searchView = customLayout.findViewById(R.id.search_view);
                searchView.setQueryHint("Search...");
                searchView.setIconifiedByDefault(false);
                RxSearchView.queryTextChanges(searchView)
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<CharSequence>() {
                            @Override
                            public void accept(CharSequence charSequence) throws Throwable {
                                String newText = charSequence.toString();
                                if (newText != null) {
                                    String url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&apikey=MSVURHNT6I166LY8&keywords=" + newText;
                                    List<String> searchableSpinner_list = new ArrayList<String>();
                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                JSONArray jsonArray = response.getJSONArray("bestMatches");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject item = jsonArray.getJSONObject(i);
                                                    String symbol = item.getString("1. symbol");
                                                    searchableSpinner_list.add(symbol);
                                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, searchableSpinner_list);

                                                    listView.setAdapter(adapter);
                                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                            String selectedStock = String.valueOf(searchableSpinner_list.get(position));
                                                            tv_stockSymbol.setText(selectedStock);
                                                            if (allStockName.contains(selectedStock)){
                                                                Log.i("contain",selectedStock);
                                                                Log.i("contain", map_StockToCategory.get(selectedStock));
                                                                Log.i("contain", String.valueOf(adapter.getPosition("金融")));
                                                                Log.i("contain", String.valueOf(stockCategoryList.indexOf(map_StockToCategory.get(selectedStock))));
                                                                spinner.setSelection(stockCategoryList.indexOf(map_StockToCategory.get(selectedStock)));
                                                            }
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.i("result", "catch");
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                        }
                                    });
                                    requestQueue.add(request);
                                }
                            }
                        });
                dialog.show();
            }
        });
        return v;
    }



}