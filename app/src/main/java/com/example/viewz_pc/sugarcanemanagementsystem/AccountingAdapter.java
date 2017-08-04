package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Viewz-PC on 8/2/2017.
 */

public class AccountingAdapter extends RecyclerView.Adapter<AccountingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> order_plant;
    private String labelName, condition;

    AccountingAdapter(Context context, String labelName, ArrayList<String> order_plant, String condition) {
        this.context = context;
        this.labelName = labelName;
        this.order_plant = order_plant;
        this.condition = condition;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView plantItem, titleItem;

        ViewHolder(final View itemView) {
            super(itemView);
            titleItem = (TextView)itemView.findViewById(R.id.idLabel);
            plantItem = (TextView)itemView.findViewById(R.id.cutIdData);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int i = getAdapterPosition();
                    Intent intent = new Intent(context, AccountingHarvesterActivity.class);
                    intent.putExtra("PLANT_ID", order_plant.get(i));
                    intent.putExtra("CONDITION", condition);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_queue, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.titleItem.setText(labelName);
        viewHolder.plantItem.setText(order_plant.get(i));
    }

    @Override
    public int getItemCount() {
        return order_plant.size();
    }
}
