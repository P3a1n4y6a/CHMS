package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Viewz-PC on 8/4/2017.
 */

public class CriteriaListAdapter extends RecyclerView.Adapter<CriteriaListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> ans;
    private String[] criteria, unitList;

    public CriteriaListAdapter(Context contexts, String[] criteria, String[] unitList, ArrayList<String> ans) {
        this.context = contexts;
        this.ans = ans;
        this.criteria = criteria;
        this.unitList = unitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contractor_criteria, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.question.setText(criteria[position]);
        holder.answer.setText(ans.get(position));
        holder.unit.setText(unitList[position]);
        if (position%2 != 0) {
            holder.linearLayout.setBackgroundColor(context.getResources().getColor(R.color.colorSearchDateSelect));
        }
    }

    @Override
    public int getItemCount() {
        return criteria.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView question;
        TextView answer;
        TextView unit;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout);
            question = (TextView) itemView.findViewById(R.id.question);
            answer = (TextView) itemView.findViewById(R.id.answer);
            unit = (TextView) itemView.findViewById(R.id.unit);
        }
    }
}
