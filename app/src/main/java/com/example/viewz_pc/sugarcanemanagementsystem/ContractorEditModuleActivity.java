package com.example.viewz_pc.sugarcanemanagementsystem;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContractorEditModuleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] sendToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_edit_module);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContractorEditProfileFragment(), "ข้อมูลทั่วไป");
        adapter.addFragment(new ContractorEditConditionFragment(), "เงื่อนไข");
        adapter.addFragment(new ContractorEditRateFragment(), "การเงิน");
        viewPager.setAdapter(adapter);
    }
}
