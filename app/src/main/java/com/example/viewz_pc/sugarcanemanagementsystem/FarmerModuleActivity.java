package com.example.viewz_pc.sugarcanemanagementsystem;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.viewz_pc.sugarcanemanagementsystem.R.id.viewpager;

public class FarmerModuleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_module);

        viewPager = (ViewPager) findViewById(viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FarmerRequestFragment(), "แปลงใหม่");
        adapter.addFragment(new FarmerProcessFragment(), "คิวรับเหมา");
        adapter.addFragment(new FarmerAllFragment(), "ชาวไร่");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (FarmerRequestFragment.web.canGoBack()) {
            FarmerRequestFragment.web.goBack();
        }else if (FarmerProcessFragment.web.canGoBack()) {
            FarmerProcessFragment.web.goBack();
        }else if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }
}
