package com.quanturium.androcloud2.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.swipedismiss.SwipeDismissListViewTouchListener;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.adapters.TransfertsAdapter;
import com.quanturium.androcloud2.listeners.FragmentListener;

public class TransfertFragment extends ListFragment
{
	private FragmentListener	mCallbacks			= null;
	private final static String	TAG					= "TransfertFragment";
	private final static int	REFRESH_INTERVAL	= 5;
	private volatile boolean	isActive			= false;

	private TransfertsAdapter	adapter;

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
		return inflater.inflate(R.layout.fragment_transferts, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		((MainActivity) getActivity()).setActionBarNavigationModeList(false);
		getActivity().getActionBar().setTitle("Transferts");

		adapter = new TransfertsAdapter(getActivity());
		configureListview(getListView());
		setListAdapter(adapter);
		setHasOptionsMenu(true);

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

	@Override
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
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
