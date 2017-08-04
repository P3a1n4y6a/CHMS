package com.example.viewz_pc.sugarcanemanagementsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountingHarvesterActivity extends AppCompatActivity {
    private ExpandableHeightGridView gridView;
    private ArrayList<String> costList, eventList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AccountingAdapter adapter;
    private TextView plant, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_harvester);

        initPreviousData();
        initGridLayout();
    }

    public String[][] getExpenseLabel() {
        String[][] words = new String[4][2]; //{measure, unit}
        words[0][0] = getResources().getString(R.string.weight);
        words[0][1] = getResources().getString(R.string.ton_unit);

        words[1][0] = getResources().getString(R.string.fuel_content);
        words[1][1] = getResources().getString(R.string.litre_unit);

        words[2][0] = getResources().getString(R.string.repair);
        words[2][1] = getResources().getString(R.string.baht_unit);

        words[3][0] = getResources().getString(R.string.fuel_cost);
        words[3][1] = getResources().getString(R.string.baht_unit);
        return words;
    }

    public void initPreviousData() {
        costList = new ArrayList<>();
        costList.add("1000");
        costList.add("5000");
        costList.add("20000");
        costList.add("300000");

        plant = (TextView) findViewById(R.id.plantData);
        plant.setText(getIntent().getStringExtra("PLANT_ID"));

        title = (TextView) findViewById(R.id.title);
        if (getIntent().getStringExtra("CONDITION").equals("cutter")) {
            title.setText(getResources().getString(R.string.total_cut));
        } else {
            title.setText(getResources().getString(R.string.total_time));
        }
    }

    public void initGridLayout() {
        gridView = (ExpandableHeightGridView)findViewById(R.id.gridView);
        GridCostAdapter adapter = new GridCostAdapter(AccountingHarvesterActivity.this, costList, getExpenseLabel());
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
    }
}
