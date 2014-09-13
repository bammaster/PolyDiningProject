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
    private ItemListAdapter itemListAdapter;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_completer, container, false);
        setupActivity();

        presenter = new MealCompleterPresenter(this);
        presenter.calcPossibleItems();
        setPresenter(this, presenter);

        itemListAdapter = new ItemListAdapter(this);
        itemListAdapter.setItems(presenter.items.getItems());
        setListAdapter(itemListAdapter);
        isCompleterEmpty();
        return v;
    }

    private void isCompleterEmpty()
    {
        if(itemListAdapter.getCount() <= 0)
        {
            v.findViewById(R.id.completer).setVisibility(View.GONE);
            v.findViewById(R.id.emptyCompleter).setVisibility(View.VISIBLE);
        }
    }

    private void setupActivity()
    {
        ((MainActivity) getActivity()).viewDrawer(false);
    }

    public void updateList()
    {

        ((ItemListAdapter) getListAdapter()).setItems(presenter.items.getItems());
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
        presenter.updateBalance();
        isCompleterEmpty();
    }
}
