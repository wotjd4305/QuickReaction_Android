package com.example.QuickReactionMJ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VisitDataAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<VisitData> sample;

    public VisitDataAdapter(Context context, ArrayList<VisitData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public VisitData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.visit_data_listview, null);

        TextView visit = (TextView)view.findViewById(R.id.QRresult);
        TextView time = (TextView)view.findViewById(R.id.timeText);

        visit.setText(sample.get(position).getVisit());
        time.setText(sample.get(position).getTime());

        return view;
    }
}
