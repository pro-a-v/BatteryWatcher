package com.example.aprochakovskiy.batt_watcher;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyBatteryAdapter extends ArrayAdapter<Battery> {

    private static class ViewHolder {
        TextView tvBatNum;
        TextView tvBatVal;
        TextView tvBatDate;
    }

    public MyBatteryAdapter(Context context, ArrayList<Battery> batteries) {
        super(context, 0, batteries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Battery battery = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_battery, parent, false);

            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_battery, parent, false);
            viewHolder.tvBatNum = (TextView) convertView.findViewById(R.id.tvBatNum);
            viewHolder.tvBatVal = (TextView) convertView.findViewById(R.id.tvBatVal);
            viewHolder.tvBatDate = (TextView) convertView.findViewById(R.id.tvBatDate);
            convertView.setTag(viewHolder);
        }
        else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

                // default colors
                convertView.setBackgroundColor(Color.WHITE);
                viewHolder.tvBatNum.setTextColor(ColorStateList.valueOf(Color.BLACK));
                viewHolder.tvBatVal.setTextColor(ColorStateList.valueOf(Color.BLACK));
                viewHolder.tvBatDate.setTextColor(ColorStateList.valueOf(Color.BLACK));

                // Populate the data into the template view using the data object
                viewHolder.tvBatNum.setText(new DecimalFormat("00").format((double) battery.getID()));
                //viewHolder.tvBatNum.setText(Integer.toString(battery.getID()));

                // Voltage above maximum
                if(battery.getVoltage()>battery.get_max_value()) {
                    viewHolder.tvBatVal.setTextColor(ColorStateList.valueOf(Color.RED));
                    parent.setBackgroundColor(Color.RED);
                }
                // Voltage less then minimum
                if(battery.getVoltage()<battery.get_min_value()) {
                    viewHolder.tvBatVal.setTextColor(ColorStateList.valueOf(Color.RED));
                    parent.setBackgroundColor(Color.RED);
                }

                DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
                unusualSymbols.setDecimalSeparator('.');
                unusualSymbols.setGroupingSeparator(' ');
                viewHolder.tvBatVal.setText(new DecimalFormat("#####0.000", unusualSymbols).format((double) battery.getVoltage() / 1000));
                //viewHolder.tvBatVal.setText(String.format(Locale.ENGLISH,"%1.3f",(double) battery.getVoltage() / 1000));

                // Data time  more then 5 min ago
                //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(battery.getVoltageDatetime()));
                String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date(battery.getVoltageDatetime()));
                if(battery.getVoltageDatetime() < (new Date().getTime() - ( 5 * 60 * 1000 )) ) {
                    viewHolder.tvBatDate.setTextColor(ColorStateList.valueOf(Color.RED));
                    parent.setBackgroundColor(Color.RED);
                }
                viewHolder.tvBatDate.setText(timeStamp);



        // Return the completed view to render on screen
        return convertView;
    }
}