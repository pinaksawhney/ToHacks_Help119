package com.to.hacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

public class AccountActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);

        //set Me selected
        bottomNavigationView.setSelectedItemId(R.id.account);

        //itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.add_report:
                startActivity(new Intent(getApplicationContext(), AddReportActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.account:
                return true;
        }
        return false;
    }
}