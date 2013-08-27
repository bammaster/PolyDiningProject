package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;

import java.math.BigDecimal;
import java.util.ArrayList;


public class VistaActivity extends FragmentActivity {


    private int minutes;
    private TextView moneyView;
    private Parser parseHtmlVg;

    private ViewPager vp;
    private PagerTabStrip myPagerTabStrip;
    private static ArrayList<FoodItemAdapter> foodAdapterList = new ArrayList<FoodItemAdapter>();
    public static ActionBar mActionBar;
    public static BigDecimal totalAmount;
    private static Context mContext;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[]  mDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);

        mContext = this;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItems = getResources().getStringArray(R.array.drawerItems);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /* The next couple lines of code dynamically sets up an ArrayList of FoodItemAdapters.
           One for each tab in the ViewPager. FoodItemAdapters are Adapters for the Card ListViews
           of each ViewPager Fragment. It gets passed in with the ViewPager adapter because the ViewPager Adapter
           will draw each fragment. MyPagerAdapter is the single adapter for the ViewPager which also uses a custom
           Fragment inner class called MyFragment.
        */
        foodAdapterList.clear();
        for(int i = 0;i<MainActivity.vgItems.size();i++)
        {
            if(MainActivity.vgItems.get(i).getNames().size() != 0) //check in case it's not the right time of day
            {
            /* Each of the components of sandItems is passed in INSTEAD of the actual list itself that way we only
               get what is applicable at this time period rather than the whole thing. This prevents
               ArrayOutOfBoundsExceptions later on.
            */
                foodAdapterList.add(new FoodItemAdapter(this, MainActivity.vgItems.get(i).getTitle(), MainActivity.vgItems.get(i).getNames(),
                        MainActivity.vgItems.get(i).getPrices()));
            }

        }

        vp = (ViewPager) findViewById(R.id.pager);
        vp.setAdapter(new VistaPagerAdapter(getSupportFragmentManager(), foodAdapterList));
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
        /*moneyView.setText("$" + MoneyTime.calcTotalMoney());
        if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal("0"))==-1)
        {
            moneyView.setTextColor(Color.RED);
        }
        else
        {
            moneyView.setTextColor(Color.parseColor("#C6930A"));
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

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            System.out.println(position);
            switch (position)
            {
                case 0:
                    Intent intentHome = new Intent(mContext, MainActivity.class);
                    startActivity(intentHome);
                case 1:
                    Intent intentSF = new Intent(mContext, SandwichActivity.class);
                    startActivity(intentSF);
                case 2:
                    Intent intentVG = new Intent(mContext, VistaActivity.class);
                    startActivity(intentVG);
                case 3:
                    Intent intentCP = new Intent(mContext, CompleteorActivity.class);
                    startActivity(intentCP);
                default:
            }
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
            String temp;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_item, null);
            }
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            temp = names.get(position);
            if(temp.contains("@#$"))
            {
                System.out.println(temp);
                tvName.setText(temp.substring(3));
            } else
            {
                tvName.setText(temp);
            }

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
            AlertDialog.Builder onListClick= new AlertDialog.Builder(VistaActivity.this);
            onListClick.setTitle("Add to Cart?");
            onListClick.setMessage("Would you like to add " + names.get(position) + " to your cart? Price: " + "$" + prices.get(position));
            onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    //money = boundPrices.get(tempIndex);
                    if (names.get(position).contains("@#$")) {
                        AlertDialog.Builder onYes = new AlertDialog.Builder(VistaActivity.this);
                        onYes.setTitle("How much?");
                        onYes.setMessage("Estimated Number of Ounces: ");
                        LayoutInflater inflater = VistaActivity.this.getLayoutInflater();
                        View DialogView = inflater.inflate(R.layout.number_picker, null);
                        final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
                        np.setMinValue(1);
                        np.setMaxValue(50);
                        np.setWrapSelectorWheel(false);
                        np.setValue(1);
                        onYes.setView(DialogView);
                        onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                Cart.add(names.get(position),  Double.toString(np.getValue()*new Double(prices.get(position))));
                                updateBalance();
                            }
                        });
                        onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                            }
                        });
                        onYes.show();
                    } else {
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
