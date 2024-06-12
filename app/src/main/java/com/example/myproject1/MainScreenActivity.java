package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainScreenActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen2);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.item_home);
    }

    HomeFragment homeFragment = new HomeFragment();
    MyGalleryFragment myGalleryFragment = new MyGalleryFragment();



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.constraintLayout_mainScreen, homeFragment)
                        .commit();
                return true;

            case R.id.item_myGallery:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.constraintLayout_mainScreen, myGalleryFragment)
                        .commit();
                return true;
        }
        return false;
    }

}