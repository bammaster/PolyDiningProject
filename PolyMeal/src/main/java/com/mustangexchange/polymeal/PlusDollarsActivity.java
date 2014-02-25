package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Blake on 2/20/14.
 */
public class PlusDollarsActivity extends Activity {
    private Account account;
    private Thread update;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.plus_dollars_activity);
        TextView name = (TextView)findViewById(R.id.nameText);
        TextView plus = (TextView)findViewById(R.id.plusValue);
        TextView express = (TextView)findViewById(R.id.expValue);
        TextView meal = (TextView)findViewById(R.id.mealText);
        LayoutInflater factory = LayoutInflater.from(this);
        View loginView = factory.inflate(R.layout.login_dialog, null);
        EditText username = (EditText)loginView.findViewById(R.id.username);
        EditText password = (EditText)loginView.findViewById(R.id.password);
        CheckBox remember = (CheckBox)loginView.findViewById(R.id.remember);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        name.setTypeface(font);
        account = account.loadAccount(Constants.FILENAME, this);
        //If the loaded account does not exist or the user said not to remember.
        if(account == null || !account.remember)
        {
            handleLogin(loginView, name, username, password, remember, plus);
        }
        else if(account.remember)
        {
            name.setText(account.name);
        }
        update = buildThread(name,remember);
    }
    private void setTextSizeName(String name,TextView nameText)
    {
        if(name.length() > 10 && name.length() < 15 || nameText.getLineCount() == 2)
        {
            nameText.setTextSize(60f);
        }
        else if(name.length() >= 15 && name.length() < 20 || nameText.getLineCount() == 2)
        {
            nameText.setTextSize(50f);
        }
        else if(name.length() >= 20 || nameText.getLineCount() == 2)
        {
            nameText.setTextSize(40f);
        }
    }

    /**
     * Handles a user login by giving the user a dialog to enter their information into.
     * Also starts the thread to get the user data and updates the UI.
     * @param loginView The view used on the laert dialog.
     * @param name The name text view at the top of the screen.
     * @param username The username the user enters into the dialog.
     * @param password The password the user enters into the dialog.
     * @param remember Whether or not the user checked the box to remember details.
     * @param plus
     */
    private void handleLogin(final View loginView, final TextView name, final EditText username,
                             final EditText password, final CheckBox remember, final TextView plus)
    {
        AlertDialog.Builder login = new AlertDialog.Builder(this);
        login.setTitle("Please login.");
        login.setCancelable(false);
        login.setView(loginView);
        login.setPositiveButton("Login", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id)
            {
                account = new Account(username.getText().toString(), password.getText().toString(), remember.isChecked());

            }
        });
        login.show();
    }

    /**
     * Builds the thread used to get all of the plus dollars data.
     * @param name Name view to update with the name that is found.
     * @param remember Wether or not to remeber the account.
     * @return A copy of the Thread.
     */
    private Thread buildThread(final TextView name, final CheckBox remember)
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                GetAllTheThings getPlusData = new GetAllTheThings(account);
                account = getPlusData.getTheThings();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("Blake","Name: " + account.name);
                        setTextSizeName(account.name, name);
                        name.setText(account.name);
                        if(remember.isChecked() && account != null)
                        {
                            account.remember = true;
                            account.saveAccount(Constants.FILENAME, PlusDollarsActivity.this);
                        }
                        //plus.setText(account.plusDollars.toString());
                        //express.setText(account.campusExpress.toString());
                        //meal.setText(account.meals);
                        setProgressBarIndeterminateVisibility(false);
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.plusdollars, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                new Thread(update).start();
                setProgressBarIndeterminateVisibility(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
