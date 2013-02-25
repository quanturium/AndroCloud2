package com.quanturium.androcloud2.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.impl.model.CloudAppItemImpl;
import com.quanturium.androcloud2.Constants;
import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.databases.FilesDatabase;
import com.quanturium.androcloud2.requests.FileActionTask;
import com.quanturium.androcloud2.requests.FileActionTaskQuery;
import com.quanturium.androcloud2.requests.FileActionTaskQuery.ActionType;
import com.quanturium.androcloud2.tools.Prefs;

public class FilesTrashFragment extends FilesAbstractFragment
{
	AlertDialog	alertRestore	= null;
	private int itemClickedId = 0;

	protected boolean willDisplayTrash()
	{
		return true;
	}

	@Override
	protected FragmentInitParams init()
	{
		return new FragmentInitParams(R.layout.fragment_files, "Trash", null, true, true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		if (savedInstanceState != null)
		{
			if (savedInstanceState.containsKey(Constants.FILE_ALERT_RESTORE_KEY))
			{
				int itemId = savedInstanceState.getInt(Constants.FILE_ITEMID_RESTORE_KEY);
				createAlertRestore(itemId);
			}
				
		}
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		if (alertRestore != null && alertRestore.isShowing())
		{
			outState.putBoolean(Constants.FILE_ALERT_RESTORE_KEY, true);
			outState.putInt(Constants.FILE_ITEMID_RESTORE_KEY, itemClickedId);
		}			

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onItemClick(int itemId)
	{
		createAlertRestore(itemId);
		itemClickedId = itemId;
	}

	private void createAlertRestore(final int itemId)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("Restore file");
		builder.setMessage("Would you like to restore the file ?");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				Cursor cursor = database.getFile(itemId, true);
				String json = cursor.getString(cursor.getColumnIndex(FilesDatabase.COL_DATA));
				if (json != null)
				{
					try
					{
						CloudAppItem item = new CloudAppItemImpl(new JSONObject(json));

						FileActionTaskQuery query = new FileActionTaskQuery(Prefs.getPreferences(getActivity()).getString(Prefs.EMAIL, ""), Prefs.getPreferences(getActivity()).getString(Prefs.PASSWORD, ""), item, ActionType.RESTORE, null);
						FileActionTask restoreTask = new FileActionTask();
						restoreTask.execute(query);

						MainActivity activity = (MainActivity) getActivity();

						if (activity != null)
						{
							item.setDeleted(false);
							database.updateFile(itemId, item);
							getLoaderManager().restartLoader(0, null, FilesTrashFragment.this);

							Toast.makeText(getActivity(), "File  restored", Toast.LENGTH_SHORT).show();
						}

					} catch (JSONException e)
					{
						e.printStackTrace();
					} catch (CloudAppException e)
					{					
						e.printStackTrace();
					}
				}
			}
		});

		alertRestore = builder.create();
		alertRestore.show();
	}
}
