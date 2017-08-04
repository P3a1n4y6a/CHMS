package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Viewz-PC on 7/26/2017.
 */

public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.ViewHolder> {
    private ArrayList<FarmerModel> farmerList;
    private String status; // accept or request
    private Context context;

    FarmerAdapter(Context context, ArrayList<FarmerModel> farmerList, String status) {
        this.farmerList = farmerList;
        this.context = context;
        this.status = status;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemFullname, itemId;

        ViewHolder(final View itemView) {
            super(itemView);
            itemFullname = (TextView) itemView.findViewById(R.id.fullname);
            itemId = (TextView) itemView.findViewById(R.id.id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FarmerRequestModuleActivity.class);
                    intent.putExtra("FARMER_INDEX", getAdapterPosition());
                    intent.putExtra("FARMER_STATUS", status);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_farmer, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemFullname.setText(farmerList.get(i).getFullname());
        viewHolder.itemId.setText(farmerList.get(i).getId());
    }

    @Override
    public int getItemCount() {
        return farmerList.size();
    }
}
