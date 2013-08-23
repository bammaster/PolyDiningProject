package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SandwichActivity extends Activity {

    private Parser parseHtmlSand;
    private ListView lv;
    private TextView moneyView;
    private String money;
    private int tempIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwich);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        parseHtmlSand = new Parser(MainActivity.names,MainActivity.prices);
        parseHtmlSand.parse(MainActivity.docSand);
        moneyView = (TextView) findViewById(R.id.moneyView);
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

    public void spec(View v)
    {
        LoadListActivity.whichToLoad = "spec";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void breakfast(View v)
    {
        LoadListActivity.whichToLoad = "breakfast";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void specSand(View v)
    {
        LoadListActivity.whichToLoad = "specSand";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void addOn(View v)
    {
        LoadListActivity.whichToLoad = "addOn";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void grab(View v)
    {
        LoadListActivity.whichToLoad = "grab";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void all(View v)
    {
        LoadListActivity.whichToLoad = "all";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sandwich, menu);
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
