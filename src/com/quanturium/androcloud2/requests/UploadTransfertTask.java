package com.quanturium.androcloud2.requests;

import android.util.Log;

import com.cloudapp.api.CloudApp;
import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.api.model.CloudAppProgressListener;
import com.cloudapp.impl.CloudAppImpl;
import com.quanturium.androcloud2.listeners.TransfertTaskListener;

public class UploadTransfertTask extends TransfertTask implements CloudAppProgressListener
{
	private long	timestamp;

	public UploadTransfertTask(TransfertTaskListener callback)
	{
		super(callback);
	}

	@Override
	protected Object doInBackground(SimpleTaskQuery... params)
	{
		UploadTransfertTaskQuery query = (UploadTransfertTaskQuery) params[0];
		name = query.file.getName();

		CloudApp api = new CloudAppImpl(query.username, query.password);

		try
		{
			CloudAppItem item = api.upload(query.file, this);
			Log.i("Task #" + this.id, "url : " + item.getUrl());

			return item;
		} catch (CloudAppException e)
		{
			cancel(true);
		}

		return null;
	}

	@Override
	public void transferred(long written, long length)
	{
		if (System.currentTimeMillis() > timestamp + 500)
		{
			timestamp = System.currentTimeMillis();
			int percent = (int) ((written / (float) length) * 100);
			
			if(percent > 100)
				percent = 100;
			
			publishProgress(percent);
		}
	}

	@Override
	public boolean isRunning()
	{
		return !this.isCancelled();
	}
}
