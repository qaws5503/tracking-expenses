package com.jimmy.tracking_expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AddAccountActivity extends AppCompatActivity {
    ViewPager2 myViewPager2;
    ViewAccountViewPagerFragmentAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        TabLayout tabs = findViewById(R.id.addAccountTabs);
        Toolbar toolbar = findViewById(R.id.addAccountToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        myViewPager2 = findViewById(R.id.addAccountViewPager);

        myAdapter = new ViewAccountViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());


        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        myViewPager2.setAdapter(myAdapter);
        new TabLayoutMediator(tabs,myViewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("資產");
                    break;
                case 1:
                    tab.setText("負債");
                    break;
            }
        }).attach();
    }
}