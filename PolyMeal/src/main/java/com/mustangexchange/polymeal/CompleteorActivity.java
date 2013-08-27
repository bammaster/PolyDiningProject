package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CompleteorActivity extends Activity {

    private ItemSet possibleItems;
    private Thread calcCompleteor;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[]  mDrawerItems;
    private ActionBar mActionBar;
    private static BigDecimal totalAmount;
    private static Context mContext;

    public ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeor);

        possibleItems = new ItemSet("Completeor",new ArrayList<String>(),new ArrayList<String>());
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new CompleteorItemAdapter(this, possibleItems));

        calcCompleteor = new Thread(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.vgOrSand==1)
                {
                    for(int i = 0;i<MainActivity.vgItems.size();i++)
                    {
                        for(int j = 0;j<MainActivity.vgItems.get(i).getPrices().size();j++)
                        {
                            if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal(MainActivity.vgItems.get(i).getPrices().get(j)))<=0)
                            {
                                possibleItems.getNames().add(MainActivity.vgItems.get(i).getNames().get(j));
                                possibleItems.getPrices().add(MainActivity.vgItems.get(i).getPrices().get(j));
                            }
                        }
                    }
                }
                else if(MainActivity.vgOrSand==2)
                {
                    for(int i = 0;i<MainActivity.sandItems.size();i++)
                    {
                        for(int j = 0;j<MainActivity.sandItems.get(i).getPrices().size();j++)
                        {
                            if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal(MainActivity.sandItems.get(i).getPrices().get(j)))<=0)
                            {
                                possibleItems.getNames().add(MainActivity.vgItems.get(i).getNames().get(j));
                                possibleItems.getPrices().add(MainActivity.vgItems.get(i).getPrices().get(j));
                            }
                        }
                    }
                }
                ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
            }
        });
        calcCompleteor.start();

        mContext = this;

        mActionBar = getActionBar();
        updateBalance();

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
    }

    public void setSubtitleColor() {
        int titleId = Resources.getSystem().getIdentifier("action_bar_subtitle", "id", "android");
        TextView yourTextView = (TextView)findViewById(titleId);
        if(totalAmount.compareTo(BigDecimal.ZERO) < 0)
        {
            yourTextView.setTextColor(Color.RED);
        }
        else
        {
            yourTextView.setTextColor(Color.WHITE);
        }
    }

    public void updateBalance() {
        totalAmount = MoneyTime.calcTotalMoney();
        setSubtitleColor();
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
            if(parent.getPositionForView(view)==0)
            {
                Intent intentHome = new Intent(mContext, MainActivity.class);
                startActivity(intentHome);
            }
            else if(parent.getPositionForView(view)==1)
            {
                if(MainActivity.vgOrSand == 2)
                {
                    SandwichActivity.clear = false;
                } else
                {
                    MainActivity.vgOrSand = 2;
                }
                Intent intentSF = new Intent(mContext, SandwichActivity.class);
                startActivity(intentSF);
            }
            else if(parent.getPositionForView(view)==2)
            {
                if(MainActivity.vgOrSand == 1)
                {
                    VistaActivity.clear = false;
                } else
                {
                    MainActivity.vgOrSand = 1;
                }
                Intent intentVG = new Intent(mContext, VistaActivity.class);
                startActivity(intentVG);
            }
            else if(parent.getPositionForView(view)==3)
            {
                Intent intentCP = new Intent(mContext, CompleteorActivity.class);
                startActivity(intentCP);
            }
            else
            {
                Toast.makeText(mContext, "Invalid Selection!", Toast.LENGTH_SHORT);
            }
        }
    }

    public class CompleteorItemAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;
        private ItemSet possibleItems;

        public CompleteorItemAdapter(Context context, ItemSet possibleItems) {
            this.context = context;
            this.possibleItems = possibleItems;
        }

        public int getCount() {
            return possibleItems.getNames().size();
        }

        public Object getItem(int position) {
            return possibleItems.getNames().get(position);
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
            tvName.setText(possibleItems.getNames().get(position));

            TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            if(possibleItems.getPrices().size() != 0)
            {
                tvPrice.setText("$" + possibleItems.getPrices().get(position));
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
            AlertDialog.Builder onListClick= new AlertDialog.Builder(CompleteorActivity.this);
            onListClick.setTitle("Add to Cart?");
            onListClick.setMessage("Would you like to add " + possibleItems.getNames().get(position) + " to your cart? Price: " + "$" + possibleItems.getPrices().get(position));
            onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    //money = boundPrices.get(tempIndex);
                    if (possibleItems.getNames().get(position).contains("Soup") || possibleItems.getNames().get(position).contains("Salad")) {
                        AlertDialog.Builder onYes = new AlertDialog.Builder(CompleteorActivity.this);
                        onYes.setTitle("How much?");
                        onYes.setMessage("Estimated Number of Ounces: ");
                        LayoutInflater inflater = CompleteorActivity.this.getLayoutInflater();
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
                                Cart.add(possibleItems.getNames().get(position), possibleItems.getPrices().get(position));
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
                        Cart.add(possibleItems.getNames().get(position), possibleItems.getPrices().get(position));
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
