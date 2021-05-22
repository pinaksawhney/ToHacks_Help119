package com.to.hacks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddReportActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        //set Me selected
        bottomNavigationView.setSelectedItemId(R.id.add_report);
        //itemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.explore:
                startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.add_report:
                return true;
            case R.id.account:
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    }
}