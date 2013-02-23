package com.quanturium.androcloud2.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.adapters.HomeAdapter;
import com.quanturium.androcloud2.adapters.MenuAdapter;

public class HomeFragment extends AbstractListFragment implements OnItemClickListener
{	
	private HomeAdapter			adapter;

	@Override
	protected FragmentInitParams init()
	{
		return new FragmentInitParams(R.layout.fragment_home, "Home", null, false, true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		adapter = new HomeAdapter(getActivity());
		configureListview(getListView());
		setListAdapter(adapter);
		setHasOptionsMenu(true);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentByTag("AddFilesDropdownFragment");
		
		if(fragment instanceof AddFileDropdownFragment)
			this.mCallbacks.onAddFileActionClicked();
	}

	private void configureListview(ListView listView)
	{
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		int value = 0;

		switch (position)
		{
			case HomeAdapter.ITEM_ALL:

				value = 0;

				break;

			case HomeAdapter.ITEM_IMAGES:
			case HomeAdapter.ITEM_BOOKMARKS:
			case HomeAdapter.ITEM_TEXT:
			case HomeAdapter.ITEM_ARCHIVES:
			case HomeAdapter.ITEM_AUDIO:
			case HomeAdapter.ITEM_VIDEO:
			case HomeAdapter.ITEM_OTHER:

				value = position - 1;

				break;

			case HomeAdapter.ITEM_TRASH:

				this.mCallbacks.onMenuItemSelected(MenuAdapter.ITEM_SHOW_TRASH);
				return;
		}

		this.mCallbacks.onHomeItemSelected(value);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menuItemAddFile:

				this.mCallbacks.onAddFileActionClicked();

				break;
		}

		return false;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.fragment_home, menu);

		super.onCreateOptionsMenu(menu, inflater);
	}
}
