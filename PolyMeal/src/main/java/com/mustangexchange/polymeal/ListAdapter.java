package com.mustangexchange.polymeal;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {

    private Activity activity;

    public ListAdapter(Activity activity, int resource, List<String> items)
    {
        super(activity, resource, items);
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.polymeal_list_item, parent, false);
        }
        TextView tt = (TextView) convertView.findViewById(R.id.polymealListItem);
        tt.setText("  " + Statics.names.get(position));
        if(Statics.venues.get(Statics.names.get(position)).closeSoon())
        {
            tt.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.soon_dot),null,null,null);
        }
        else if(Statics.venues.get(Statics.names.get(position)).isOpen())
        {
            tt.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.open_dot),null,null,null);
        }
        else
        {
            tt.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.close_dot),null,null,null);
        }
        return convertView;

    }
    public void notifyData()
    {
        this.notifyDataSetChanged();
    }
}