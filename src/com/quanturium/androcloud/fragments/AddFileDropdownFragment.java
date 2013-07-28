package com.quanturium.androcloud.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.quanturium.androcloud.FragmentInitParams;
import com.quanturium.androcloud.R;
import com.quanturium.androcloud.adapters.AddFileAdapter;

public class AddFileDropdownFragment extends AbstractListFragment implements OnItemClickListener
{
	private AddFileAdapter	adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_addfile_dropdown, container, false);
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

		adapter = new AddFileAdapter(getActivity(), AddFileAdapter.STYLE_DROPDOWN);
		getListView().setOnItemClickListener(this);
		setListAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		this.mCallbacks.onAddFileItemSelected(arg2);
	}
}
