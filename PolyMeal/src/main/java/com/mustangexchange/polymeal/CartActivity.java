package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CartActivity extends Activity {

    private static TextView moneyView;
    private ListView lv;
    private static BigDecimal totalAmount;
    private static ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(new CartItemAdapter(this, Cart.getCart(), Cart.getCartMoney()));
        mActionBar = getActionBar();
        updateBalance();

        if(isCartEmpty())
        {
            setContentView(R.layout.empty_cart);
        }
        /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {

            }

        });*/
    }

    public static void setTextMoney()
    {
        moneyView.setText("$"+MoneyTime.calcTotalMoney());
        if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal("0"))==-1)
        {
            moneyView.setTextColor(Color.RED);
        }
        else
        {
            moneyView.setTextColor(Color.parseColor("#C6930A"));
        }
    }

    public boolean isCartEmpty()
    {
        if(lv.getAdapter().getCount() > 0) {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setNegative() {
        int titleId = Resources.getSystem().getIdentifier("action_bar_subtitle", "id", "android");
        TextView yourTextView = (TextView)findViewById(titleId);
        yourTextView.setTextColor(0xffcc0000);
    }

    public void updateBalance() {
        totalAmount = MoneyTime.calcTotalMoney();
        if(totalAmount.compareTo(BigDecimal.ZERO) < 0)
        {
            setNegative();
        }
        mActionBar.setSubtitle("$" + totalAmount + " Remaining");
    }
    public void onResume()
    {
        super.onResume();
/*      moneyView.setText("$"+MoneyTime.calcTotalMoney());
        if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal("0"))==-1)
        {
            moneyView.setTextColor(Color.RED);
        }
        else
        {
            moneyView.setTextColor(Color.parseColor("#C6930A"));
        }*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /*switch (item.getItemId())
        {
            case android.R.id.home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    public class CartItemAdapter extends BaseAdapter implements View.OnClickListener {
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
            cartMoney.remove(entry);
            updateBalance();
            if(isCartEmpty())
            {
                setContentView(R.layout.empty_cart);
            }
            // listPhonebook.remove(view.getId());
            notifyDataSetChanged();
        }
    }
    
}
