package com.example.viewz_pc.sugarcanemanagementsystem;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SurveyModuleActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_module);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SurveyRequestFragment(), "รอจัดคิวสำรวจ");
        adapter.addFragment(new SurveyAcceptedFragment(), "คิวสำรวจ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (SurveyRequestFragment.web.canGoBack()) {
            SurveyRequestFragment.web.goBack();
        }else if (SurveyAcceptedFragment.web.canGoBack()) {
            SurveyAcceptedFragment.web.goBack();
        }else if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }
}
