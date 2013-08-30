/*package com.mustangexchange.polymeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SandwichPagerAdapter extends FragmentPagerAdapter {

    //ArrayList<ItemSet> foodList;
    static ArrayList<SandwichActivity.FoodItemAdapter> foodAdapterList;

    public SandwichPagerAdapter(FragmentManager fm, ArrayList<SandwichActivity.FoodItemAdapter> foodAdapterList) {
        super(fm);
        //this.foodList = foodList;
        this.foodAdapterList = foodAdapterList;
    }

    @Override
    public Fragment getItem(int i) {
        return new MyFragment(i);
    }

    @Override
    public int getCount() {
        return foodAdapterList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return foodAdapterList.get(position).getTitle();
    }
    @Override
    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    private static class MyFragment extends Fragment {

        private int position;

        public MyFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.main_fragment, container, false);
            //Actual Fragment inflating happens in the next line.
            ListView lv = ((ListView) rootView.findViewById(R.id.list));
            lv.setAdapter(foodAdapterList.get(position));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                    //Log.i(TAG, "onListItemClick: " + position);
                    final AlertDialog.Builder onListClick= new AlertDialog.Builder(SandwichActivity.this);
                    onListClick.setTitle("Add to Cart?");
                    onListClick.setMessage("Would you like to add " + names.get(position) + " to your cart? Price: " + "$" + prices.get(position));
                    onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            //MoneyTime.moneySpent = MoneyTime.moneySpent + (new Double(money));
                            Cart.add(names.get(position), prices.get(position));
                            updateBalance();
                        }
                    });
                    onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                        }
                    });
                    onListClick.setNeutralButton("Description", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int button) {
                            AlertDialog.Builder onDialogClick = new AlertDialog.Builder(SandwichActivity.this);
                            onDialogClick.setTitle("Description");
                            onDialogClick.setMessage(desc.get(position));
                            onDialogClick.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {

                                }
                            });
                            onDialogClick.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int button) {
                                    onListClick.show();
                                }
                            });
                            onDialogClick.show();
                        }
                    });
                    onListClick.show();

                }

            }
        });

            return rootView;
        }
    }
}*/