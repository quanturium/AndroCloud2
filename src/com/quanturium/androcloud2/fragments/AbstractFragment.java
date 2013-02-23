package com.quanturium.androcloud2.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.listeners.FragmentListener;

public abstract class AbstractFragment extends Fragment
{
	protected FragmentListener	mCallbacks	= null;
	protected static String		TAG			= "AbstractFragment";
	private FragmentInitParams	params;

	public AbstractFragment()
	{
		TAG = getClass().getSimpleName();
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		params = init();
	}

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
		return inflater.inflate(params.layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		if (params != null)
		{
			((MainActivity) getActivity()).setActionBarNavigationModeList(params.isActionBarNavigationModeList);
			getActivity().getActionBar().setTitle(params.title);

			getActivity().getActionBar().setSubtitle(params.subtitle);
		}
	}

	@Override
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
	}

	protected abstract FragmentInitParams init();
}
