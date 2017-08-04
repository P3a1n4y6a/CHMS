package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FarmerRequestModuleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int index;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_request_module);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        index = getIntent().getIntExtra("FARMER_INDEX", 0);
        status = getIntent().getStringExtra("FARMER_STATUS");
    }

    private void setupViewPager(ViewPager viewPager) {
        OneViewPagerAdapter adapter = new OneViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FarmerRequestDetailFragment(), "ข้อมูลชาวไร่");
        adapter.addFragment(new FarmerRequestPlantFragment(), "รายการแปลง");
        viewPager.setAdapter(adapter);
    }

    class OneViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        OneViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                Bundle args = new Bundle();
                args.putString("FARMER_STATUS2", status);
                args.putInt("FARMER_INDEX2", index);
                fragmentList.get(position).setArguments(args);
            }else if (position == 1) {
                Bundle args = new Bundle();
                args.putString("FARMER_STATUS2", status);
                args.putInt("FARMER_INDEX2", index);
                fragmentList.get(position).setArguments(args);
            }
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
    }
}
