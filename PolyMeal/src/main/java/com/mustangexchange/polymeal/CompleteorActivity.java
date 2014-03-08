package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.mustangexchange.polymeal.Sorting.ItemNameComparator;
import com.mustangexchange.polymeal.Sorting.ItemPriceComparator;

import java.math.BigDecimal;
import java.util.Collections;

public class CompleteorActivity extends Activity {

    private ItemSet possibleItems;

    private ActionBar mActionBar;
    private static BigDecimal totalAmount;
    private static Context mContext;
    private static Activity activity;
    public ListView lv;
    public CompleteorItemAdapter lvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeor);

        mContext = this;
        activity = this;
        mActionBar = getActionBar();
        mActionBar.setTitle(Constants.activityTitle);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        possibleItems = new ItemSet();
        lvAdapter = new CompleteorItemAdapter(this, possibleItems);

        lv = (ListView) findViewById(R.id.listView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                final int fPos = pos;
                final AlertDialog.Builder onListClick= new AlertDialog.Builder(activity);
                onListClick.setCancelable(false);
                onListClick.setTitle("Add to Cart?");
                onListClick.setMessage("Would you like to add " + possibleItems.getItem(pos).getName() + " to your cart? Price: " + possibleItems.getItem(pos).getPriceString());
                onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        //money = boundPrices.get(tempIndex);
                        if (possibleItems.getItem(fPos).getOunces()) {
                            AlertDialog.Builder onYes = new AlertDialog.Builder(activity);
                            onYes.setTitle("How much?");
                            onYes.setMessage("Estimated Number of Ounces: ");
                            LayoutInflater inflater = activity.getLayoutInflater();
                            View DialogView = inflater.inflate(R.layout.number_picker, null);
                            final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
                            np.setMinValue(1);
                            np.setMaxValue(50);
                            np.setWrapSelectorWheel(false);
                            np.setValue(1);
                            onYes.setView(DialogView);
                            onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                                    possibleItems.getItem(fPos).setPrice(new BigDecimal(new BigDecimal(np.getValue())+"").multiply(possibleItems.getItem(fPos).getPrice()));
                                    Cart.add(possibleItems.getItem(fPos));
                                    updateBalance();
                                    updateList();
                                }
                            });
                            onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                                }
                            });
                            onYes.show();
                        } else {
                            Cart.add(possibleItems.getItem(fPos));
                            updateBalance();
                            updateList();
                        }
                    }
                });
                onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                    }
                });
                onListClick.setNeutralButton("Description", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        AlertDialog.Builder onDialogClick = new AlertDialog.Builder(activity);
                        onDialogClick.setTitle("Description");
                        onDialogClick.setMessage(possibleItems.getItem(fPos).getDescription());
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
    }

    public void checkLayout() {
        if((lv.getAdapter().getCount() == 0) && (lv.getAdapter() == lvAdapter)) {
            setContentView(R.layout.empty_completeor);
        }
    }

    public void onResume()
    {
        super.onResume();
        new calcCompleteor().execute("");
        updateBalance();
    }

    public void updateList() {
        new calcCompleteor().execute("");
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
        try
        {
            totalAmount = MoneyTime.calcTotalMoney();
            setSubtitleColor();
            mActionBar.setSubtitle("$" + totalAmount + " Remaining");
        }
        catch (NullPointerException e)
        {
            Intent intentHome = new Intent(mContext, PolyMealActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intentHome);
        }
    }

    public void updateSettings()
    {
        try
        {
            int sortMode;
            SharedPreferences defaultSP = PreferenceManager.getDefaultSharedPreferences(this);

            sortMode = Integer.valueOf(defaultSP.getString("sortMode", "0"));
            if(sortMode == 0)
            {
                Collections.sort(possibleItems.items, new ItemNameComparator());
            }
            else if(sortMode == 1)
            {
                Collections.sort(possibleItems.items, new ItemNameComparator());
                Collections.reverse(possibleItems.items);
            }
            else if(sortMode == 2)
            {
                Collections.sort(possibleItems.items, new ItemPriceComparator());
            }
            else
            {
                Collections.sort(possibleItems.items, new ItemPriceComparator());
                Collections.reverse(possibleItems.items);
            }
        }
        catch (NullPointerException e)
        {
            Intent intentHome = new Intent(mContext, PolyMealActivity.class);
            intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intentHome);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.completeor, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                final Intent intentVenue = new Intent(mContext, VenueActivity.class);
                intentVenue.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intentVenue);
                return true;
            case R.id.cart:
                Intent intent = new Intent(this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CompleteorItemAdapter extends BaseAdapter implements View.OnClickListener {
        private Context context;
        private ItemSet possibleItems;

        public CompleteorItemAdapter(Context context, ItemSet possibleItems) {
            this.context = context;
            this.possibleItems = possibleItems;
        }

        public ItemSet getPossibleItems() {
            return possibleItems;
        }

        public int getCount() {
            return possibleItems.size();
        }

        public void setItems(ItemSet items)
        {
            possibleItems = items;
            notifyDataSetChanged();
        }

        public void notifyData()
        {
            notifyDataSetChanged();
        }

        public Object getItem(int position) {
            return possibleItems.getItem(position);
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
            tvName.setText(possibleItems.getItem(position).getName());

            TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            tvPrice.setText(possibleItems.getItem(position).getPriceString());

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
            if (possibleItems.getItem(position).getOunces()) {
                AlertDialog.Builder onYes = new AlertDialog.Builder(activity);
                onYes.setTitle("How much?");
                onYes.setMessage("Estimated Number of Ounces: ");
                LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
                View DialogView = inflater.inflate(R.layout.number_picker, null);
                final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
                np.setMinValue(1);
                np.setMaxValue(50);
                np.setWrapSelectorWheel(false);
                np.setValue(1);
                onYes.setView(DialogView);
                onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        possibleItems.getItem(position).setPrice(new BigDecimal(new BigDecimal(np.getValue())+"").multiply(possibleItems.getItem(position).getPrice()));
                        Cart.add(possibleItems.getItem(position));
                        Toast.makeText(activity, possibleItems.getItem(position).getName() + " added to Cart!", Toast.LENGTH_SHORT).show();
                        updateBalance();
                        updateList();
                    }
                });
                onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                    }
                });
                onYes.show();
            }
            else
            {
                Cart.add(possibleItems.getItem(position));
                updateBalance();
                Toast.makeText(activity, possibleItems.getItem(position).getName() + " added to Cart!", Toast.LENGTH_SHORT).show();
                updateList();
            }
        }
    }

    private class calcCompleteor extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            possibleItems = new ItemSet();
            for(Venue venue : Constants.venues.values())
            {
                if(venue.getName().equals(Constants.activityTitle))
                for(ItemSet itemset : venue.venueItems)
                {
                    for(Item item : itemset.items)
                    {
                        if(item.getPrice().compareTo(MoneyTime.calcTotalMoney()) <= 0)
                        {
                            possibleItems.add(item);
                        }
                    }
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            updateSettings();
            lvAdapter.setItems(possibleItems);
            lvAdapter.notifyData();
            checkLayout();
        }

        @Override
        protected void onPreExecute() {
            lv.setAdapter(lvAdapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
