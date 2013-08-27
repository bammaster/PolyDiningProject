package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SandwichActivity extends FragmentActivity {

    private Parser parseHtmlSand;
    private ListView lv;
    private TextView moneyView;
    private String money;
    private int tempIndex;

    private ViewPager vp;
    private PagerTabStrip myPagerTabStrip;
    private static ArrayList<SandwichActivity.FoodItemAdapter> foodAdapterList = new ArrayList<FoodItemAdapter>();
    public static ActionBar mActionBar;
    public static BigDecimal totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwich);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        totalAmount = MoneyTime.calcTotalMoney();
        /* The next couple lines of code dynamically sets up an ArrayList of FoodItemAdapters.
           One for each tab in the ViewPager. FoodItemAdapters are Adapters for the Card ListViews
           of each ViewPager Fragment. It gets passed in with the ViewPager adapter because the ViewPager Adapter
           will draw each fragment. MyPagerAdapter is the single adapter for the ViewPager which also uses a custom
           Fragment inner class called MyFragment.
         */

        foodAdapterList.clear();
        for(int i = 0;i<MainActivity.sandItems.size();i++)
        {
            if(MainActivity.sandItems.get(i).getNames().size() != 0) //check in case it's not the right time of day
            {
            /* Each of the components of sandItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
             */
            foodAdapterList.add(new FoodItemAdapter(this, MainActivity.sandItems.get(i).getTitle(), MainActivity.sandItems.get(i).getNames(),
                    MainActivity.sandItems.get(i).getPrices()));
            }

        }

        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(new SandwichPagerAdapter(getSupportFragmentManager(), foodAdapterList));
        vp.getAdapter().notifyDataSetChanged();

        myPagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        myPagerTabStrip.setTabIndicatorColor(0xC6930A);

        mActionBar = getActionBar();
        updateBalance();
        Cart.clear();
    }

    public void onResume()
    {
        super.onResume();
        updateBalance();
        /*moneyView.setText("$"+MoneyTime.calcTotalMoney());
        if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal("0"))==-1)
        {
            moneyView.setTextColor(Color.RED);
        }
        else
        {
            moneyView.setTextColor(Color.GREEN);
        }*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem money = menu.findItem(R.id.cart);
        //money.setTitle("$"+MoneyTime.calcTotalMoney()+"");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem money = menu.findItem(R.id.cart);
        //money.setTitle("$" + MoneyTime.calcTotalMoney());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.cart:
                Intent intent = new Intent(this, CartActivity.class);
                intent.putExtra("PARENT", "SandwichActivity.class");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FoodItemAdapter extends BaseAdapter implements View.OnClickListener {
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
            if(prices.size() != 0)
            {
                tvPrice.setText("$" + prices.get(position));
            }

            //Set the onClick Listener on this button
            ImageButton btnAdd = (ImageButton) convertView.findViewById(R.id.btn_add);
            btnAdd.setFocusableInTouchMode(false);
            btnAdd.setFocusable(false);
            btnAdd.setOnClickListener(this);
            btnAdd.setTag(new Integer(position));

            return convertView;
        }

        @Override
        public void onClick(View view) {
            final int position = (Integer) view.getTag();
            AlertDialog.Builder onListClick= new AlertDialog.Builder(SandwichActivity.this);
            onListClick.setTitle("Add to Cart?");
            onListClick.setMessage("Would you like to add " + names.get(position) + " to your cart? Price: " + "$" + prices.get(position));
            onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    //money = boundPrices.get(tempIndex);
                    if (names.get(position).contains("Soup") || names.get(position).contains("Salad")) {
                        AlertDialog.Builder onYes = new AlertDialog.Builder(SandwichActivity.this);
                        onYes.setTitle("How much?");
                        onYes.setMessage("Estimated Number of Ounces: ");
                        LayoutInflater inflater = SandwichActivity.this.getLayoutInflater();
                        View DialogView = inflater.inflate(R.layout.number_picker, null);
                        final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
                        np.setMinValue(1);
                        np.setMaxValue(50);
                        np.setWrapSelectorWheel(false);
                        np.setValue(1);
                        onYes.setView(DialogView);
                        onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                //MoneyTime.moneySpent = MoneyTime.moneySpent + (np.getValue()*new Double(money));
                                Cart.add(names.get(position), prices.get(position));
                                updateBalance();
                            }
                        });
                        onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                            }
                        });
                        onYes.show();
                    } else {
                        //MoneyTime.moneySpent = MoneyTime.moneySpent + (new Double(money));
                        Cart.add(names.get(position), prices.get(position));
                        updateBalance();
                    }
                }
            });
            onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                }
            });
            onListClick.show();
        }
    }
}