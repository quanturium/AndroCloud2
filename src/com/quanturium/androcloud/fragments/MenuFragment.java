package com.quanturium.androcloud.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quanturium.androcloud.FragmentInitParams;
import com.quanturium.androcloud.R;
import com.quanturium.androcloud.adapters.MenuAdapter;
import com.quanturium.androcloud.tools.Prefs;

public class MenuFragment extends AbstractListFragment
{
	private MenuAdapter			adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}
	
	@Override
	protected FragmentInitParams init()
	{
		return null;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		adapter = new MenuAdapter(getActivity());
		setListAdapter(adapter);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentByTag("AddFileInlineFragment");
		
		if(fragment instanceof AddFileInlineFragment)
			this.mCallbacks.onAddFileMenuClicked();		
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		int nbBackgroundUploadRunning = Prefs.getPreferences(getActivity()).getInt(Prefs.NUMBER_BACKGROUND_UPLOADS_RUNNING, 0);
		int nbBackgroundDownloadRunning = Prefs.getPreferences(getActivity()).getInt(Prefs.NUMBER_BACKGROUND_DOWNLOADS_RUNNING, 0);
		
		if(nbBackgroundDownloadRunning > 0 || nbBackgroundUploadRunning > 0)
			setBackgroundTasksRunning(new int[]{nbBackgroundUploadRunning,nbBackgroundDownloadRunning});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		super.onListItemClick(l, v, position, id);
		this.mCallbacks.onMenuItemSelected(position);
	}

	public void notifyDatasetChanged()
	{
		adapter.notifyDataSetChanged();
	}
	
	public void setBackgroundTasksRunning(int[] n)
	{
		if(adapter != null)
		{
			adapter.setBackgroundTasksRunning(n);
			adapter.notifyDataSetChanged();
		}			
	}
}
