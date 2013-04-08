package com.quanturium.androcloud.requests;

import android.util.Log;

import com.quanturium.androcloud.listeners.TransfertTaskListener;

public class DummyTask extends AbstractTransfertTask
{

	public DummyTask(TransfertTaskListener callback)
	{
		super(callback);
	}

	@Override
	protected String doInBackground(AbstractTaskQuery... params)
	{
		try
		{
			Thread.sleep(60000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		Log.i("DummyTask","Ended");
		super.onPostExecute(result);
	}
	
	@Override
	protected void onCancelled()
	{
		Log.i("DummyTask","Canceled");
		super.onCancelled();
	}

}
