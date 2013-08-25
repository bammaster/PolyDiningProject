package com.mustangexchange.polymeal;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CompleteorActivity extends Activity {

    ItemSet possibleItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeor);
        possibleItems = new ItemSet("Completeor",new ArrayList<String>(),new ArrayList<String>());
        if(MainActivity.vgOrSand==1)
        {
            for(int i = 0;i<MainActivity.vgItems.size();i++)
            {
                for(int j = 0;j<MainActivity.vgItems.get(i).getNames().size();j++)
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
                for(int j = 0;j<MainActivity.sandItems.get(i).getNames().size();j++)
                {
                    if(MoneyTime.calcTotalMoney().compareTo(new BigDecimal(MainActivity.sandItems.get(i).getPrices().get(j)))<=0)
                    {
                        possibleItems.getNames().add(MainActivity.vgItems.get(i).getNames().get(j));
                        possibleItems.getPrices().add(MainActivity.vgItems.get(i).getPrices().get(j));
                    }
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem money = menu.findItem(R.id.money_left);
        money.setTitle("$"+MoneyTime.calcTotalMoney()+"");
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.clear();
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem money = menu.findItem(R.id.money_left);
        money.setTitle("$" + MoneyTime.calcTotalMoney());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.money_left:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
