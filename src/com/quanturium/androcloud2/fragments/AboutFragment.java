package com.quanturium.androcloud2.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.listeners.FragmentListener;

public class AboutFragment extends Fragment
{
	private FragmentListener	mCallbacks	= null;
	private final static String	TAG			= "AboutFragment";

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
		return inflater.inflate(R.layout.fragment_about, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);		
		((MainActivity)getActivity()).setActionBarNavigationModeList(false);
		getActivity().getActionBar().setTitle("About");
	}
	
	@Override
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
	}
}
