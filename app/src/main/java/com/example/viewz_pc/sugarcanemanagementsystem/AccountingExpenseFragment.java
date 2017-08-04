package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class AccountingExpenseFragment extends Fragment {
    private View expenseView;
    private ExpandableHeightGridView gridView;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AccountingAdapter adapter;
    private ArrayList<String> costList, orderList;
    private TextView title, date;
    private String labelName, condition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        expenseView = inflater.inflate(R.layout.fragment_accounting_expense, container, false);
        Log.d("Accounting", "REFRESH!");
        initData();
        selectDateToView();
        return expenseView;
    }

    public String[][] getExpenseLabel() {
        String[][] words = new String[4][2]; //{measure, unit}
        words[0][0] = getActivity().getResources().getString(R.string.weight);
        words[0][1] = getActivity().getResources().getString(R.string.ton_unit);

        words[1][0] = getActivity().getResources().getString(R.string.fuel_content);
        words[1][1] = getActivity().getResources().getString(R.string.litre_unit);

        words[2][0] = getActivity().getResources().getString(R.string.repair);
        words[2][1] = getActivity().getResources().getString(R.string.baht_unit);

        words[3][0] = getActivity().getResources().getString(R.string.fuel_cost);
        words[3][1] = getActivity().getResources().getString(R.string.baht_unit);
        return words;
    }

    public void initData() {
        costList = new ArrayList<>();
        costList.add("1000");
        costList.add("5000");
        costList.add("20000");
        costList.add("300000");

        orderList = new ArrayList<>();
        orderList.add("6011-001");
        orderList.add("6011-002");
        orderList.add("6012-001");
        orderList.add("6012-002");
        orderList.add("6013-001");
    }

    public void selectDateToView() {
        condition = getArguments().getString("CONDITION2");
        title = (TextView) expenseView.findViewById(R.id.title);
        if (condition.equals("cutter")) {
            title.setText(getResources().getString(R.string.harvest_summary));
            labelName = getResources().getString(R.string.cutter_order);
        } else {
            title.setText(getResources().getString(R.string.tractor_summary));
            labelName = getResources().getString(R.string.tractor_order);
        }
        //From date
        date = (TextView)expenseView.findViewById(R.id.firstDate);
        date.setText(getArguments().getString("DATE2"));

        initGridLayout();
        initRecyclerView();
    }

    public void initGridLayout() {
        gridView = (ExpandableHeightGridView)expenseView.findViewById(R.id.gridView);
        GridCostAdapter adapter = new GridCostAdapter(getActivity(), costList, getExpenseLabel());
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
    }

    public void initRecyclerView() {
        recyclerView = (RecyclerView) expenseView.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AccountingAdapter(getActivity(), labelName, orderList, condition);
        recyclerView.setAdapter(adapter);
    }
}
