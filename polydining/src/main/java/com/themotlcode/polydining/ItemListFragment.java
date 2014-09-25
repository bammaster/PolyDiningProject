package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.Item;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jon on 9/12/14.
 */
public class ItemListFragment extends ListFragment implements View.OnClickListener {
    private Fragment fragment;
    private MealPresenter presenter;
    private ArrayList<Item> items;
    private PolyApplication app;

    public void setPresenter(Fragment fragment, MealPresenter presenter, PolyApplication app) {
        this.presenter = presenter;
        this.fragment = fragment;
        this.items = presenter.items.getItems();
        this.app = app;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final int fPos = position;
        final AlertDialog.Builder onListClick = new AlertDialog.Builder(getActivity());
        onListClick.setTitle("Add to Cart?");
        onListClick.setMessage("Would you like to add " + ((presenter.items.getItem(fPos))).getName() + " to your cart? Price: " + "$" + ((presenter.items.getItem(fPos))).getPrice());
        onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                if (((presenter.items.getItem(fPos))).getIsPricePerOunce()) {
                    AlertDialog.Builder onYes = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                    View DialogView = inflater.inflate(R.layout.number_picker, null);
                    final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
                    np.setMinValue(1);
                    np.setMaxValue(50);
                    np.setWrapSelectorWheel(false);
                    np.setValue(1);
                    onYes.setView(DialogView);
                    onYes.setTitle("How many ounces?");
                    onYes.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            app.cart.add(new Item(((presenter.items.getItem(fPos))), ((presenter.items.getItem(fPos))).getPrice().multiply(new BigDecimal(np.getValue()))));
                            presenter.updateBalance();
                            if (presenter instanceof MealCompleterPresenter) {
                                ((MealCompleterPresenter) presenter).calcPossibleItems();
                                ((MealCompleterFragment) fragment).updateList();
                            }

                        }
                    });
                    onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                        }
                    });
                    onYes.create();
                    Dialog d = onYes.show();
                    int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                    View divider = d.findViewById(dividerId);
                    divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                    int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                    TextView tv = (TextView) d.findViewById(textViewId);
                    tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
                } else {
                    app.cart.add(((presenter.items.getItem(fPos))));
                    presenter.updateBalance();
                    if (presenter instanceof MealCompleterPresenter) {
                        ((MealCompleterPresenter) presenter).calcPossibleItems();
                        ((MealCompleterFragment) fragment).updateList();
                    }
                }
            }
        });
        onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
            }
        });
        onListClick.setNeutralButton("Description", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                AlertDialog.Builder onDialogClick = new AlertDialog.Builder(getActivity());
                onDialogClick.setTitle("Description");
                onDialogClick.setMessage((((presenter.items.getItem(fPos))).getDescription()));
                onDialogClick.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {

                    }
                });
                onDialogClick.create();
                Dialog d = onDialogClick.show();
                int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
                View divider = d.findViewById(dividerId);
                divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

                int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
                TextView tv = (TextView) d.findViewById(textViewId);
                tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
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

    @Override
    public void onClick(View view) {
        final int position = (Integer) view.getTag();
        if (items.get(position).getIsPricePerOunce()) {
            AlertDialog.Builder onYes = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
            View DialogView = inflater.inflate(R.layout.number_picker, null);
            final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
            np.setMinValue(1);
            np.setMaxValue(50);
            np.setWrapSelectorWheel(false);
            np.setValue(1);
            onYes.setView(DialogView);
            onYes.setTitle("How many ounces?");
            onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {

                    app.cart.add(new Item(items.get(position), items.get(position).getPrice().multiply(new BigDecimal(np.getValue()))));
                    Toast.makeText(fragment.getActivity(), items.get(position).getName() + " added to Cart!", Toast.LENGTH_SHORT).show();
                    presenter.updateBalance();
                    if (presenter instanceof MealCompleterPresenter) {
                        ((MealCompleterPresenter) presenter).calcPossibleItems();
                        ((MealCompleterFragment) fragment).updateList();
                    }
                }
            });
            onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                }
            });
            onYes.create();
            Dialog d = onYes.show();
            int dividerId = d.getContext().getResources().getIdentifier("titleDivider", "id", "android");
            View divider = d.findViewById(dividerId);
            divider.setBackgroundColor(Color.parseColor(PolyApplication.APP_COLOR));

            int textViewId = d.getContext().getResources().getIdentifier("alertTitle", "id", "android");
            TextView tv = (TextView) d.findViewById(textViewId);
            tv.setTextColor(Color.parseColor(PolyApplication.APP_COLOR));
        } else {
            app.cart.add(items.get(position));
            presenter.updateBalance();
            if (presenter instanceof MealCompleterPresenter) {
                ((MealCompleterPresenter) presenter).calcPossibleItems();
                ((MealCompleterFragment) fragment).updateList();
            }
            Toast.makeText(fragment.getActivity(), items.get(position).getName() + " added to Cart!", Toast.LENGTH_SHORT).show();
        }
    }
}
