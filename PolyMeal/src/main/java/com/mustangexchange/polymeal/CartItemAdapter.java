package com.mustangexchange.polymeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: jon
 * Date: 8/24/13
 * Time: 5:43 PM
 * To change this template use File | Settings | File Templates.
 */

public class CartItemAdapter extends BaseAdapter implements OnClickListener {
    private Context context;
    private String parent;
    private ArrayList<String> cart;
    private ArrayList<String> cartMoney;

    public CartItemAdapter(Context context, String parent, ArrayList<String> cart, ArrayList<String> cartMoney) {
        this.context = context;
        this.cart = cart;
        this.cartMoney = cartMoney;
        this.parent = parent;
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
            convertView = inflater.inflate(R.layout.row_item_cart, null);
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
        System.out.println(parent);
        if(parent.equals(SandwichActivity.class))
        {
            SandwichActivity.totalAmount.add(new BigDecimal(cartMoney.get(entry)));
        }
        else if(parent.equals(VistaActivity.class))
        {
            VistaActivity.totalAmount.add(new BigDecimal(cartMoney.get(entry)));
        }
        cartMoney.remove(entry);
        // listPhonebook.remove(view.getId());
        notifyDataSetChanged();
    }
}