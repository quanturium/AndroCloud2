package com.quanturium.androcloud2.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.swipedismiss.SwipeDismissListViewTouchListener;
import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.adapters.TransfertsAdapter;
import com.quanturium.androcloud2.tools.Prefs;

public class TransfertFragment extends AbstractListFragment
{
	private final static int	REFRESH_INTERVAL	= 5;
	private volatile boolean	isActive			= false;

	private TransfertsAdapter	adapter;

	@Override
	protected FragmentInitParams init()
	{
		return new FragmentInitParams(R.layout.fragment_transferts, "Transferts", null, false, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		adapter = new TransfertsAdapter(getActivity());
		configureListview(getListView());
		setListAdapter(adapter);
		
		if(adapter.getCountUpload() == 0)
			Prefs.getPreferences(getActivity()).edit().putInt(Prefs.NUMBER_BACKGROUND_UPLOADS_RUNNING, 0).commit();
		if(adapter.getCountDownload() == 0)
			Prefs.getPreferences(getActivity()).edit().putInt(Prefs.NUMBER_BACKGROUND_DOWNLOADS_RUNNING, 0).commit();

		Toast.makeText(getActivity(), "Swipe to cancel", Toast.LENGTH_SHORT).show();
		updateView();
	}

	@Override
	public void onResume()
	{
		this.isActive = true;
		setRecurringrefresh();
		super.onResume();
	}

	@Override
	public void onPause()
	{
		this.isActive = false;
		super.onPause();
	}

	private void configureListview(ListView listView)
	{
		// TEST

		SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(listView, new SwipeDismissListViewTouchListener.OnDismissCallback()
		{
			@Override
			public void onDismiss(ListView listView, int[] reverseSortedPositions)
			{
				for (int position : reverseSortedPositions)
				{
					adapter.remove(position);
				}
				adapter.notifyDataSetChanged();
			}
		});

		listView.setOnTouchListener(touchListener);
		listView.setOnScrollListener(touchListener.makeScrollListener());

		// TEST
	}

	private void setRecurringrefresh()
	{

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				if (isActive)
				{
					Log.i(TAG, "refresh");
					refresh();
					handler.postDelayed(this, REFRESH_INTERVAL * 1000);
				}
			}
		}, REFRESH_INTERVAL * 1000);
	}

	private void refresh()
	{
		adapter.update();
		updateView();
	}

	private void updateView()
	{
		if (getActivity() != null)
		{
			TextView textView = (TextView) getActivity().findViewById(R.id.transfertHeader);

			if (textView != null)
				textView.setText("Transferts : " + adapter.getCountUpload() + " uploads & " + adapter.getCountDownload() + " downloads");
		}
	}
}
