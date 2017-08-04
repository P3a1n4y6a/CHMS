package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
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

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {
    private ArrayList<PlantModel> plantList;
    private Context context;

    PlantAdapter(Context context, ArrayList<PlantModel> plantList) {
        this.plantList = plantList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemPlant, itemArea;

        ViewHolder(final View itemView) {
            super(itemView);
            itemPlant = (TextView) itemView.findViewById(R.id.plant);
            itemArea = (TextView) itemView.findViewById(R.id.area);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShowPlantDetailActivity.class);
                    intent.putExtra("PLANT_INDEX", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_plant, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemPlant.setText(plantList.get(i).getId());
        viewHolder.itemArea.setText(plantList.get(i).getArea());
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }
}
