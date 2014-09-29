package com.mustangexchange.polymeal;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.mustangexchange.polymeal.models.Item;

import java.util.ArrayList;

//VenueListAdapter inflates the rows for each Item in the ItemSet for the current page in the ViewPager

public class ItemListAdapter extends BaseAdapter {
    private ItemListFragment fragment;
    private ArrayList<Item> items;

    public ItemListAdapter(Fragment fragment) {
        this.fragment = (ItemListFragment) fragment;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(items.get(position).getName());

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        tvPrice.setText(items.get(position).getPriceString());

        //Set the onClick Listener on this button
        ImageButton btnAdd = (ImageButton) convertView.findViewById(R.id.btn_add);
        btnAdd.setFocusableInTouchMode(false);
        btnAdd.setFocusable(false);
        btnAdd.setOnClickListener(fragment);
        btnAdd.setTag(position);
        return convertView;
    }

}