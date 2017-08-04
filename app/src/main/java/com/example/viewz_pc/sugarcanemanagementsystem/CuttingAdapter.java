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
 * Created by Viewz-PC on 7/26/2017.
 */

public class CuttingAdapter extends RecyclerView.Adapter<CuttingAdapter.ViewHolder> {
    private ArrayList<CuttingModel> cuttingList;
    private Context context;

    CuttingAdapter(Context context, ArrayList<CuttingModel> cuttingList) {
        this.cuttingList = cuttingList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemId, itemEstCut;

        ViewHolder(final View itemView) {
            super(itemView);
            itemId = (TextView) itemView.findViewById(R.id.id);
            itemEstCut = (TextView) itemView.findViewById(R.id.estCut);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(context, CuttingPlantListActivity.class);
                    intent.putExtra("PLANT_INDEX", getAdapterPosition());
                    context.startActivity(intent);*/
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cutting, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemId.setText(cuttingList.get(i).getId());
        viewHolder.itemEstCut.setText(cuttingList.get(i).getEst_cutting());
    }

    @Override
    public int getItemCount() {
        return cuttingList.size();
    }
}
