package com.mustangexchange.polymeal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.NumberPicker;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    ArrayList<FoodItemAdapter> foodAdapterList;
    Context context;
    Activity activity;

    public PagerAdapter(Context context, FragmentManager fm, ArrayList<FoodItemAdapter> foodAdapterList) {
        super(fm);
        this.context = context;
        activity = (Activity) context;
        this.foodAdapterList = foodAdapterList;
    }

    public void updateBalance()
    {
        String parentClass = context.getClass().getSimpleName();
        if(parentClass.equals("SandwichActivity"))
        {
            SandwichActivity.updateBalance();
        } else if(parentClass.equals("VistaActivity"))
        {
            VistaActivity.updateBalance();
        }
    }

    @Override
    public Fragment getItem(int i)
    {
        return new MyFragment(i);
    }

    @Override
    public int getCount()
    {
        return foodAdapterList.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return foodAdapterList.get(position).getTitle();
    }
    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    private class MyFragment extends Fragment {

        private int position;
        public MyFragment()
        {
            //empty contructor required for fragment subclasses
        }
        public MyFragment(int position) {
            this.position = position;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
           View rootView = inflater.inflate(R.layout.main_fragment, container, false);
            //Actual Fragment inflating happens in the next line.
            ListView lv = ((ListView) rootView.findViewById(R.id.list));
            lv.setAdapter(foodAdapterList.get(position));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> list, View view, int pos, long id) {
                    final int fPos = pos;
                    if(foodAdapterList.get(position).getPrices().size()>0)
                    {
                        final AlertDialog.Builder onListClick= new AlertDialog.Builder(activity);
                        onListClick.setCancelable(false);
                        onListClick.setTitle("Add to Cart?");
                        onListClick.setMessage("Would you like to add " + foodAdapterList.get(position).getNames().get(pos).replace("@#$", "") + " to your cart? Price: " + "$" + foodAdapterList.get(position).getPrices().get(pos));
                        onListClick.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                //money = boundPrices.get(tempIndex);
                                if (foodAdapterList.get(position).getNames().get(fPos).contains("@#$")) {
                                    AlertDialog.Builder onYes = new AlertDialog.Builder(activity);
                                    onYes.setTitle("How much?");
                                    onYes.setMessage("Estimated Number of Ounces: ");
                                    LayoutInflater inflater = activity.getLayoutInflater();
                                    View DialogView = inflater.inflate(R.layout.number_picker, null);
                                    final NumberPicker np = (NumberPicker) DialogView.findViewById(R.id.numberPicker);
                                    np.setMinValue(1);
                                    np.setMaxValue(50);
                                    np.setWrapSelectorWheel(false);
                                    np.setValue(1);
                                    onYes.setView(DialogView);
                                    onYes.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                            Cart.add(foodAdapterList.get(position).getNames().get(fPos), Double.toString(np.getValue() * new Double(foodAdapterList.get(position).getPrices().get(fPos))));
                                            updateBalance();
                                        }
                                    });
                                    onYes.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int button) {
                                        }
                                    });
                                    onYes.show();
                                } else {
                                    Cart.add(foodAdapterList.get(position).getNames().get(position), foodAdapterList.get(position).getPrices().get(fPos));
                                    updateBalance();
                                }
                            }
                        });
                        onListClick.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                            }
                        });
                        onListClick.setNeutralButton("Description", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int button) {
                                AlertDialog.Builder onDialogClick = new AlertDialog.Builder(activity);
                                onDialogClick.setTitle("Description");
                                onDialogClick.setMessage(foodAdapterList.get(position).getDesc().get(fPos));
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
                    else
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
                }
            });
            return rootView;
        }
    }
}