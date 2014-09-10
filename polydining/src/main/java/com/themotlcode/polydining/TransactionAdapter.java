package com.themotlcode.polydining;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.themotlcode.polydining.models.Transaction;

import java.util.ArrayList;

public class TransactionAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private ArrayList<Transaction> trans;

    public TransactionAdapter(Context context, ArrayList<Transaction> trans)
    {
        this.context = context;
        activity = (Activity) context;
        this.trans = trans;
    }

    public int getCount()
    {
        return trans.size();
    }

    public Object getItem(int position)
    {
        return trans.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_trans, null);
        }

        TextView tvVenue = (TextView) convertView.findViewById(R.id.tv_venue);
        tvVenue.setText(trans.get(position).getPlace());

        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
        tvDate.setText(trans.get(position).getDate());

        TextView tvAmount = (TextView) convertView.findViewById(R.id.tv_amount);
        tvAmount.setText(trans.get(position).getAmount());

        return convertView;
    }

    public void updateData(ArrayList<Transaction> trans)
    {
        this.trans = trans;
        notifyDataSetChanged();
    }
}