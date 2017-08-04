package com.example.viewz_pc.sugarcanemanagementsystem;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CuttingQueueModuleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutting_queue_module);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CuttingQueueWaitFragment(), "คิวรอสั่งตัด");
        adapter.addFragment(new CuttingQueueInProcessFragment(), "กำลังดำเนินการตัด");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (CuttingQueueWaitFragment.web.canGoBack()) {
            CuttingQueueWaitFragment.web.goBack();
        }else if (CuttingQueueInProcessFragment.web.canGoBack()) {
            CuttingQueueInProcessFragment.web.goBack();
        }else if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }
}
