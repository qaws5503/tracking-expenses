package com.jimmy.tracking_expenses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AddCategoryActivity extends AppCompatActivity {
    ViewPager2 myViewPager2;
    ViewCategoryViewPagerFragmentAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        TabLayout tabs = findViewById(R.id.tabs);
        myViewPager2 = findViewById(R.id.viewPager);
        Toolbar toolbar = findViewById(R.id.addCategoryToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myAdapter = new ViewCategoryViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());


        // set Orientation in your ViewPager2
        myViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        myViewPager2.setAdapter(myAdapter);
        new TabLayoutMediator(tabs,myViewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("支出");
                    break;
                case 1:
                    tab.setText("收入");
                    break;
            }
        }).attach();
    }
}