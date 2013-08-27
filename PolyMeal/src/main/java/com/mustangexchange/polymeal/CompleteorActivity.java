package com.mustangexchange.polymeal;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CompleteorActivity extends Activity {

    private ItemSet possibleItems;
    private Thread calcCompleteor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completeor);
        possibleItems = new ItemSet("Completeor",new ArrayList<String>(),new ArrayList<String>());
        calcCompleteor = new Thread(new Runnable() {
            @Override
            public void run() {
                if(MainActivity.vgOrSand==1)
                {
                    for(int i = 0;i<MainActivity.vgItems.size();i++)
                    {
                        for(int j = 0;j<MainActivity.vgItems.get(i).getPrices().size();j++)
                        {
                            Log.e("Blake",MainActivity.vgItems.get(i).getTitle()+" Size Prices: "+MainActivity.vgItems.get(i).getPrices().size()+ " Size Names: "+MainActivity.vgItems.get(i).getNames().size());
                            Log.e("Blake","Prices: "+MainActivity.vgItems.get(i).getPrices().get(j));
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
            }
        });
        calcCompleteor.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main, menu);
        //getMenuInflater().inflate(R.menu.main, menu);
        //MenuItem money = menu.findItem(R.id.money_left);
        //money.setTitle("$"+MoneyTime.calcTotalMoney()+"");
        return super.onCreateOptionsMenu(menu);
    }
}
