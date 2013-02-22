package com.quanturium.androcloud2.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.adapters.MenuAdapter;
import com.quanturium.androcloud2.listeners.FragmentListener;
import com.quanturium.androcloud2.tools.Prefs;

public class MenuFragment extends ListFragment
{
	private FragmentListener	mCallbacks	= null;
	private final static String	TAG			= "MenuFragment";
	private MenuAdapter			adapter;

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
		return inflater.inflate(R.layout.fragment_menu, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		adapter = new MenuAdapter(getActivity());
		setListAdapter(adapter);
		
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentByTag("AddFilesInlineFragment");
		
		if(fragment instanceof AddFilesInlineFragment)
			this.mCallbacks.onAddFilesMenuClicked();		
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
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
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
