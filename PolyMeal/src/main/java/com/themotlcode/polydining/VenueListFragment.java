package com.themotlcode.polydining;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.NumberPicker;
import com.themotlcode.polydining.models.Cart;
import com.themotlcode.polydining.models.Item;

import java.math.BigDecimal;

//VenueListFragment inflates the ListFragment for the current page in the ViewPager

public class VenueListFragment extends ListFragment
{

    public int position;
    private VenueListPresenter presenter;

    public VenueListFragment(int position, VenueListPresenter presenter)
    {
        this.position = position;
        this.presenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_venue_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new VenueListAdapter(this, presenter, presenter.items));
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
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
            AlertDialog.Builder onYes = new AlertDialog.Builder(getActivity());
            onYes.setTitle("How much?");
            onYes.setMessage("Estimated Number of Ounces: ");
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View DialogView = inflater.inflate(R.layout.number_picker, null);
            final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
            np.setMinValue(1);
            np.setMaxValue(50);
            np.setWrapSelectorWheel(false);
            np.setValue(1);
            onYes.setView(DialogView);
            onYes.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                Cart.add(new Item(((presenter.items.getItem(fPos))), ((presenter.items.getItem(fPos))).getPrice().multiply(new BigDecimal(np.getValue()))));
                    presenter.updateBalance();
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
                Cart.add(((presenter.items.getItem(fPos))));
                presenter.updateBalance();
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
}