package com.quanturium.androcloud2.requests;

import com.cloudapp.api.CloudApp;
import com.cloudapp.api.CloudAppException;
import com.cloudapp.impl.CloudAppImpl;

import android.os.AsyncTask;

public class FileActionTask extends AsyncTask<FileActionTaskQuery, Integer, Void>
{

	@Override
	protected Void doInBackground(FileActionTaskQuery... params)
	{
		FileActionTaskQuery query = params[0];
		CloudApp api = new CloudAppImpl(query.username, query.password);

		switch (query.type)
		{
			case RENAME:

				try
				{
					api.rename(query.item, query.newName);
				} catch (CloudAppException e)
				{
					e.printStackTrace();
				}

				break;

			case DELETE:

				try
				{
					api.delete(query.item);
				} catch (CloudAppException e)
				{
					e.printStackTrace();
				}

				break;

			case RESTORE:
				try
				{
					api.recover(query.item);
				} catch (CloudAppException e)
				{
					e.printStackTrace();
				}
		}

		return null;
	}

}
