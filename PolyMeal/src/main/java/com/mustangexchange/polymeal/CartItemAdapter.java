package com.mustangexchange.polymeal;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: jon
 * Date: 8/24/13
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */

public class CartItemAdapter extends BaseAdapter implements OnClickListener {
    private Context context;

    private ArrayList<String> cart;
    private ArrayList<String> cartMoney;

    public CartItemAdapter(Context context, ArrayList<String> cart, ArrayList<String> cartMoney) {
        this.context = context;
        this.cart = cart;
        this.cartMoney = cartMoney;
    }

    public int getCount() {
        return cart.size();
    }

    public Object getItem(int position) {
        return cart.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Integer entry = position;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(cart.get(position));

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        tvPrice.setText("$" + cartMoney.get(position));


        //Set the onClick Listener on this button
        ImageButton btnRemove = (ImageButton) convertView.findViewById(R.id.btn_rmv);
        btnRemove.setFocusableInTouchMode(false);
        btnRemove.setFocusable(false);
        btnRemove.setOnClickListener(this);
        btnRemove.setTag(entry);



        return convertView;
    }

    @Override
    public void onClick(View view) {
        Integer entry = (Integer) view.getTag();
        Cart.remove(entry);
        cart.remove(entry);
        cartMoney.remove(entry);
        // listPhonebook.remove(view.getId());
        notifyDataSetChanged();

    }
}