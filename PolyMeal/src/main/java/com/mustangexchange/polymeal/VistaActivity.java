package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.*;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;


public class VistaActivity extends FragmentActivity {


    private int minutes;
    private TextView moneyView;
    private Parser parseHtml;
    private Handler uiUpdate= new Handler();
    private ProgressDialog status;

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
    public static Activity mActivity;
    public static boolean clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);

        mContext = this;
        mActivity = this;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerItems = getResources().getStringArray(R.array.drawerItems);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        /*mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mDrawerItems));*/
        mDrawerList.setAdapter(new ListViewArrayAdapter(this, new ArrayList(Arrays.asList(mDrawerItems))));
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
                foodAdapterList.add(new FoodItemAdapter(this, MainActivity.vgItems.get(i).getTitle(),MainActivity.vgItems.get(i).getDesc(), MainActivity.vgItems.get(i).getNames(),
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
        if(clear)
        {
            Cart.clear();
        }
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
        return super.onCreateOptionsMenu(menu);
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
            case R.id.refresh:
                status = new ProgressDialog(VistaActivity.this,ProgressDialog.STYLE_SPINNER);
                status.setMessage("Downloading and Parsing...");
                status.setTitle("Refreshing Menu Data");
                status.setIndeterminate(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            uiUpdate.post(new Runnable() {
                                @Override
                                public void run() {
                                    status.show();
                                }
                            });
                            Connection one = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1014&name=VG%20Cafe").timeout(10000);
                            Connection two = Jsoup.connect("http://dining.calpoly.edu/menus/?lid=1012&name=Sandwich%20Factory").timeout(10000);
                            Document docVg = one.get();
                            try
                            {
                                parseHtml = new Parser(MainActivity.vgItems);
                                parseHtml.parse(docVg,false);
                            }
                            catch(Exception e)
                            {
                                uiUpdate.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder onErrorConn= new AlertDialog.Builder(VistaActivity.this);
                                        onErrorConn.setTitle("Error Parsing!");
                                        onErrorConn.setMessage("There was an error parsing menu data. Please relaunch the app and try again. If the issue persists contact the developer.");
                                        onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int button) {
                                                finish();
                                            }
                                        });
                                        onErrorConn.show();
                                    }
                                });
                            }
                            Document docSand = two.get();
                            try
                            {
                                parseHtml = new Parser(MainActivity.sandItems);
                                parseHtml.parse(docSand,true);
                            }
                            catch(Exception e)
                            {
                                uiUpdate.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder onErrorConn= new AlertDialog.Builder(VistaActivity.this);
                                        onErrorConn.setTitle("Error Parsing!");
                                        onErrorConn.setMessage("There was an error parsing menu data. Please relaunch the app and try again. If the issue persists contact the developer.");
                                        onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int button) {
                                                finish();
                                            }
                                        });
                                        onErrorConn.show();
                                    }
                                });
                            }
                        }
                        catch (Exception e){
                            uiUpdate.post(new Runnable() {
                                @Override
                                public void run() {

                                    AlertDialog.Builder onErrorConn= new AlertDialog.Builder(VistaActivity.this);
                                    onErrorConn.setTitle("Error Connecting!");
                                    onErrorConn.setMessage("There was an error connecting to the website to download the menu. Please check your data connection and try again.");
                                    onErrorConn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                            finish();
                                        }
                                    });
                                    onErrorConn.show();
                                }
                            });
                        }
                        uiUpdate.post(new Runnable() {
                            @Override
                            public void run() {
                                status.dismiss();
                                vp.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
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
                mDrawerLayout.closeDrawer(mDrawerList);
                Thread threadHome = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        Intent intentHome = new Intent(mContext, MainActivity.class);
                        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intentHome);
                    }
                });
                threadHome.start();
            }
            else if(parent.getPositionForView(view)==1)
            {
                final Thread threadSF = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        final Intent intentSF = new Intent(mContext, SandwichActivity.class);
                        intentSF.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intentSF);
                        VistaActivity.mActivity.finish();
                    }
                });
                if(Cart.getCart().size()>0)
                {
                    AlertDialog.Builder notifyClear = new AlertDialog.Builder(mContext);
                    notifyClear.setTitle("Warning!");
                    notifyClear.setMessage("Your cart contains Vista Grande items. If you continue the cart will be cleared and these items will be removed. Do you want to continue?");
                    notifyClear.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            Cart.clear();
                            Toast.makeText(mContext,"Cart Cleared!",Toast.LENGTH_SHORT).show();
                            MainActivity.vgOrSand = 2;
                            mDrawerLayout.closeDrawer(mDrawerList);
                            SandwichActivity.clear = true;
                            threadSF.start();
                        }
                    });
                    notifyClear.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                        }
                    });
                    notifyClear.show();
                }
                else
                {
                    MainActivity.vgOrSand = 2;
                    mDrawerLayout.closeDrawer(mDrawerList);
                    SandwichActivity.clear = true;
                    threadSF.start();
                }

            }
            else if(parent.getPositionForView(view)==2)
            {
                MainActivity.vgOrSand = 1;
                mDrawerLayout.closeDrawer(mDrawerList);
            }
            else if(parent.getPositionForView(view)==3)
            {
                final Thread threadCP = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        final Intent intentCP = new Intent(mContext, CompleteorActivity.class);
                        intentCP.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        mContext.startActivity(intentCP);
                    }
                });
                mDrawerLayout.closeDrawer(mDrawerList);
                threadCP.start();
            }
            else
            {
                Toast.makeText(mContext, "Invalid Selection!", Toast.LENGTH_SHORT);
            }
        }
    }

    public class FoodItemAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;

        private ArrayList<String> names;
        private ArrayList<String> prices;
        private String title;
        private ArrayList<String> desc;

        public FoodItemAdapter(Context context, String title, ArrayList<String> desc, ArrayList<String> names, ArrayList<String> prices) {
            this.context = context;
            this.names = names;
            this.prices = prices;
            this.title = title;
            this.desc = desc;
        }

        public ArrayList<String> getNames() {
            return names;
        }

        public ArrayList<String> getPrices() {
            return prices;
        }

        public ArrayList<String> getDesc() {
            return desc;
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
            /*final AlertDialog.Builder onListClick= new AlertDialog.Builder(VistaActivity.this);
            onListClick.setCancelable(false);
            onListClick.setTitle("Add to Cart?");
            onListClick.setMessage("Would you like to add " + names.get(position).replace("@#$", "") + " to your cart? Price: " + "$" + prices.get(position));
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
                                Cart.add(names.get(position), Double.toString(np.getValue() * new Double(prices.get(position))));
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
            onListClick.setNeutralButton("Description", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    AlertDialog.Builder onDialogClick = new AlertDialog.Builder(VistaActivity.this);
                    onDialogClick.setTitle("Description");
                    onDialogClick.setMessage(desc.get(position));
                    onDialogClick.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {

                        }
                    });
                    onDialogClick.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            onListClick.show();
                        }
                    });
                    onDialogClick.show();
                }
            });
            onListClick.show();
        }*/
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
                        DecimalFormat twoDForm = new DecimalFormat("#.##");
                        Cart.add(names.get(position), twoDForm.format(np.getValue() * new Double(prices.get(position))));
                        StringBuilder sb = new StringBuilder(names.get(position) + " added to Cart!");
                        sb.replace(0,3,"");
                        Toast.makeText(mContext, sb, Toast.LENGTH_SHORT).show();
                        updateBalance();
                    }
                });
                onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                    }
                });
                onYes.show();
            }
            else {
                Cart.add(names.get(position), prices.get(position));
                updateBalance();
                Toast.makeText(mContext, names.get(position) + " added to Cart!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class VistaPagerAdapter extends FragmentPagerAdapter {

        //ArrayList<ItemSet> foodList;
        ArrayList<VistaActivity.FoodItemAdapter> foodAdapterList;

        public VistaPagerAdapter(FragmentManager fm, ArrayList<VistaActivity.FoodItemAdapter> foodAdapterList) {
            super(fm);
            //this.foodList = foodList;
            this.foodAdapterList = foodAdapterList;
        }

        @Override
        public Fragment getItem(int i) {
            return new MyFragment(i);
        }

        @Override
        public int getCount() {
            return foodAdapterList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return foodAdapterList.get(position).getTitle();
        }
        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        private class MyFragment extends Fragment {

            private int position;

            public MyFragment(int position) {
                this.position = position;
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                     Bundle savedInstanceState) {

                View rootView = inflater.inflate(R.layout.main_fragment, container, false);
                //Actual Fragment inflating happens in the next line.
                ListView lv = ((ListView) rootView.findViewById(R.id.list));
                lv.setAdapter(foodAdapterList.get(position));
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                        final int fPos = pos;
                        final AlertDialog.Builder onListClick= new AlertDialog.Builder(VistaActivity.this);
                        onListClick.setCancelable(false);
                        onListClick.setTitle("Add to Cart?");
                        onListClick.setMessage("Would you like to add " + foodAdapterList.get(position).getNames().get(pos).replace("@#$", "") + " to your cart? Price: " + "$" + foodAdapterList.get(position).getPrices().get(pos));
                        onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                //money = boundPrices.get(tempIndex);
                                if (foodAdapterList.get(position).getNames().get(fPos).contains("@#$")) {
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
                                            Cart.add(foodAdapterList.get(position).getNames().get(fPos), Double.toString(np.getValue() * new Double(foodAdapterList.get(position).getPrices().get(fPos))));
                                            updateBalance();
                                        }
                                    });
                                    onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                        }
                                    });
                                    onYes.show();
                                } else {
                                    Cart.add(foodAdapterList.get(position).getNames().get(position), foodAdapterList.get(position).getPrices().get(fPos));
                                    updateBalance();
                                }
                            }
                        });
                        onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                            }
                        });
                        onListClick.setNeutralButton("Description", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                AlertDialog.Builder onDialogClick = new AlertDialog.Builder(VistaActivity.this);
                                onDialogClick.setTitle("Description");
                                onDialogClick.setMessage(foodAdapterList.get(position).getDesc().get(fPos));
                                onDialogClick.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int button) {

                                    }
                                });
                                onDialogClick.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int button) {
                                        onListClick.show();
                                    }
                                });
                                onDialogClick.show();
                            }
                        });
                        onListClick.show();
                    }
                });
                return rootView;
            }
        }
    }
}
