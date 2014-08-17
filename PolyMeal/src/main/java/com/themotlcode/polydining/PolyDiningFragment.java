package com.themotlcode.polydining;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PolyDiningFragment extends Fragment implements View.OnClickListener
{

    private TextView welcome;
    private TextView header;
    private Button plus;
    private Button meal;
    protected QustomDialogBuilder greeting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_dining, container, false);

        init(v);

        return v;
    }

    private void init(View v)
    {
        welcome = (TextView)v.findViewById(R.id.welcomeText);
        header = (TextView)v.findViewById(R.id.mainHeader);
        plus = (Button)v.findViewById(R.id.plusDollarsButton);
        meal = (Button)v.findViewById(R.id.polyMealButton);

        plus.setOnClickListener(this);
        meal.setOnClickListener(this);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        welcome.setTypeface(font);
        welcome.setAlpha(0);
        header.setAlpha(0);
        plus.setAlpha(0);
        meal.setAlpha(0);
        fadeIn();
        greeting = new QustomDialogBuilder(getActivity());
        greeting.setTitle("Greeting");
        greeting.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }

    private void fadeIn()
    {
        final int duration = 300;
        final int delay = 100;
        welcome.animate().alpha(1.0f).setStartDelay(delay).setDuration(duration).start();
        header.animate().alpha(1.0f).setStartDelay(duration/2+delay).setDuration(duration).start();
        plus.animate().alpha(1.0f).setStartDelay(duration + duration/2+delay).setDuration(duration).start();
        meal.animate().alpha(1.0f).setStartDelay(2 * duration + duration/2+delay).setDuration(duration).start();
    }

    public void polymeal(View v)
    {
        startActivity(new Intent(getActivity(),PolyMealActivity.class));
    }
    public void plusdollars(View v)
    {
        startActivity(new Intent(getActivity(),PlusDollarsActivity.class));
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.polyMealButton:
                polymeal(view);
                break;
            case R.id.plusDollarsButton:
                plusdollars(view);
                break;
            default:
                break;
        }
    }
}
