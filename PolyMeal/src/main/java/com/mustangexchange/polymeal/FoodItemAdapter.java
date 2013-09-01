package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;

public class FoodItemAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private Activity activity;
    private ArrayList<String> names;
    private ArrayList<String> prices;
    private String title;
    private ArrayList<String> desc;

    public FoodItemAdapter(Context context, String title, ArrayList<String> desc, ArrayList<String> names, ArrayList<String> prices)
    {
        this.context = context;
        activity = (Activity) context;
        this.names = names;
        this.prices = prices;
        this.title = title;
        this.desc = desc;
    }

    public ArrayList<String> getNames()
    {
        return names;
    }

    public ArrayList<String> getPrices()
    {
        return prices;
    }

    public ArrayList<String> getDesc()
    {
        return desc;
    }

    public String getTitle()
    {
        return title;
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

    public int getCount()
    {
        return names.size();
    }

    public Object getItem(int position)
    {
        return names.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        String temp;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        temp = names.get(position);
        if(temp.contains("@#$"))
        {
            tvName.setText(temp.substring(3));
        } else
        {
            tvName.setText(temp);
        }

        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
        if(prices.size() != 0)
        {
            tvPrice.setText("$" + prices.get(position));
        }

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
        if (names.get(position).contains("@#$")) {
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
                    Cart.add(names.get(position), Double.toString(np.getValue() * new Double(prices.get(position))));
                    StringBuilder sb = new StringBuilder(names.get(position) + " added to Cart!");
                    sb.replace(0,3,"");
                    Toast.makeText(activity, sb, Toast.LENGTH_SHORT).show();
                    updateBalance();
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
            Cart.add(names.get(position), prices.get(position));
            updateBalance();
            Toast.makeText(activity, names.get(position) + " added to Cart!", Toast.LENGTH_SHORT).show();
        }
    }
}