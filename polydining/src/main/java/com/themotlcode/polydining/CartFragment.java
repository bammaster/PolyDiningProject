package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;
import com.themotlcode.polydining.models.Cart;

public class CartFragment extends Fragment
{
    private ListView lv;
    private CartAdapter cartAdapter;
    private View v;
    private CartPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        v = inflater.inflate(R.layout.fragment_cart, container, false);

        ((MainActivity) getActivity()).viewDrawer(false);

        init();

        presenter = new CartPresenter(this);

        updateBalance();

        isCartEmpty();

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        isCartEmpty();
        cartAdapter.updateCart();
        updateBalance();
        presenter.updateSettings();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.cart, menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuCom:
                MealCompleterFragment mcFragment = new MealCompleterFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout, mcFragment).addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.clrCart:
                Cart.clear();
                cartAdapter.clearCart();
                cartAdapter.notifyDataSetChanged();
                isCartEmpty();
                updateBalance();
                Toast.makeText(getActivity(), "Cart Cleared!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init()
    {
        cartAdapter = new CartAdapter();

        this.setHasOptionsMenu(true);

        lv = (ListView) v.findViewById(R.id.listView);
        lv.setAdapter(cartAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                final int fPos = pos;
                final AlertDialog.Builder onListClick= new AlertDialog.Builder(getActivity());
                System.out.println(pos);
                onListClick.setCancelable(false);
                onListClick.setTitle("Remove to Cart?");
                onListClick.setMessage("Would you like to remove " + Cart.getCart().get(pos).getName() + " to your cart? \nPrice: " + Cart.getCart().get(pos).getPriceString());
                onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int button)
                    {
                        removeFromCart(fPos);
                    }
                });
                onListClick.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int button)
                    {
                    }
                });
                onListClick.create();
                Dialog d = onListClick.show();
                int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                TextView tv = (TextView) d.findViewById(textViewId);
                tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
            }
        });
    }

    private void isCartEmpty()
    {
        if(lv.getAdapter().getCount() <= 0)
        {
            v.findViewById(R.id.cart).setVisibility(View.GONE);
            v.findViewById(R.id.emptyCart).setVisibility(View.VISIBLE);
        }
    }

    private void removeFromCart(int position) {
        Cart.remove(position);
        updateBalance();
        isCartEmpty();
        cartAdapter.notifyDataSetChanged();
    }

    protected void updateBalance() {
        presenter.updateBalance();
    }

    protected class CartAdapter extends BaseAdapter implements View.OnClickListener {

        public void updateCart()
        {
            notifyDataSetChanged();
        }

        public void clearCart()
        {
            Cart.clear();
        }

        public int getCount()
        {
            return Cart.getCart().size();
        }

        public Object getItem(int position)
        {
            return Cart.get(position);
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup)
        {
            Integer entry = position;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.row_item_cart, null);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            tvName.setText(Cart.get(position).getName().replace("@#$",""));

            TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            tvPrice.setText("$" + Cart.get(position).getPrice());


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
            Cart.remove(entry);
            updateBalance();
            isCartEmpty();
            notifyDataSetChanged();
        }
    }
}
