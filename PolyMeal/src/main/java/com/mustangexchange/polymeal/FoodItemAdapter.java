package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.math.BigDecimal;

public class FoodItemAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ItemSet itemset;

    public FoodItemAdapter(Context context, ItemSet itemset)
    {
        this.context = context;
        activity = (Activity) context;
        this.itemset = itemset;
    }

    public void updateBalance()
    {
        String parentClass = context.getClass().getSimpleName();
        System.out.println(parentClass);
        if(parentClass.equals("SandwichActivity"))
        {
            SandwichActivity.updateBalance();
        } else if(parentClass.equals("VistaActivity"))
        {
            VistaActivity.updateBalance();
        }
    }

    public String getTitle()
    {
        return itemset.getTitle();
    }

    public int getCount()
    {
        return itemset.size();
    }

    public Object getItem(int position)
    {
        return itemset.getItem(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(itemset.getItem(position).getName());

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        tvPrice.setText(itemset.getItem(position).getPriceString());

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
        if (itemset.getItem(position).getOunces()) {
            AlertDialog.Builder onYes = new AlertDialog.Builder(activity);
            onYes.setTitle("How much?");
            onYes.setMessage("Estimated Number of Ounces: ");
            LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
            View DialogView = inflater.inflate(R.layout.number_picker, null);
            final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
            np.setMinValue(1);
            np.setMaxValue(50);
            np.setWrapSelectorWheel(false);
            np.setValue(1);
            onYes.setView(DialogView);
            onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                    Cart.add(new Item(itemset.getItem(position), itemset.getItem(position).getPrice().multiply(new BigDecimal(np.getValue()))));
                    Toast.makeText(activity, itemset.getItem(position).getName() + " added to Cart!", Toast.LENGTH_SHORT).show();
                    updateBalance();
                }
            });
            onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int button) {
                }
            });
            onYes.show();
        }
        else if(!itemset.getItem(position).getValid())
        {
            AlertDialog.Builder invalidItem = new AlertDialog.Builder(activity);
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
            Cart.add(itemset.getItem(position));
            updateBalance();
            Toast.makeText(context, itemset.getItem(position).getName() + " added to Cart!",Toast.LENGTH_SHORT).show();
        }
    }
}