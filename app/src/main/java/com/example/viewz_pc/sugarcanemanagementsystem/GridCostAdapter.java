package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Viewz-PC on 8/1/2017.
 */

public class GridCostAdapter extends BaseAdapter {
    private Context context;
    private String[][] labels;
    private ArrayList<String> costList;

    public GridCostAdapter(Context context, ArrayList<String> costList, String[][] labels) {
        this.context = context;
        this.costList = costList;
        this.labels = labels;
    }

    @Override
    public int getCount() {
        return costList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridCostAdapter.ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new GridCostAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_cost, null); //More detail in layout resource
            holder.measureLabel = (TextView) convertView.findViewById(R.id.titleLabel);
            holder.unitLabel = (TextView) convertView.findViewById(R.id.unitLabel);
            holder.sumCost = (TextView) convertView.findViewById(R.id.cost);
            convertView.setTag(holder);
        } else {
            holder = (GridCostAdapter.ViewHolder) convertView.getTag();
        }
        holder.measureLabel.setText(labels[position][0]);
        holder.unitLabel.setText(labels[position][1]);
        holder.sumCost.setText(costList.get(position));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertView.setBackground(context.getDrawable(R.drawable.green_box));
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView measureLabel, unitLabel, sumCost;
    }
}
