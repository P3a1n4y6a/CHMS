package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Viewz-PC on 7/25/2017.
 */

public class GridDashboardAdapter extends BaseAdapter {
    private Context context;
    private String[] title; //Button name
    private int[] icon; //Image ID to call image from drawable resource

    public GridDashboardAdapter(Context context, String[] title, int[] icon) {
        this.context = context;
        this.icon = icon;
        this.title = title;
    }

    @Override
    public int getCount() {
        return title.length;
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
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_button, null); //More detail in layout resource
            holder.title = (TextView)convertView.findViewById(R.id.grid_text);
            holder.icon = (ImageView)convertView.findViewById(R.id.grid_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(title[position]);
        holder.icon.setImageResource(icon[position]);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertView.setBackground(context.getDrawable(R.drawable.white_button));}*/
        return convertView;
    }

    public class ViewHolder {
        TextView title;
        ImageView icon;
    }
}