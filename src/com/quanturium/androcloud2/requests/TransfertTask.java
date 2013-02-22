package com.quanturium.androcloud2.requests;

import android.os.AsyncTask;
import android.util.Log;

import com.quanturium.androcloud2.listeners.TransfertTaskListener;

public abstract class TransfertTask extends AsyncTask<SimpleTaskQuery, Integer, Object>
{
	protected int					id	= -1;
	protected TransfertTaskListener	mCallback;
	protected int					progress;
	protected volatile String		name;

	public TransfertTask(TransfertTaskListener callback)
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
		Log.i("New task", "onPreExecute");
		this.mCallback.onTaskStart(id);
	}

	@Override
	protected abstract Object doInBackground(SimpleTaskQuery... params);

	@Override
	protected void onProgressUpdate(Integer... values)
	{
		Log.i("Task #" + this.id, "onProgressUpdate : " + values[0]);
		this.progress = values[0];
		this.mCallback.onTaskProgress(id, values[0]);
	}

	@Override
	protected void onPostExecute(Object result)
	{
		Log.i("Task #" + this.id, "onPostExecute");
		this.mCallback.onTaskFinish(id, result);
	}

	@Override
	protected void onCancelled()
	{
		Log.i("Task #" + this.id, "onCancelled");
		this.mCallback.onCancel(id);
	}

	public int getProgress()
	{
		return progress;
	}
}
