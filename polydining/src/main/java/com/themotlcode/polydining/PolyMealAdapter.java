package com.themotlcode.polydining;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PolyMealAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private PolyApplication app;

    public PolyMealAdapter(Activity activity, int resource, List<String> items)
    {
        super(activity, resource, items);
        this.activity = activity;
        this.app = (PolyApplication) activity.getApplication();
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
        tt.setText("  " + app.names.get(position));
        if(app.venues.get(app.names.get(position)).closeSoon())
        {
            tt.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(R.drawable.soon_dot),null,null,null);
        }
        else if(app.venues.get(app.names.get(position)).isOpen())
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