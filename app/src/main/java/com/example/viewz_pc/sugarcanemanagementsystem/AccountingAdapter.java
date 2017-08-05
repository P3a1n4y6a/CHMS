package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Viewz-PC on 8/2/2017.
 */

public class AccountingAdapter extends RecyclerView.Adapter<AccountingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<PlantModel> order_plant, dialog_data;
    private int index = 0;

    AccountingAdapter(Context context, ArrayList<PlantModel> order_plant, ArrayList<PlantModel> dialog_data) {
        this.context = context;
        this.order_plant = order_plant;
        this.dialog_data = dialog_data;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView plantItem, farmerItem, surveyItem, employeeItem;
        Button showBtn;

        ViewHolder(final View itemView) {
            super(itemView);
            plantItem = (TextView)itemView.findViewById(R.id.plant);
            farmerItem = (TextView)itemView.findViewById(R.id.farmer);
            surveyItem = (TextView)itemView.findViewById(R.id.survey);
            employeeItem= (TextView)itemView.findViewById(R.id.employee);

            showBtn = (Button)itemView.findViewById(R.id.showBtn);
            showBtn.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    index = getAdapterPosition();

                }
            });
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View view = inflater.inflate(R.layout.dialog_plant_cost, null);
            DecimalFormat formatter = new DecimalFormat("#,###.##");

            index = getAdapterPosition();
            double db_format;
            TextView textView;
            textView = (TextView) view.findViewById(R.id.title);
            textView.setText("หมายเลขแปลง: " + order_plant.get(index).getId());

            textView = (TextView) view.findViewById(R.id.labor);
            db_format = Double.parseDouble(dialog_data.get(index).getLabor_cost());
            textView.setText(formatter.format(db_format));

            textView = (TextView) view.findViewById(R.id.repair);
            db_format = Double.parseDouble(dialog_data.get(index).getRepair_cost());
            textView.setText(formatter.format(db_format));

            textView = (TextView) view.findViewById(R.id.decadent);
            db_format = Double.parseDouble(dialog_data.get(index).getDecadent_cost());
            textView.setText(formatter.format(db_format));

            textView = (TextView) view.findViewById(R.id.fuel);
            db_format = Double.parseDouble(dialog_data.get(index).getFuel_cost());
            textView.setText(formatter.format(db_format));

            textView = (TextView) view.findViewById(R.id.trans);
            db_format = Double.parseDouble(dialog_data.get(index).getTrans_cost());
            textView.setText(formatter.format(db_format));

            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_plant_cost, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.plantItem.setText(order_plant.get(i).getId());
        viewHolder.farmerItem.setText(order_plant.get(i).getFullname());
        viewHolder.surveyItem.setText(order_plant.get(i).getSurvey());
        viewHolder.employeeItem.setText(order_plant.get(i).getEmployee());
    }

    @Override
    public int getItemCount() {
        return order_plant.size();
    }
}
