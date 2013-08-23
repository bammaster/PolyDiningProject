package com.mustangexchange.polymeal;

/**
 * Created with IntelliJ IDEA.
 * User: jon
 * Date: 8/23/13
 * Time: 12:33 AM
 * To change this template use File | Settings | File Templates.
 */
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FoodItemAdapter extends BaseAdapter /*implements OnClickListener*/ {
    private Context context;

    private ArrayList<String> names;
    private ArrayList<String> prices;
    private String title;

    public FoodItemAdapter(Context context, String title, ArrayList<String> names, ArrayList<String> prices) {
        this.context = context;
        this.names = names;
        this.prices = prices;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getCount() {
        return names.size();
    }

    public Object getItem(int position) {
        return names.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //ItemSet entry = setList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(names.get(position));

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        tvPrice.setText("$" + prices.get(position));

        /* Set the onClick Listener on this button
        ImageButton btnAdd = (ImageButton) convertView.findViewById(R.id.btn_add);
        btnRemove.setFocusableInTouchMode(false);
        btnRemove.setFocusable(false);
        btnAdd.setOnClickListener(this);*/



        return convertView;
    }

    /*@Override
    public void onClick(View view) {
        view.
        Item entry = (Item) view.getTag();
        listItem.remove(entry);
        // listPhonebook.remove(view.getId());
        notifyDataSetChanged();

    }*/
}