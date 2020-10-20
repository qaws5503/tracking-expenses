package com.jimmy.tracking_expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jimmy.tracking_expenses.StockDataBase.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddStockActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stock);

        loadFragment(new BuyStockFragment());

        BottomNavigationView bottomNavigationView =
                findViewById(R.id.stock_navigation);
        //bottomNavigationView.getMenu().setGroupCheckable(0, false, false);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) ->{
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.nav1:
                    fragment = new BuyStockFragment();
                    break;
                case R.id.nav2:
                    fragment = new SellStockFragment();
                    break;
            }
            return loadFragment(fragment);
        });


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}