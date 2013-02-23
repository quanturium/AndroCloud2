package com.quanturium.androcloud2.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.impl.model.CloudAppItemImpl;
import com.quanturium.androcloud2.Constants;
import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.databases.FilesDatabase;
import com.quanturium.androcloud2.holders.FileDetailsViewHolder;
import com.quanturium.androcloud2.listeners.FragmentListener;

public class FileDetailsFragment extends AbstractFragment implements OnClickListener
{
	private FilesDatabase			database	= null;
	private FileDetailsViewHolder	viewHolder	= null;

	private CloudAppItem			item		= null;

	@Override
	protected FragmentInitParams init()
	{
		return new FragmentInitParams(R.layout.fragment_filedetails, "File", null, false, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		database = FilesDatabase.getInstance(getActivity());

		Bundle bundle = this.getArguments();

		if (bundle != null)
		{
			if (getArguments().containsKey(Constants.ITEM_ID_KEY))
			{
				int itemId = getArguments().getInt(Constants.ITEM_ID_KEY, -1);
				if (itemId != -1)
				{
					try
					{
						loadItem(itemId);						
					} catch (JSONException e)
					{
						e.printStackTrace();
					} catch (CloudAppException e)
					{
						e.printStackTrace();
					}

					if (item != null)
					{
						setUI();
						fillUI();
					}
				}
			}
		}
	}

	@Override
	public void onDestroy()
	{
		// if (imageBitmap != null)
		// {
		// imageBitmap.recycle();
		// imageBitmap = null;
		// }

		super.onDestroy();
	}

	private void setUI()
	{
		viewHolder = new FileDetailsViewHolder();
		viewHolder.view = (ImageView) getActivity().findViewById(R.id.filedetailsActionView);
		viewHolder.view.setOnClickListener(this);
		viewHolder.save = (ImageView) getActivity().findViewById(R.id.filedetailsActionSave);
		viewHolder.save.setOnClickListener(this);
		viewHolder.share = (ImageView) getActivity().findViewById(R.id.filedetailsActionShare);
		viewHolder.share.setOnClickListener(this);
		viewHolder.edit = (ImageView) getActivity().findViewById(R.id.filedetailsActionEdit);
		viewHolder.edit.setOnClickListener(this);
		viewHolder.delete = (ImageView) getActivity().findViewById(R.id.filedetailsActionDelete);
		viewHolder.delete.setOnClickListener(this);
	}

	private void loadItem(int itemId) throws JSONException, CloudAppException
	{
		Cursor cursor = database.getFile(itemId);
		String json = cursor.getString(cursor.getColumnIndex(FilesDatabase.COL_DATA));

		if (json != null)
		{
			item = new CloudAppItemImpl(new JSONObject(json));
			getActivity().getActionBar().setSubtitle(item.getName());
		}
	}

	private void fillUI()
	{

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.filedetailsActionView:

				break;

			case R.id.filedetailsActionSave:

				break;

			case R.id.filedetailsActionShare:

				break;

			case R.id.filedetailsActionEdit:

				break;

			case R.id.filedetailsActionDelete:

				break;
		}
	}
}
