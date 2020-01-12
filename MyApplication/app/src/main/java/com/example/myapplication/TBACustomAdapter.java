package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TBACustomAdapter extends ArrayAdapter {
    private final Activity activity;
    private final List list;

    public TBACustomAdapter(Activity activity, int position, ArrayList<CourseTime> list) {
        super(activity ,position,list);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {




        View rowView = convertView;

        ViewHolder view;

        if(rowView == null)
        {
            // Get a new instance of the row layout view
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.rows, null);

            // Hold the view objects in an object, that way the don't need to be "re-  finded"
            view = new ViewHolder();
            view.sname= (TextView) rowView.findViewById(R.id.textVieww);
            view.atime= (TextView) rowView.findViewById(R.id.textViewww);

            rowView.setTag(view);
        } else {
            view = (ViewHolder) rowView.getTag();

        }

        /** Set data to your Views. */
        CourseTime item = (CourseTime) list.get(position);
        view.sname.setText(item.getFullname());
        view.atime.setText(item.getTime());
            if (position % 2 == 1) {

                rowView.setBackgroundColor(Color.parseColor("#474747"));

            } else {

                rowView.setBackgroundColor(Color.parseColor("#E94C3D"));

            }

        return rowView;
    }

    protected static class ViewHolder{
        protected TextView sname;
        protected TextView atime;
    }
}