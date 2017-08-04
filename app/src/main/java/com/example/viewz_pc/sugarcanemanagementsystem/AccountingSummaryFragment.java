package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccountingSummaryFragment extends Fragment {
    private View accountView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private TextView title, date;

    String[][] data = {{"30", "28", "6.66%"}, {"120.86", "100", "16.67%"}, {"140000", "10000", "28.57%"}
            , {"140000.54", "10000.43", "28.57%"}}; //{Real, Estimate, Error}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        accountView = inflater.inflate(R.layout.fragment_accounting_summary, container, false);
        Log.d("Accounting", "REFRESH3!");
        selectDateToView();
        return accountView;
    }

    public void selectDateToView() {
        title = (TextView) accountView.findViewById(R.id.title);
        if (getArguments().getString("CONDITION2").equals("cutter")) {
            title.setText(getResources().getString(R.string.harvest_summary));
        } else {
            title.setText(getResources().getString(R.string.tractor_summary));
        }
        date = (TextView)accountView.findViewById(R.id.firstDate);
        date.setText(getArguments().getString("DATE2"));

        initRecycler();
    }

    public void initRecycler() {
        recyclerView = (RecyclerView) accountView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        String[] title = {
                getResources().getString(R.string.weight_unit),
                getResources().getString(R.string.fuel_content_unit),
                getResources().getString(R.string.fuel_cost_unit),
                getResources().getString(R.string.repair_unit)};
        adapter = new CompareAdapter(data, title);
        recyclerView.setAdapter(adapter);
    }
}
