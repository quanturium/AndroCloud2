package com.quanturium.androcloud2.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.adapters.HomeAdapter;
import com.quanturium.androcloud2.adapters.MenuAdapter;
import com.quanturium.androcloud2.listeners.FragmentListener;

public class HomeFragment extends ListFragment implements OnItemClickListener
{
	private FragmentListener	mCallbacks	= null;
	private final static String	TAG			= "HomeFragment";

	private HomeAdapter			adapter;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (!(activity instanceof FragmentListener))
			throw new IllegalStateException("Activity must implement fragment's callbacks.");

		this.mCallbacks = (FragmentListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		((MainActivity) getActivity()).setActionBarNavigationModeList(false);
		getActivity().getActionBar().setTitle("Home");

		adapter = new HomeAdapter(getActivity());
		configureListview(getListView());
		setListAdapter(adapter);
		setHasOptionsMenu(true);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentByTag("AddFilesDropdownFragment");
		
		if(fragment instanceof AddFilesDropdownFragment)
			this.mCallbacks.onAddFilesActionClicked();
	}

	@Override
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
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
			case R.id.menuItemAddFiles:

				this.mCallbacks.onAddFilesActionClicked();

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
