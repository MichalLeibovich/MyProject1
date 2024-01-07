package com.example.myproject1.loginsignup_page;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myproject1.loginsignup_page.LoginTabFragment;
import com.example.myproject1.loginsignup_page.SignUpTabFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private ILoginRegister iLoginRegister;
    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle,ILoginRegister loginRegister) {
        super(fragmentManager, lifecycle);
        iLoginRegister = loginRegister;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        if (position == 1)
            return new SignUpTabFragment();
        return new LoginTabFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
