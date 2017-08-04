package com.example.viewz_pc.sugarcanemanagementsystem;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AccountingModuleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String date, condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_module);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        date = getIntent().getStringExtra("DATE"); // Start date
        condition = getIntent().getStringExtra("CONDITION");
    }

    private void setupViewPager(final ViewPager viewPager) {
        OneViewPagerAdapter adapter = new OneViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AccountingExpenseFragment(), "รายจ่าย");
        adapter.addFragment(new AccountingIncomeFragment(), "รายรับ");
        adapter.addFragment(new AccountingSummaryFragment(), "ข้อมูลสรุป");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
    }

    class OneViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        OneViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("DATE2", date);
            args.putString("CONDITION2", condition);
            fragmentList.get(position).setArguments(args);
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
