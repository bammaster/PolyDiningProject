package com.mustangexchange.polymeal;


import android.content.DialogInterface;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.mustangexchange.polymeal.models.Account;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

import java.math.BigDecimal;

public class PlusDollarsFragment extends Fragment
{
    private TextView name;
    private TextView expressHeader;
    private TextView express;
    private TextView plusHeader;
    private TextView plus;
    private TextView mealHeader;
    private TextView meal;
    private TextView budgetHeader;
    private TextView budget1;
    private TextView budget2;
    private TextView weeksLeft;
    private EditText username;
    private EditText password;
    private CheckBox remember;
    private LayoutInflater factory;
    private Days d;
    private Weeks w;

    private PlusDollarsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_plus_dollars, container, false);

        init(v);

        presenter = new PlusDollarsPresenter(this);

        if (presenter.init())
        {
            String temp = Statics.user.plusAsMoney();
            temp = temp.substring(1);
            name.setText(Statics.user.getName());
            plus.setText(Statics.user.plusAsMoney());
            express.setText(Statics.user.expressAsMoney());
            meal.setText(Statics.user.meals + "");
            budget1.setText("$" + new BigDecimal(temp).divide(new BigDecimal(d.getDays()), 2, BigDecimal.ROUND_HALF_DOWN) + "/day");
            budget2.setText("$" + new BigDecimal(temp).divide(new BigDecimal(w.getWeeks()), 2, BigDecimal.ROUND_HALF_DOWN) + "/week");
        }
        fadeIn();
        handleMusic();

        return v;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(Statics.user!= null)
        {
            if(name != null && Statics.user.getName()!= null) {
                setTextSizeName(Statics.user.getName(), name);
            }
        }
        if(Statics.endOfQuarter == null) {
            presenter.loadBudget();
        }
        DateTime start = new DateTime(Statics.startOfQuarter[0], Statics.startOfQuarter[1], Statics.startOfQuarter[2], 0, 0, 0, 0);
        DateTime end = new DateTime(Statics.endOfQuarter[0], Statics.endOfQuarter[1], Statics.endOfQuarter[2], 0, 0, 0, 0);
        d = Days.daysBetween(start, end);
        w = Weeks.weeksBetween(start, end);
        if(w.getWeeks() > 10)
        {
            weeksLeft.setText(Weeks.weeksBetween(DateTime.now(), start) + " " + getResources().getString(R.string.weeksstart));
        }
        else {
            weeksLeft.setText(w.getWeeks() + " " + getResources().getString(R.string.weeksleft));
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if(Statics.user != null) {
            if (Statics.user.remember) {
                presenter.saveAccount();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.plusdollars, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                presenter.loadData();
                getActivity().setProgressBarIndeterminateVisibility(true);
                return true;
            case R.id.login:
                handleLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Helps auto size text based on the length of the users name.
     * @param name The name of the person from the meal plan website.
     * @param nameText The view to set the parameter name to.
     */
    private void setTextSizeName(String name, TextView nameText)
    {
        if(name.length() > 10 && name.length() < 15)
        {
            nameText.setTextSize(60f);
        }
        else if(name.length() >= 15 && name.length() < 20 )
        {
            nameText.setTextSize(50f);
        }
        else if(name.length() >= 20)
        {
            nameText.setTextSize(40f);
        }
    }

    /**
     * Handles a user login by giving the user a dialog to enter their information into.
     * Also starts the thread to get the user data and updates the UI.
     */
    void handleLogin()
    {
        View loginView = factory.inflate(R.layout.login_dialog, null);
        username = (EditText) loginView.findViewById(R.id.username);
        password = (EditText) loginView.findViewById(R.id.password);
        remember = (CheckBox) loginView.findViewById(R.id.remember);
        QustomDialogBuilder login = new QustomDialogBuilder(getActivity());
        login.setTitleColor(Constants.APP_COLOR);
        login.setDividerColor(Constants.APP_COLOR);
        login.setTitle("Please login.");
        login.setCustomView(loginView, getActivity());
        login.setPositiveButton("Login", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id)
            {
                Statics.user = new Account(username.getText().toString(), password.getText().toString(), remember.isChecked());
                getActivity().setProgressBarIndeterminateVisibility(true);
                presenter.loadData();
                dialog.dismiss();
            }
        });
        login.show();
    }

    /**
     * Sets up the media player for the John Doe "easter egg".
     */
    private void handleMusic()
    {
        MediaPlayer mp = MediaPlayer.create(getActivity(), R.raw.john_doe_sample);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(name.getText().toString().equals("John Doe")) {
                            mediaPlayer.seekTo(0);
                            mediaPlayer.start();
                        }
                    }
                });
            }
        });
    }

    void returnThread(Boolean b)
    {
        if (!b)
        {
            Toast.makeText(getActivity(), "Unable to download budget data.", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(Statics.user == null)
            {
                Toast.makeText(getActivity(), "Unable to login. Please try again.", Toast.LENGTH_LONG).show();
                handleLogin();
            }
            else
            {
                setTextSizeName(Statics.user.getName(), name);
                name.setText(Statics.user.getName());
                if(remember != null)
                {

                    if(remember.isChecked())
                    {
                        Statics.user.remember = true;
                        presenter.saveAccount();
                    }
                    else
                    {
                        presenter.clearAccount();
                    }
                }
                plus.setText(Statics.user.plusAsMoney());
                express.setText(Statics.user.expressAsMoney());
                meal.setText(Statics.user.meals+"");

                DateTime start = new DateTime();
                DateTime end = new DateTime(Statics.endOfQuarter[0], Statics.endOfQuarter[1], Statics.endOfQuarter[2], 0, 0, 0, 0);
                Days d = Days.daysBetween(start, end);
                Weeks w = Weeks.weeksBetween(start, end);
                String temp = Statics.user.plusAsMoney();
                temp = temp.substring(1);

                budget1.setText("$" + new BigDecimal(temp).divide(new BigDecimal(d.getDays()), 2, BigDecimal.ROUND_HALF_DOWN) + "/day");
                budget2.setText("$" + new BigDecimal(temp).divide(new BigDecimal(w.getWeeks()), 2, BigDecimal.ROUND_HALF_DOWN) + "/week");
                presenter.storeDates();

            }
            getActivity().setProgressBarIndeterminateVisibility(false);
            //used when TransactionActivity calls this activity, closes immediately for a more seamless transition
            if(getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getInt("login") == 1)
                getActivity().finish();
            setAlphaToZero();
            fadeIn();
        }
    }


    private void init(View v)
    {
        this.setHasOptionsMenu(true);

        getViews(v);

        setAlphaToZero();
        factory = LayoutInflater.from(getActivity());
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        name.setTypeface(font);
    }

    /**
     * Gets access to the views on screen for manipulation and animation.
     */
    private void getViews(View v)
    {
        name = (TextView)v.findViewById(R.id.nameText);
        expressHeader = (TextView)v.findViewById(R.id.expHeader);
        express = (TextView)v.findViewById(R.id.expValue);
        plusHeader = (TextView)v.findViewById(R.id.plusHeader);
        plus = (TextView)v.findViewById(R.id.plusValue);
        mealHeader = (TextView)v.findViewById(R.id.mealHeader);
        meal = (TextView)v.findViewById(R.id.mealText);
        budgetHeader = (TextView) v.findViewById(R.id.budgetHeader);
        budget1 = (TextView) v.findViewById(R.id.budgetText1);
        budget2 = (TextView) v.findViewById(R.id.budgetText2);
        weeksLeft = (TextView) v.findViewById(R.id.textWeeks);
    }

    /**
     * Prepares the views on the screen for fading in.
     */
    private void setAlphaToZero()
    {
        name.setAlpha(0);
        expressHeader.setAlpha(0);
        express.setAlpha(0);
        plusHeader.setAlpha(0);
        plus.setAlpha(0);
        mealHeader.setAlpha(0);
        meal.setAlpha(0);
        budgetHeader.setAlpha(0);
        budget1.setAlpha(0);
        budget2.setAlpha(0);
    }

    /**
     * Fades in UI elements. To add an elements, add 1 to the multiple(#) of duration and follow
     * the format "# * duration/2 + delay" for the cascading effect.
     */
    private void fadeIn()
    {
        final int duration = 300;
        final int delay = 150;
        name.animate().alpha(1.0f).setStartDelay(delay).setDuration(duration).start();
        expressHeader.animate().alpha(1.0f).setStartDelay(duration/2+delay).setDuration(duration).start();
        express.animate().alpha(1.0f).setStartDelay(2*duration/2+delay).setDuration(duration).start();
        plusHeader.animate().alpha(1.0f).setStartDelay(3 * duration/2+delay).setDuration(duration).start();
        plus.animate().alpha(1.0f).setStartDelay(4 * duration/2+delay).setDuration(duration).start();
        mealHeader.animate().alpha(1.0f).setStartDelay(5 * duration/2+delay).setDuration(duration).start();
        meal.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
        budgetHeader.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
        budget1.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
        budget2.animate().alpha(1.0f).setStartDelay(6 * duration/2+delay).setDuration(duration).start();
    }
}
