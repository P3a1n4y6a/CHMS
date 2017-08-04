package com.example.viewz_pc.sugarcanemanagementsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Viewz-PC on 7/27/2017.
 */

public class CompareAdapter extends RecyclerView.Adapter<CompareAdapter.ViewHolder> {
    private String[][] data;
    private String[] title;

    CompareAdapter(String[][] data, String[] title) {
        this.data = data;
        this.title = title;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleItem;
        TextView realItem;
        TextView estimateItem;
        TextView errorItem;

        ViewHolder(final View itemView) {
            super(itemView);
            titleItem = (TextView)itemView.findViewById(R.id.label);
            realItem = (TextView)itemView.findViewById(R.id.realData);
            estimateItem = (TextView)itemView.findViewById(R.id.estimateData);
            errorItem = (TextView)itemView.findViewById(R.id.errorData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_compare, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        double real_amount = Double.parseDouble(data[i][0]);
        double est_amount = Double.parseDouble(data[i][1]);
        DecimalFormat formatter = new DecimalFormat("#,###.##");

        viewHolder.titleItem.setText(title[i]);
        viewHolder.realItem.setText(formatter.format(real_amount));
        viewHolder.estimateItem.setText(formatter.format(est_amount));
        viewHolder.errorItem.setText(data[i][2]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
