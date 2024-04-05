package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;

import com.example.myproject1.loginsignup_screen.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{

    FirebaseAuth fUser = FirebaseAuth.getInstance();
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get window().setFlags(WindowManager.LayoutParams.FLAGS_FULLSCREEN), WindowManager.LayoutParams.FLAG_FULLSCREEN); initViews();
        this.tabLayout = findViewById(R.id.tab_layout);
        this.viewPager2 = findViewById(R.id.view_pager);

        this.tabLayout.addTab(this.tabLayout.newTab().setText("log In"));
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Sign Up"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        this.viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        this.viewPager2.setAdapter(this.viewPagerAdapter);

        if (fUser.getCurrentUser() != null) {
            //Intent intent = new Intent(MainActivity.this, MainScreenActivity.class);
            Intent intent = new Intent(MainActivity.this, MainScreenActivity2.class);
            startActivity(intent);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                // why I can't write this. in listeners I suppose?
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        this.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position)
            {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }




}