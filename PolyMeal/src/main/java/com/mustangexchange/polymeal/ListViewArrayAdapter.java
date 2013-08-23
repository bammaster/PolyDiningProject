package com.mustangexchange.polymeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Blake on 8/10/13.
 */
public class ListViewArrayAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> strings;
    int tempPosition;

    public ListViewArrayAdapter(Context context, ArrayList<String> strings) {
        super(context, R.layout.activity_cart, strings);
        this.strings = strings;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_item, parent, false);

        final TextView titleTextView = (TextView) convertView.findViewById(R.id.listText);
        titleTextView.setText(strings.get(position));
        ImageButton delete = (ImageButton)convertView.findViewById(R.id.deleteButton);
        delete.setTag(new Integer(position));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart.remove(new Integer(view.getTag().toString()));
                CartActivity.setTextMoney();
                notifyDataSetChanged();
            }
        });

        return convertView;

    }
}