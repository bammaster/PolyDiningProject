package com.themotlcode.polydining;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.Item;
import com.themotlcode.polydining.models.ItemList;

import java.math.BigDecimal;
import java.util.ArrayList;

//VenueListAdapter inflates the rows for each Item in the ItemSet for the current page in the ViewPager

public class ItemListAdapter extends BaseAdapter {
    private ItemListFragment fragment;
    private MealPresenter presenter;
    private ArrayList<Item> items;

    public ItemListAdapter(Fragment fragment, MealPresenter presenter, ItemList itemList)
    {
        this.fragment = (ItemListFragment) fragment;
        this.presenter = presenter;
        this.items = itemList.getItems();
    }

    public int getCount()
    {
        return items.size();
    }

    public Object getItem(int position)
    {
        return items.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
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
        btnAdd.setTag(new Integer(position));
        return convertView;
    }

}