package com.quanturium.androcloud2.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.adapters.AddFilesAdapter;
import com.quanturium.androcloud2.listeners.FragmentListener;

public class AddFilesInlineFragment extends Fragment implements OnItemClickListener
{
	private GridView			gridView = null;
	private AddFilesAdapter		adapter;

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
		return inflater.inflate(R.layout.fragment_addfiles_inline, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		adapter = new AddFilesAdapter(getActivity(), AddFilesAdapter.STYLE_INLINE);
		gridView = (GridView) getView().findViewById(R.id.addFilesGridview);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		this.mCallbacks.onAddFilesItemSelected(arg2);
	}

	@Override
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
	}
}
