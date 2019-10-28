package com.example.corre;

import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class RunList extends ArrayAdapter<Run> {

    private AppCompatActivity context;
    private List<Run> runList;

    public RunList(AppCompatActivity context, List<Run> runList){
        super(context, R.layout.runlist_layout, runList);
        this.context = context;
        this.runList = runList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.runlist_layout, null, true);

        TextView tv_Date = (TextView) listViewItem.findViewById(R.id.textViewDate);
        //TextView tv_Cal = (TextView) listViewItem.findViewById(R.id.textViewCalories);
        TextView tv_Distance = (TextView) listViewItem.findViewById(R.id.textViewDistance);
        TextView tv_Time = (TextView) listViewItem.findViewById(R.id.textViewTime);
        TextView tv_Rate = (TextView) listViewItem.findViewById(R.id.textViewRate);
        TextView tv_Id = (TextView) listViewItem.findViewById(R.id.tv_id);
        Run run = runList.get(position);

        tv_Id.setText(run.getId());
        tv_Date.setText(run.getDate());
        tv_Time.setText(run.getTime());


        tv_Distance.setText(run.doubleToStr(run.getDistance()) + " mi");   // Need to override for a string
        tv_Rate.setText(run.doubleToStr(run.getRate())+ " MPH");  // Need to override for a string

        return listViewItem;

    }
}
