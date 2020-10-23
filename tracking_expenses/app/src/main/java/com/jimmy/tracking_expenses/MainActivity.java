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
import com.facebook.stetho.Stetho;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.toptoche.searchablespinnerlibrary.SearchableListDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

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
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigation_view = findViewById(R.id.side_navigation);
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("記帳");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        navigation_view.setNavigationItemSelectedListener((item) -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            Intent intent;
            switch (item.getItemId()){
                case R.id.side_nav1:
                    intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.side_nav2:
                    break;
                case R.id.side_nav3:
                    intent = new Intent(MainActivity.this, ViewStockActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        });

        //loadFragment(new BuyStockFragment());

        BottomNavigationView bottomNavigationView =
                findViewById(R.id.bottom_navigation);
        //bottomNavigationView.getMenu().setGroupCheckable(0, false, false);
        bottomNavigationView.setOnNavigationItemSelectedListener((item) ->{
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.main_bottom_nav1:
                    //fragment = new BuyStockFragment();
                    break;
                case R.id.main_bottom_nav2:

                    break;
                case R.id.main_bottom_nav3:
                    //fragment = new SellStockFragment();
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

