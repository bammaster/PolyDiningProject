package com.mustangexchange.polymeal;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class VistaActivity extends Activity {


    private int minutes;
    private TextView moneyView;
    private Parser parseHtmlVg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        parseHtmlVg = new Parser(MainActivity.names,MainActivity.prices);
        parseHtmlVg.parse(MainActivity.docVg);
        moneyView = (TextView) findViewById(R.id.money);
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

    public void sand(View v)
    {
        LoadListActivity.whichToLoad = "sand";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void chop(View v)
    {
        LoadListActivity.whichToLoad = "chop";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void cali(View v)
    {
        LoadListActivity.whichToLoad = "cali";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void cagie(View v)
    {
        LoadListActivity.whichToLoad = "cagie";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void bella(View v)
    {
        LoadListActivity.whichToLoad = "bella";
        startActivity(new Intent(this,LoadListActivity.class));
    }

    public void misc(View v)
    {
        LoadListActivity.whichToLoad = "misc";
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
        getMenuInflater().inflate(R.menu.restaurant, menu);
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
