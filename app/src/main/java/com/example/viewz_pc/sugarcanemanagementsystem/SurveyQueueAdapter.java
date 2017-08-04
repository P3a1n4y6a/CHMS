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
 * Created by Viewz-PC on 7/27/2017.
 */

public class SurveyQueueAdapter extends RecyclerView.Adapter<SurveyQueueAdapter.ViewHolder> {
    private ArrayList<SurveyQueueModel> queueList;
    private Context context;

    SurveyQueueAdapter(Context context, ArrayList<SurveyQueueModel> queueList) {
        this.queueList = queueList;
        this.context = context;

        for (int i = 0; i < queueList.size(); i++) {
            Log.d("SurveyAdapter", queueList.get(i).getId());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemId, itemStatus;

        ViewHolder(final View itemView) {
            super(itemView);
            itemId = (TextView) itemView.findViewById(R.id.id);
            itemStatus = (TextView) itemView.findViewById(R.id.status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(context, ShowPlantDetailActivity.class);
                    intent.putExtra("PLANT_INDEX", getAdapterPosition());
                    context.startActivity(intent);*/
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_survey_queue, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemId.setText(queueList.get(i).getId());
        viewHolder.itemStatus.setText(queueList.get(i).getStatus());
    }

    @Override
    public int getItemCount() {
        return queueList.size();
    }
}