package com.quanturium.androcloud.requests;

import android.os.AsyncTask;

import com.quanturium.androcloud.listeners.TransfertTaskListener;
import com.quanturium.androcloud.tools.Logger;

public abstract class AbstractTransfertTask extends AsyncTask<AbstractTaskQuery, Integer, String>
{
	protected int					id	= -1;
	protected TransfertTaskListener	mCallback;
	protected int					progress;
	protected volatile String		name;

	public AbstractTransfertTask(TransfertTaskListener callback)
	{
		this.mCallback = callback;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return this.id;
	}
	
	public String getname()
	{
		return this.name;
	}

	@Override
	protected void onPreExecute()
	{
		Logger.v("New task", "onPreExecute");
		this.mCallback.onTaskStart(id);
	}

	@Override
	protected abstract String doInBackground(AbstractTaskQuery... params);

	@Override
	protected void onProgressUpdate(Integer... values)
	{
		Logger.v("Task #" + this.id, "onProgressUpdate : " + values[0]);
		this.progress = values[0];
		this.mCallback.onTaskProgress(id, values[0]);
	}

	@Override
	protected void onPostExecute(String result)
	{
		Logger.v("Task #" + this.id, "onPostExecute");
		this.mCallback.onTaskFinish(id, result);
	}

	@Override
	protected void onCancelled()
	{
		Logger.v("Task #" + this.id, "onCancelled");
		this.mCallback.onCancel(id);
	}

	public int getProgress()
	{
		return progress;
	}
}
