package com.quanturium.androcloud2.fragments;

import com.quanturium.androcloud2.FragmentInitParams;
import com.quanturium.androcloud2.R;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;



public class FilesTrashFragment extends FilesAbstractFragment
{
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
	public void onItemClick(int itemId)
	{
		// TODO : restore the item
	}
}
