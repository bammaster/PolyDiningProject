package com.themotlcode.polydining;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.TextView;
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
    private Days d;
    private Weeks w;

    private PolyApplication app;
    private PlusDollarsPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_plus_dollars, container, false);
        app = (PolyApplication) getActivity().getApplication();
        presenter = new PlusDollarsPresenter(this);
        init(v);
        handleMusic();

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu,inflater);
        menu.clear();
        inflater.inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                presenter.refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        System.out.println("onDestroyView");
        getActivity().getActionBar().setSubtitle(null);
    }

    private void init(View v)
    {
        this.setHasOptionsMenu(true);

        getViews(v);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        name.setTypeface(font);

        loadData();
    }

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

    protected void loadData()
    {
        setAlphaToZero();

        //setTextSizeName(app.user.getName(), name);

        DateTime start = new DateTime(app.startOfQuarter[0], app.startOfQuarter[1], app.startOfQuarter[2], 0, 0, 0, 0);
        DateTime end = new DateTime(app.endOfQuarter[0], app.endOfQuarter[1], app.endOfQuarter[2], 0, 0, 0, 0);
        d = Days.daysBetween(start, end);
        w = Weeks.weeksBetween(start, end);
        int titleId = Resources.getSystem().getIdentifier("action_bar_subtitle", "id", "android");
        TextView yourTextView = (TextView) getActivity().findViewById(titleId);
        yourTextView.setTextColor(Color.WHITE);
        if(w.getWeeks() > 10)
        {
            getActivity().getActionBar().setSubtitle(Weeks.weeksBetween(DateTime.now(), start) + " " + getResources().getString(R.string.weeksstart));
        }
        else {
            getActivity().getActionBar().setSubtitle(w.getWeeks() + " " + getResources().getString(R.string.weeksleft));
        }

        login();
        fadeIn();
    }

    private void login()
    {
        String temp = app.user.plusAsMoney();
        temp = temp.substring(1);
        name.setText(app.user.getName());
        plus.setText(app.user.plusAsMoney());
        express.setText(app.user.expressAsMoney());
        meal.setText(app.user.getMeals() + "");
        budget1.setText("$" + new BigDecimal(temp).divide(new BigDecimal(d.getDays()), 2, BigDecimal.ROUND_HALF_DOWN) + "/day");
        budget2.setText("$" + new BigDecimal(temp).divide(new BigDecimal(w.getWeeks()), 2, BigDecimal.ROUND_HALF_DOWN) + "/week");
    }

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
}