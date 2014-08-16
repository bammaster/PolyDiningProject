package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import com.mustangexchange.polymeal.models.Cart;

import java.math.BigDecimal;

public class CartActivity extends Activity {

    private ListView lv;
    private CartAdapter cartAdapter;
    public static Activity mActivity;

    private CartPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mActivity = this;

        init();

        presenter = new CartPresenter(this);

        updateBalance();

        isCartEmpty();
    }

    private void init()
    {
        cartAdapter = new CartAdapter(this);

        lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(cartAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                final int fPos = pos;
                final AlertDialog.Builder onListClick= new AlertDialog.Builder(mActivity);
                System.out.println(pos);
                onListClick.setCancelable(false);
                onListClick.setTitle("Remove to Cart?");
                onListClick.setMessage("Would you like to remove " + Cart.getCart().get(pos).getName() + " to your cart? \nPrice: " +  Cart.getCart().get(pos).getPriceString());
                onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        removeFromCart(fPos);
                    }
                });
                onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                    }
                });
                onListClick.show();
            }
        });
    }

    public void isCartEmpty()
    {
        if(lv.getAdapter().getCount() > 0) {
        }
        else
        {
            setContentView(R.layout.empty_cart);
        }
    }

    public void removeFromCart(int position) {
        Cart.remove(position);
        updateBalance();
        isCartEmpty();
        cartAdapter.notifyDataSetChanged();
    }

    public void setSubtitleColor() {
        int titleId = Resources.getSystem().getIdentifier("action_bar_subtitle", "id", "android");
        TextView yourTextView = (TextView)findViewById(titleId);
        if(presenter.getTotalAmount().compareTo(BigDecimal.ZERO) < 0)
        {
            yourTextView.setTextColor(Color.RED);
        }
        else
        {
            yourTextView.setTextColor(Color.WHITE);
        }
    }

    public void updateBalance() {
        try
        {
            setSubtitleColor();
            getActionBar().setSubtitle("$" + presenter.getTotalAmount() + " Remaining");
        }
        catch (NullPointerException e)
        {
            Intent intentHome = new Intent(this, PolyMealActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intentHome);
        }
    }

    public void onResume()
    {
        super.onResume();
        isCartEmpty();
        cartAdapter.updateCart();
        updateBalance();
        presenter.updateSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart, menu);
        return super.onCreateOptionsMenu(menu);
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
            /*case R.id.menuCom:
                Intent intent = new Intent(this, CompleteorActivity.class);
                startActivity(intent);
                return true;*/
            case R.id.clrCart:
                Cart.clear();
                cartAdapter.clearCart();
                cartAdapter.notifyDataSetChanged();
                isCartEmpty();
                updateBalance();
                Toast.makeText(this, "Cart Cleared!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CartAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;

        public CartAdapter(Context context)
        {
            this.context = context;
        }

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
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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