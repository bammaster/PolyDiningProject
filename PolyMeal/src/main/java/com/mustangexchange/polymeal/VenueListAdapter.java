package com.mustangexchange.polymeal;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mustangexchange.polymeal.models.Cart;
import com.mustangexchange.polymeal.models.Item;
import com.mustangexchange.polymeal.models.ItemSet;

import java.math.BigDecimal;
import java.util.ArrayList;

//VenueListAdapter inflates the rows for each Item in the ItemSet for the current page in the ViewPager

public class VenueListAdapter extends BaseAdapter implements View.OnClickListener {
    private static VenueListFragment fragment;
    private VenueListPresenter presenter;
    private ArrayList<Item> items;

    public VenueListAdapter(Fragment fragment, VenueListPresenter presenter, ItemSet itemSet)
    {
        this.fragment = (VenueListFragment) fragment;
        this.items = itemSet.getItems();
        this.presenter = presenter;
    }

    public int getCount()
    {
        return items.size();
    }

    public Object getItem(int position)
    {
        return items.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(items.get(position).getName());

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        tvPrice.setText(items.get(position).getPriceString());

        //Set the onClick Listener on this button
        ImageButton btnAdd = (ImageButton) convertView.findViewById(R.id.btn_add);
        btnAdd.setFocusableInTouchMode(false);
        btnAdd.setFocusable(false);
        btnAdd.setOnClickListener(this);
        btnAdd.setTag(new Integer(position));
        return convertView;
    }

    @Override
    public void onClick(View view)
    {
        final int position = (Integer) view.getTag();
        if (items.get(position).getOunces()) {
            QustomDialogBuilder onYes = new QustomDialogBuilder(fragment.getActivity());
            onYes.setDividerColor(Constants.APP_COLOR);
            onYes.setTitleColor(Constants.APP_COLOR);
            onYes.setTitle("How much?");
            onYes.setMessage("Estimated Number of Ounces: ");
            LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
            View DialogView = inflater.inflate(R.layout.number_picker, null);
            final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
            np.setMinValue(1);
            np.setMaxValue(50);
            np.setWrapSelectorWheel(false);
            np.setValue(1);
            onYes.setView(DialogView);
            onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {

                    Cart.add(new Item(items.get(position), items.get(position).getPrice().multiply(new BigDecimal(np.getValue()))));
                    Toast.makeText(fragment.getActivity(), items.get(position).getName() + " added to Cart!", Toast.LENGTH_SHORT).show();
                    presenter.updateBalance();
                }
            });
            onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                }
            });
            onYes.show();
        }
        else if(!items.get(position).getValid())
        {
            QustomDialogBuilder invalidItem = new QustomDialogBuilder(fragment.getActivity());
            invalidItem.setDividerColor(Constants.APP_COLOR);
            invalidItem.setDividerColor(Constants.APP_COLOR);
            invalidItem.setTitle("Invalid Item!");
            invalidItem.setMessage("No price data was found for this item. It was not added to your cart.");
            invalidItem.setNeutralButton("OK",new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int button){

             }
             });
             invalidItem.show();
        }
        else
        {
            Cart.add(items.get(position));
            presenter.updateBalance();
            Toast.makeText(fragment.getActivity(), items.get(position).getName() + " added to Cart!",Toast.LENGTH_SHORT).show();
        }
    }
}