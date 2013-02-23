package com.quanturium.androcloud2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.adapters.AddFileAdapter;

public class AddFileInlineFragment extends AbstractFragment implements OnItemClickListener
{
	private GridView		gridView	= null;
	private AddFileAdapter	adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_addfile_inline, container, false);
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

		adapter = new AddFileAdapter(getActivity(), AddFileAdapter.STYLE_INLINE);
		gridView = (GridView) getView().findViewById(R.id.addFilesGridview);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		this.mCallbacks.onAddFileItemSelected(arg2);
	}
}
