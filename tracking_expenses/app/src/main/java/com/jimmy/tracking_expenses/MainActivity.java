package com.jimmy.tracking_expenses;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableListDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    Runnable runnable;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        List<String> searchablespinner_list = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, searchablespinner_list);

        SearchableSpinner searchablespinner = (SearchableSpinner) findViewById(R.id.searchablespinner);
        searchablespinner.setOnSearchTextChangedListener(new SearchableListDialog.OnSearchTextChanged()
        {
            @Override
            public void onSearchTextChanged(String strText) {

            }
        });


        searchablespinner.setAdapter(adapter);

    }

    public void change(View view) {
        Intent intent = new Intent(this, ViewStockActivity.class);
        startActivity(intent);
    }

    public void showAlertDialogButtonClicked(View view)
    {



        // Create an alert builder
        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        builder.setTitle("Name");

        // set the custom layout
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog,null);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        ListView listView = customLayout.findViewById(R.id.list_view);
        SearchView searchView = customLayout.findViewById(R.id.search_view);
        searchView.setQueryHint("Search...");
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                handler.removeCallbacks(runnable);

                if (newText != null){
                    runnable = () -> {
                        String url = "https://www.alphavantage.co/query?function=SYMBOL_SEARCH&apikey=MSVURHNT6I166LY8&keywords=" + newText;
                        List<String> searchablespinner_list = new ArrayList<String>();
                        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("bestMatches");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject item = jsonArray.getJSONObject(i);
                                        String symbol = item.getString("1. symbol");
                                        searchablespinner_list.add(symbol);
                                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, searchablespinner_list);

                                        listView.setAdapter(adapter);
                                        Log.i("result",symbol);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                sendDialogDataToActivity(String.valueOf(searchablespinner_list.get(position)));
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("result","catch");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Log.i("result","err");
                            }
                        });
                        requestQueue.add(request);
                    };
                    handler.postDelayed(runnable, 1000);
                }


                return false;
            }
        });

        dialog.show();
    }



    private void sendDialogDataToActivity(String data)
    {
        Toast.makeText(this,
                data,
                Toast.LENGTH_SHORT)
                .show();
    }


}

