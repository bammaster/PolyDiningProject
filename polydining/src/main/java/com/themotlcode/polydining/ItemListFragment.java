package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.Item;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by jon on 9/12/14.
 */
public class ItemListFragment extends ListFragment implements View.OnClickListener
{
    private Fragment fragment;
    private MealPresenter presenter;
    private ArrayList<Item> items;

    public void setPresenter(Fragment fragment, MealPresenter presenter)
    {
        this.presenter = presenter;
        this.fragment = fragment;
        this.items = presenter.items.getItems();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        final int fPos = position;
        final QustomDialogBuilder onListClick = new QustomDialogBuilder(getActivity());
        onListClick.setDividerColor(PolyApplication.APP_COLOR);
        onListClick.setTitleColor(PolyApplication.APP_COLOR);
        onListClick.setTitle("Add to Cart?");
        onListClick.setMessage("Would you like to add " + ((presenter.items.getItem(fPos))).getName() + " to your cart? Price: " + "$" + ((presenter.items.getItem(fPos))).getPrice());
        onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int button) {
                if(((presenter.items.getItem(fPos))).getIsPricePerOunce())
                {
                    QustomDialogBuilder onYes = new QustomDialogBuilder(getActivity());
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);                    View DialogView = inflater.inflate(R.layout.number_picker, null);
                    final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
                    np.setMinValue(1);
                    np.setMaxValue(50);
                    np.setWrapSelectorWheel(false);
                    np.setValue(1);
                    onYes.setCustomView(DialogView, getActivity());
                    onYes.setDividerColor(PolyApplication.APP_COLOR);
                    onYes.setTitleColor(PolyApplication.APP_COLOR);
                    onYes.setTitle("How many ounces?");
                    onYes.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            Cart.add(new Item(((presenter.items.getItem(fPos))), ((presenter.items.getItem(fPos))).getPrice().multiply(new BigDecimal(np.getValue()))));
                            presenter.updateBalance();
                            if(presenter instanceof MealCompleterPresenter)
                            {
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
                    onYes.show();
                }
                else
                {
                    Cart.add(((presenter.items.getItem(fPos))));
                    presenter.updateBalance();
                    if(presenter instanceof MealCompleterPresenter)
                    {
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
                QustomDialogBuilder onDialogClick = new QustomDialogBuilder(getActivity());
                onDialogClick.setTitleColor(PolyApplication.APP_COLOR);
                onDialogClick.setDividerColor(PolyApplication.APP_COLOR);
                onDialogClick.setTitle("Description");
                onDialogClick.setMessage((((presenter.items.getItem(fPos))).getDescription()));
                onDialogClick.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int button) {

                    }
                });
                onDialogClick.show();
            }
        });
        onListClick.show();
    }

    @Override
    public void onClick(View view)
    {
        final int position = (Integer) view.getTag();
        if (items.get(position).getIsPricePerOunce()) {
            QustomDialogBuilder onYes = new QustomDialogBuilder(getActivity());
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);                    View DialogView = inflater.inflate(R.layout.number_picker, null);
            final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
            np.setMinValue(1);
            np.setMaxValue(50);
            np.setWrapSelectorWheel(false);
            np.setValue(1);
            onYes.setCustomView(DialogView, getActivity());
            onYes.setDividerColor(PolyApplication.APP_COLOR);
            onYes.setTitleColor(PolyApplication.APP_COLOR);
            onYes.setTitle("How many ounces?");
            onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {

                    Cart.add(new Item(items.get(position), items.get(position).getPrice().multiply(new BigDecimal(np.getValue()))));
                    Toast.makeText(fragment.getActivity(), items.get(position).getName() + " added to Cart!", Toast.LENGTH_SHORT).show();
                    presenter.updateBalance();
                    if(presenter instanceof MealCompleterPresenter)
                    {
                        ((MealCompleterPresenter) presenter).calcPossibleItems();
                        ((MealCompleterFragment) fragment).updateList();
                    }
                }
            });
            onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                }
            });
            onYes.show();
        }
        else
        {
            Cart.add(items.get(position));
            presenter.updateBalance();
            if(presenter instanceof MealCompleterPresenter)
            {
                ((MealCompleterPresenter) presenter).calcPossibleItems();
                ((MealCompleterFragment) fragment).updateList();
            }
            Toast.makeText(fragment.getActivity(), items.get(position).getName() + " added to Cart!",Toast.LENGTH_SHORT).show();
        }
    }
}
