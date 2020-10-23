package com.jimmy.tracking_expenses;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jimmy.tracking_expenses.StockDataBase.DataBase;
import com.jimmy.tracking_expenses.StockDataBase.StockData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SellStockFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SellStockFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Spinner sp_showStockSpinner;
    EditText ed_sellPrice,ed_sellShares;
    TextView tv_showAllStockPrice,tv_showAllStockShares,tv_showAllStockTitle;
    Button bt_sell,bt_cancel;
    StockData selectedStock;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SellStockFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SellStockFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SellStockFragment newInstance(String param1, String param2) {
        SellStockFragment fragment = new SellStockFragment();
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
        View v = inflater.inflate(R.layout.fragment_sell_stock, container, false);
        sp_showStockSpinner = v.findViewById(R.id.spinnerShowStock);
        ed_sellPrice = v.findViewById(R.id.editTextSellStockPrice);
        ed_sellShares = v.findViewById(R.id.editTextSellStockShares);
        tv_showAllStockPrice = v.findViewById(R.id.textViewShowAllStockPrice);
        tv_showAllStockShares = v.findViewById(R.id.textViewShowAllStockShares);
        tv_showAllStockTitle = v.findViewById(R.id.textViewShowStockTitle);
        bt_sell = v.findViewById(R.id.buttonSellStock);
        bt_cancel = v.findViewById(R.id.buttonSellStockCancel);

        new Thread(()->{
            List<StockData> stockDataList =  DataBase.getInstance(getContext()).getDataUao().displayAll();
            ArrayList<String> spinnerList = new ArrayList<String>();
            spinnerList.add("請選擇股票");
            for (StockData now : stockDataList){
                spinnerList.add(now.getName());
            }
            ArrayAdapter adapter = new  ArrayAdapter(getContext()
                    ,android.R.layout.simple_dropdown_item_1line,spinnerList);
            getActivity().runOnUiThread(()->{
                sp_showStockSpinner.setAdapter(adapter);
            });

        }).start();

        sp_showStockSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String spinnerText = parent.getItemAtPosition(position).toString();
                if (!spinnerText.equals("請選擇股票")) {
                    new Thread(() -> {
                        selectedStock = DataBase.getInstance(getContext()).getDataUao().findStockDataByName(
                                spinnerText);
                        getActivity().runOnUiThread(() -> {
                            tv_showAllStockPrice.setText("股票價格:" + String.valueOf(selectedStock.getBuyPrice()));
                            tv_showAllStockShares.setText("股票總數:" + String.valueOf(selectedStock.getBuyShares()));
                            tv_showAllStockPrice.setVisibility(View.VISIBLE);
                            tv_showAllStockShares.setVisibility(View.VISIBLE);
                            tv_showAllStockTitle.setVisibility(View.VISIBLE);
                        });
                    }).start();
                }
                else {
                    selectedStock = null;
                    tv_showAllStockPrice.setVisibility(View.INVISIBLE);
                    tv_showAllStockShares.setVisibility(View.INVISIBLE);
                    tv_showAllStockTitle.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bt_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_sellPrice.getText().toString())) {
                    ed_sellPrice.setError("請填入賣出股票價格");
                } else if (TextUtils.isEmpty(ed_sellShares.getText().toString())) {
                    ed_sellShares.setError("請填入賣出股票數量");
                }
                else {
                    if (selectedStock != null) {
                        float newPrice = Float.parseFloat(ed_sellPrice.getText().toString());
                        float newShares = Float.parseFloat(ed_sellShares.getText().toString());
                        float newTotal = newPrice * newShares;
                        float oldTotal = selectedStock.getTotal();
                        float oldShares = selectedStock.getBuyShares();
                        if (newShares == oldShares) {
                            new Thread(() -> {
                                DataBase.getInstance(getContext()).getDataUao().deleteData(selectedStock.getId());
                            }).start();
                        } else if (newShares > oldShares) {
                            AlertDialog.Builder builder
                                    = new AlertDialog.Builder(getContext());
                            builder.setTitle("錯誤");
                            builder.setMessage("欲賣出股票總數大於持有總數");
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            new Thread(() -> {

                                DataBase.getInstance(getContext()).getDataUao().updateData(
                                        selectedStock.getId(), selectedStock.getCategory(), selectedStock.getName(),
                                        oldShares - newShares, selectedStock.getBuyPrice(), oldTotal - newTotal, selectedStock.getAccount());
                            }).start();
                        }

                        getActivity().finish();
                        Intent intent = new Intent(getContext(), ViewStockActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(getContext());
                        builder.setTitle("錯誤");
                        builder.setMessage("選擇股票欄不得為空白");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });
        return v;
    }
}