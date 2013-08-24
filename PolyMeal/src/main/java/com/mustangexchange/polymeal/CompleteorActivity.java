package com.mustangexchange.polymeal;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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
        getMenuInflater().inflate(R.menu.completeor, menu);
        return true;
    }
    
}
