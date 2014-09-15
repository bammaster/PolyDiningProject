package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class LoginFragment extends Fragment implements View.OnClickListener
{

    private TextView welcome;
    private EditText username;
    private EditText password;
    private CheckBox remember;
    private Button login;

    protected AlertDialog.Builder greeting;
    private LoginPresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        ((MainActivity) getActivity()).viewDrawer(false);

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        presenter = new LoginPresenter(this);
        setupActivity();
        init(v);

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }


    private void setupActivity()
    {
        ((MainActivity) getActivity()).viewDrawer(false);
    }

    @Override
    public void onClick(View view)
    {
        presenter.login(username.getText().toString(), password.getText().toString(), remember.isChecked(), login);
    }

    private void init(View v)
    {
        welcome = (TextView) v.findViewById(R.id.welcomeText);
        username = (EditText) v.findViewById(R.id.username);
        password = (EditText) v.findViewById(R.id.password);
        login = (Button) v.findViewById(R.id.login);
        remember = (CheckBox) v.findViewById(R.id.remember);
        login.setOnClickListener(this);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        welcome.setTypeface(font);
        welcome.setAlpha(0);
        username.setAlpha(0);
        password.setAlpha(0);
        login.setAlpha(0);
        fadeIn();

        greeting = new AlertDialog.Builder(getActivity());
        greeting.setTitle("Greeting");
        greeting.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        //greeting.show();
    }

    private void fadeIn()
    {
        final int duration = 300;
        final int delay = 100;
        welcome.animate().alpha(1.0f).setStartDelay(delay).setDuration(duration).start();
        username.animate().alpha(1.0f).setStartDelay(duration/2+delay).setDuration(duration).start();
        password.animate().alpha(1.0f).setStartDelay(duration + duration/2+delay).setDuration(duration).start();
        login.animate().alpha(1.0f).setStartDelay(2 * duration + duration/2+delay).setDuration(duration).start();
    }

    protected void returnThread(boolean b)
    {
        if(b)
        {
            Toast.makeText(getActivity(), "Login Successful!", Toast.LENGTH_LONG).show();
            MyAccountFragment maFragment = new MyAccountFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_layout, maFragment);
            getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            transaction.commit();
        }
        else
        {
            Toast.makeText(getActivity(), "Unable to login. Please try again.", Toast.LENGTH_LONG).show();
        }

    }
}