package com.quanturium.androcloud2.fragments;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;



public class FilesTrashFragment extends FilesAbstractFragment
{
	protected boolean willDisplayTrash()
	{
		return true;
	}
	
	protected String getTitle()
	{
		return "Trash";
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
