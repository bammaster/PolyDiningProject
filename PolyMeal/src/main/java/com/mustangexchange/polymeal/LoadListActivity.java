package com.mustangexchange.polymeal;
/*
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class LoadListActivity extends Activity {

    private Handler uiUpdate = new Handler();
    private TextView moneyView;
    private ListView lv;
    private double price = 8.75;
    //to allow access to list view index in alert dialog
    private int tempIndex;
    private String money;
    public static String whichToLoad;
    private ArrayList<String> boundNames;
    private ArrayList<String> boundPrices;
    private int firstBound;
    private int secondBound;
    private Loader loader;
    private Time today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //loader = new Loader(MainActivity.names,MainActivity.prices);
        boundNames = loader.getBoundNames();
        boundPrices = loader.getBoundPrices();
        if(!(whichToLoad == null))
        {
            if(MainActivity.vgOrSand==1)
            {
                if(whichToLoad.equals("spec"))
                {
                    firstBound = MainActivity.names.indexOf("Specials");
                    secondBound = MainActivity.names.indexOf("Sandwich Stop");
                    loader.load(firstBound,secondBound);
                    if(boundNames.size()==0)
                    {
                        boundNames.add("No Specials Available!");
                        boundPrices.add("Dolla Dolla Bill Y'all");
                    }
                    getActionBar().setTitle("Specials");
                }
                else if(whichToLoad.equals("sand"))
                {
                    firstBound = MainActivity.names.indexOf("Sandwich Stop");
                    secondBound = MainActivity.names.indexOf("Chopstix");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Sandwich Stop");
                }
                else if(whichToLoad.equals("chop"))
                {
                    firstBound = MainActivity.names.indexOf("Chopstix");
                    secondBound = MainActivity.names.indexOf("Caliente");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Chopstix");
                }
                else if(whichToLoad.equals("cali"))
                {
                    firstBound = MainActivity.names.indexOf("Caliente");
                    secondBound = MainActivity.names.indexOf("Cagie Moons");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Caliente");
                }
                else if(whichToLoad.equals("cagie"))
                {
                    firstBound = MainActivity.names.indexOf("Cagie Moons");
                    secondBound = MainActivity.names.indexOf("Bella Pasta");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Cagie Moons");
                }
                else if(whichToLoad.equals("bella"))
                {
                    firstBound = MainActivity.names.indexOf("Bella Pasta");
                    secondBound = MainActivity.names.indexOf("Salad Bar");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Bella Pasta");
                }
                else if(whichToLoad.equals("misc"))
                {
                    firstBound = MainActivity.names.indexOf("Salad Bar");
                    secondBound = MainActivity.names.indexOf("Breakfast");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Miscellaneous");
                }
                else if(whichToLoad.equals("all"))
                {
                    loader.loadAll();
                    getActionBar().setTitle("Everything");
                }
            }
            else if(MainActivity.vgOrSand==2)
            {
                if(whichToLoad.equals("spec"))
                {
                    firstBound = MainActivity.names.indexOf("Specials");
                    secondBound = MainActivity.names.indexOf("Breakfast");
                    loader.load(firstBound,secondBound);
                    if(boundNames.size()==0)
                    {
                        boundNames.add("No Specials Available!");
                        boundPrices.add("Dolla Dolla Bill Y'all");
                    }
                    getActionBar().setTitle("Specials");
                }
                else if(whichToLoad.equals("breakfast"))
                {
                    firstBound = MainActivity.names.indexOf("Breakfast");
                    secondBound = MainActivity.names.indexOf("Specialty Sandwiches");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Breakfast");
                }
                else if(whichToLoad.equals("specSand"))
                {
                    firstBound = MainActivity.names.indexOf("Specialty Sandwiches");
                    secondBound = MainActivity.names.indexOf("Add Ons");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Specialty Sandwiches");
                }
                else if(whichToLoad.equals("addOn"))
                {
                    firstBound = MainActivity.names.indexOf("Add Ons");
                    secondBound = MainActivity.names.indexOf("Grab & Go");
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Add Ons");
                }
                else if(whichToLoad.equals("grab"))
                {
                    firstBound = MainActivity.names.indexOf("Grab & Go");
                    secondBound = MainActivity.names.size();
                    loader.load(firstBound,secondBound);
                    getActionBar().setTitle("Grab & Go");
                }
                else if(whichToLoad.equals("all"))
                {
                    loader.loadAll();
                    getActionBar().setTitle("Everything");
                }
            }
            else
            {
                Toast.makeText(this,"Error Loading Data!",Toast.LENGTH_LONG);
                startActivity(new Intent(this,VistaActivity.class));
            }
        }
        today = new Time(Time.getCurrentTimezone());
        moneyView = (TextView) findViewById(R.id.textView);
        moneyView.setText(MoneyTime.calcTotalMoney()+"");
        lv = (ListView)findViewById(R.id.listViewMain);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_text_view, boundNames);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v,int index, long id)
            {
                tempIndex = index;
                AlertDialog.Builder onListClick= new AlertDialog.Builder(LoadListActivity.this);
                onListClick.setTitle("Add to Cart?");
                onListClick.setMessage("Would you like to add " + boundNames.get(index) + " to your cart? Price: " + "$" + boundPrices.get(index));
                onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        money = boundPrices.get(tempIndex);
                        if (money.contains("per oz")) {
                            AlertDialog.Builder onYes = new AlertDialog.Builder(LoadListActivity.this);
                            onYes.setTitle("Soup or Salad?");
                            onYes.setMessage("Please select soup or salad: ");
                            onYes.setPositiveButton("Soup", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                                    money = money.substring(25, money.length() - 6);
                                    AlertDialog.Builder onSoup = new AlertDialog.Builder(LoadListActivity.this);
                                    onSoup.setTitle("Estimated Number of Ounces:");
                                    LayoutInflater inflater = LoadListActivity.this.getLayoutInflater();
                                    View DialogView = inflater.inflate(R.layout.number_picker, null);
                                    final NumberPicker np = (NumberPicker)DialogView.findViewById(R.id.numberPicker);
                                    np.setMinValue(1);
                                    np.setMaxValue(50);
                                    np.setWrapSelectorWheel(false);
                                    np.setValue(1);
                                    onSoup.setView(DialogView);
                                    onSoup.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                            MoneyTime.moneySpent = MoneyTime.moneySpent + (np.getValue()*new Double(money));
                                            Cart.add(boundNames.get(tempIndex),np.getValue() * new Double(money) + "");
                                            moneyView.setText("$" + MoneyTime.calcTotalMoney());
                                        }
                                    });
                                    onSoup.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                        }
                                    });
                                    onSoup.show();
                                }
                            });
                            onYes.setNegativeButton("Salad", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                                    money = money.substring(6, 11);
                                    AlertDialog.Builder onSalad = new AlertDialog.Builder(LoadListActivity.this);
                                    onSalad.setTitle("Estimated Number of Ounces:");
                                    LayoutInflater inflater = LoadListActivity.this.getLayoutInflater();
                                    View DialogView = inflater.inflate(R.layout.number_picker, null);
                                    final NumberPicker np = (NumberPicker)DialogView.findViewById(R.id.numberPicker);
                                    np.setMinValue(1);
                                    np.setMaxValue(50);
                                    np.setWrapSelectorWheel(false);
                                    np.setValue(1);
                                    onSalad.setView(DialogView);
                                    onSalad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                            MoneyTime.moneySpent = MoneyTime.moneySpent + (np.getValue()*new Double(money));
                                            Cart.add(boundNames.get(tempIndex),np.getValue() * new Double(money) + "");
                                            moneyView.setText("$" + MoneyTime.calcTotalMoney());
                                        }
                                    });
                                    onSalad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                        }
                                    });
                                    onSalad.show();
                                }
                            });
                            onYes.show();
                        }
                        else if (money.contains("plus tax"))
                        {
                            money = money.replace(" plus tax", "");
                            MoneyTime.moneySpent = MoneyTime.moneySpent + new Double(money) * .08 + new Double(money);
                            Cart.add(boundNames.get(tempIndex),new Double(money) * .08 + new Double(money)+"");
                        }
                        else
                        {
                            MoneyTime.moneySpent = MoneyTime.moneySpent + (new Double(money));
                            Cart.add(boundNames.get(tempIndex),boundPrices.get(tempIndex));
                        }
                        moneyView.setText("$" + MoneyTime.calcTotalMoney());
                        if (MoneyTime.calcTotalMoney() < 0) {
                            moneyView.setTextColor(Color.RED);
                        }
                        else
                        {
                            moneyView.setTextColor(Color.GREEN);
                        }
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

    public void onResume()
    {
        super.onResume();
        moneyView.setText("$"+MoneyTime.calcTotalMoney());
        if(MoneyTime.calcTotalMoney()<0)
        {
            moneyView.setTextColor(Color.RED);
        }
        else
        {
            moneyView.setTextColor(Color.GREEN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_cart:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            case android.R.id.home:
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
*/
