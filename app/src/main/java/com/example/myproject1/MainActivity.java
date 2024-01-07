package com.example.myproject1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import com.example.myproject1.GamePage.GameActivity;
import com.example.myproject1.loginsignup_page.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity{

    // implements ILoginRegister
    FirebaseAuth fUser = FirebaseAuth.getInstance();
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tabLayout = findViewById(R.id.tab_layout);
        this.viewPager2 = findViewById(R.id.view_pager);

        this.tabLayout.addTab(this.tabLayout.newTab().setText("log In"));
        this.tabLayout.addTab(this.tabLayout.newTab().setText("Sign Up"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        // this.viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle(),this);
        this.viewPagerAdapter = new ViewPagerAdapter(fragmentManager, getLifecycle());
        this.viewPager2.setAdapter(this.viewPagerAdapter);

        if (fUser.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                // TODO: find out why I can't write this. in listeners I suppose?
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