package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CompleteorActivity extends Activity {

    private ItemSet possibleItems;
    private Thread calcCompleteor;

    private ActionBar mActionBar;
    private static BigDecimal totalAmount;
    private static Context mContext;

    public ListView lv;
    public CompleteorItemAdapter lvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeor);

        possibleItems = new ItemSet("Completeor",new ArrayList<String>(),new ArrayList<String>());
        lv = (ListView) findViewById(R.id.listView);

        initializeThread(calcCompleteor);
        calcCompleteor.start();
        lvAdapter = new CompleteorItemAdapter(this, possibleItems);
        lv.setAdapter(lvAdapter);

        mContext = this;

        mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        updateBalance();
    }

    public void checkLayout() {
        if(lv.getAdapter().getCount() == 0) {
            setContentView(R.layout.empty_completeor);
        }
    }

    public void initializeThread(Thread thread) {
        calcCompleteor = new Thread(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.vgOrSand==1)
                {
                    for(int i = 0;i<MainActivity.vgItems.size();i++)
                    {
                        for(int j = 0;j<MainActivity.vgItems.get(i).getPrices().size();j++)
                        {
                            if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal(MainActivity.vgItems.get(i).getPrices().get(j)))>=0)
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
                            if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal(MainActivity.sandItems.get(i).getPrices().get(j)))>=0)
                            {
                                possibleItems.getNames().add(MainActivity.sandItems.get(i).getNames().get(j));
                                possibleItems.getPrices().add(MainActivity.sandItems.get(i).getPrices().get(j));
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.invalidate();
                        lvAdapter.notifyDataSetChanged();
                        checkLayout();
                    }
                });
            }
        });
    }

    public void updateList() {
        ItemSet updatedList;
        calcCompleteor = null;
        updatedList = lvAdapter.getPossibleItems();
        updatedList.clear();
        updatedList = possibleItems;
        initializeThread(calcCompleteor);
        calcCompleteor.start();
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
        inflater.inflate(R.menu.completeor, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(MainActivity.vgOrSand == 1)
                {
                    Intent intent = new Intent(this, VistaActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
                else
                {
                    Intent intent = new Intent(this, SandwichActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
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
            tvName.setText(possibleItems.getNames().get(position).replace("@#$",""));

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
            onListClick.setMessage("Would you like to add " + possibleItems.getNames().get(position).replace("@#$","") + " to your cart? Price: " + "$" + possibleItems.getPrices().get(position));
            onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    //money = boundPrices.get(tempIndex);
                    if (possibleItems.getNames().get(position).contains("@#$")) {
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
                                //Cart.add(possibleItems.getNames().get(position), (new BigDecimal(possibleItems.getPrices().get(position)).multiply(new BigDecimal(np.getValue() + ""))).setScale(2)+"");
                                Cart.add(possibleItems.getNames().get(position), Double.toString(np.getValue() * new Double(possibleItems.getPrices().get(position))));
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
                        //MoneyTime.moneySpent = MoneyTime.moneySpent + (new Double(money));
                        Cart.add(possibleItems.getNames().get(position), possibleItems.getPrices().get(position));
                        updateBalance();
                        updateList();
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
