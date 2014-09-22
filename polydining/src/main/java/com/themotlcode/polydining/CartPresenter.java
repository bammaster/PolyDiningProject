package com.themotlcode.polydining;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.Item;

public class CartPresenter extends MealPresenter
{
    private CartFragment fragment;
    private CartAdapter cartAdapter;
    private PolyApplication app;

    public CartPresenter(Fragment fragment)
    {
        this.fragment = (CartFragment) fragment;
        setFragment(fragment);
        app = ((PolyApplication) fragment.getActivity().getApplication());
    }

    protected void updateSettings() {
        int sortMode;

        sortMode = Integer.valueOf(app.defaultSP.getString("sortMode", "0"));

        app.cart.sort(sortMode);
    }

    protected void clearCart()
    {
        app.cart.clear();
        cartAdapter.clearCart();
        cartAdapter.notifyDataSetChanged();
        updateBalance();
    }

    protected void updateCart()
    {
        cartAdapter.updateCart();
        updateBalance();
    }

    protected void removeFromCart(int position) {
        app.cart.remove(position);
        updateBalance();
        fragment.isCartEmpty();
        cartAdapter.notifyDataSetChanged();
    }

    public CartAdapter getAdapter()
    {
        if(cartAdapter == null)
        {
            cartAdapter = new CartAdapter();
        }
        return cartAdapter;
    }

    public Item get(int pos)
    {
        return app.cart.get(pos);
    }

    protected class CartAdapter extends BaseAdapter implements View.OnClickListener {

        public void updateCart()
        {
            notifyDataSetChanged();
        }

        public void clearCart()
        {
            app.cart.clear();
        }

        public int getCount()
        {
            return app.cart.getCart().size();
        }

        public Object getItem(int position)
        {
            return app.cart.get(position);
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup)
        {
            Integer entry = position;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
                convertView = inflater.inflate(R.layout.row_item_cart, null);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvName.setText(app.cart.get(position).getName().replace("@#$",""));

            TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            tvPrice.setText("$" + app.cart.get(position).getPrice());


            //Set the onClick Listener on this button
            ImageButton btnRemove = (ImageButton) convertView.findViewById(R.id.btn_rmv);
            btnRemove.setFocusableInTouchMode(false);
            btnRemove.setFocusable(false);
            btnRemove.setOnClickListener(this);
            btnRemove.setTag(entry);

            return convertView;
        }

        @Override
        public void onClick(View view)
        {
            Integer entry = (Integer) view.getTag();
            app.cart.remove(entry);
            updateBalance();
            fragment.isCartEmpty();
            notifyDataSetChanged();
        }
    }
}
