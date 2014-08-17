package com.mustangexchange.polymeal;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawerAdapter extends ArrayAdapter<String>
{

    Context context;
    ArrayList<String> strings;
    Typeface font;

    public DrawerAdapter(Context context, ArrayList<String> strings) {
        super(context, R.layout.drawer_list_item, strings);
        this.strings = strings;
        this.context = context;
        font = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    protected boolean callingClass(int position) {
        String parentClass = context.getClass().getSimpleName();

        if(parentClass.equals("PolyMealActivity") && position == 1) {
            return true;
        } else if(parentClass.equals("PlusDollarsActivity") && position == 2) {
            return true;
        } else if(parentClass.equals("TransactionActivity") && position == 3) {
            return true;
        }

        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);

        TextView drawerItem = (TextView) convertView.findViewById(R.id.text1);
        drawerItem.setText(strings.get(position));

        if(callingClass(position)) {
            drawerItem.setTypeface(font, Typeface.BOLD);
        }
        else {
            drawerItem.setTypeface(font);
        }

        return convertView;

    }
}