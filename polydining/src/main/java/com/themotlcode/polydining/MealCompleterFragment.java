package com.themotlcode.polydining;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by jon on 9/12/14.
 */
public class MealCompleterFragment extends ItemListFragment
{

    private MealCompleterPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_venue_list, container, false);
        setupActivity();

        presenter = new MealCompleterPresenter(this);
        presenter.calcPossibleItems();
        setListAdapter(new ItemListAdapter(this, presenter, presenter.items));
        return v;
    }

    private void setupActivity()
    {
        ((MainActivity) getActivity()).viewDrawer(false);
    }

    public void updateList()
    {
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        presenter.updateBalance();
    }
}
