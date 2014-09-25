package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;

import com.themotlcode.polydining.models.Cart;

public class CartFragment extends Fragment {
    private ListView lv;
    private View v;
    private CartPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = inflater.inflate(R.layout.fragment_cart, container, false);

        ((MainActivity) getActivity()).viewDrawer(false);

        presenter = new CartPresenter(this);
        presenter.updateBalance();

        init();
        isCartEmpty();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        isCartEmpty();
        presenter.updateCart();
        presenter.updateSettings();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.cart, menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCom:
                MealCompleterFragment mcFragment = new MealCompleterFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_layout, mcFragment).addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.clrCart:
                presenter.clearCart();
                isCartEmpty();
                Toast.makeText(getActivity(), "Cart Cleared!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void init() {
        this.setHasOptionsMenu(true);

        lv = (ListView) v.findViewById(R.id.listView);
        lv.setAdapter(presenter.getAdapter());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                final int fPos = pos;
                final AlertDialog.Builder onListClick = new AlertDialog.Builder(getActivity());
                onListClick.setCancelable(false);
                onListClick.setTitle("Remove to Cart?");
                onListClick.setMessage("Would you like to remove " + presenter.get(pos).getName() + " to your cart? \nPrice: " + presenter.get(pos).getPriceString());
                onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                        presenter.removeFromCart(fPos);
                    }
                });
                onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {
                    }
                });
                onListClick.create();
                Dialog d = onListClick.show();
                int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                TextView tv = (TextView) d.findViewById(textViewId);
                tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
            }
        });
    }

    protected void isCartEmpty() {
        if (lv.getAdapter().getCount() <= 0) {
            v.findViewById(R.id.cart).setVisibility(View.GONE);
            v.findViewById(R.id.emptyCart).setVisibility(View.VISIBLE);
        }
    }
}
