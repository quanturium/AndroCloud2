package com.quanturium.androcloud.requests;

import java.util.List;

import android.os.AsyncTask;

import com.cloudapp.api.CloudApp;
import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.cloudapp.impl.CloudAppImpl;
import com.quanturium.androcloud.listeners.FilesTaskListener;

public class FilesTask extends AsyncTask<FilesTaskQuery, Integer, FilesTaskAnswer>
{
	private FilesTaskListener mCallback;
	
	public FilesTask(FilesTaskListener listener)
	{
		this.mCallback = listener;
	}
	
	public void setCallback(FilesTaskListener listener)
	{
		this.mCallback = listener;
	}
	
	@Override
	protected FilesTaskAnswer doInBackground(FilesTaskQuery... params)
	{
		FilesTaskQuery query = params[0];
		CloudApp api = new CloudAppImpl(query.username , query.password);
		List<CloudAppItem> items = null;
		FilesTaskAnswer answer;
		
		try
		{		
			items = api.getItems(query.page, query.nbFiles, null, query.trashed, null);
			query.database.addFiles(items.toArray(new CloudAppItem[items.size()]));
			answer = new FilesTaskAnswer(FilesTaskAnswer.RESULT_OK, null, query.page);

		} catch (CloudAppException e)
		{
			answer = new FilesTaskAnswer(FilesTaskAnswer.RESULT_ERROR, e, 0);
			e.printStackTrace();
		}
		
		return answer;
	}
	
	@Override
	protected void onPostExecute(FilesTaskAnswer answer)
	{		
		if(this.mCallback != null)
			this.mCallback.onTaskFinished(answer);		
	}

	@Override
	protected void onCancelled()
	{
		if(this.mCallback != null)
			this.mCallback.onTaskCanceled();				
	}
}
