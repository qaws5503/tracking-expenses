package com.jimmy.tracking_expenses;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
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
import com.jimmy.tracking_expenses.StockDataBase.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
        ArrayList arrayList = new ArrayList<String>();

        arrayList.add("金融");
        arrayList.add("科技");
        arrayList.add("傳統");

        ArrayAdapter adapter = new  ArrayAdapter(getContext()
                ,android.R.layout.simple_dropdown_item_1line,arrayList);
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
                new Thread(()->{
                    String category = spinner.getSelectedItem().toString();;
                    String name = tv_stockSymbol.getText().toString();
                    String account = "";
                    Log.i("ed",ed_stockPrice.getText().toString());
                    float buyPrice = Float.parseFloat(ed_stockPrice.getText().toString());
                    float buyShares = Float.parseFloat(ed_stockShares.getText().toString());
                    float total = buyPrice*buyShares;
                    DataBase.getInstance(getActivity()).getDataUao().insertData(category,name,buyShares,buyPrice,total,account);
                    getActivity().finish();
                    Intent intent = new Intent(getContext(),ViewStockActivity.class);
                    startActivity(intent);
                }).start();
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
                                                            tv_stockSymbol.setText(String.valueOf(searchableSpinner_list.get(position)));
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