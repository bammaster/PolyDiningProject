package com.themotlcode.polydining;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//VenueListFragment inflates the ListFragment for the current page in the ViewPager

public class VenueListFragment extends ItemListFragment
{

    public int position;
    private VenueListPresenter presenter;

    public VenueListFragment(int position, VenueListPresenter presenter)
    {
        this.position = position;
        this.presenter = presenter;
        setPresenter(this, presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        presenter.setFragment(this);
        return inflater.inflate(R.layout.fragment_venue_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        ItemListAdapter itemListAdapter = new ItemListAdapter(this);
        itemListAdapter.setItems(presenter.items.getItems());
        setListAdapter(itemListAdapter);

    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

}