package com.quanturium.androcloud;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.quanturium.androcloud.requests.ImageCacheTask;
import com.quanturium.androcloud.services.MainService;

public class MyApplication extends Application
{
	private Map<Integer, ImageCacheTask>	imageCacheTaskArray	= new HashMap<Integer, ImageCacheTask>();
	private GoogleAnalytics					googleAnalytics;
	private Tracker							tracker;

	@Override
	public void onCreate()
	{		
		setupAnalytics();
		super.onCreate();
	}

	private void setupAnalytics()
	{
		this.googleAnalytics = GoogleAnalytics.getInstance(this);
		this.tracker = this.googleAnalytics.getTracker(Constants.ANALYTICS_TRACKING_ID);
	}
	
	public Tracker getTracker()
	{
		return this.tracker;
	}
	
	public ImageCacheTask getImageCacheTask(int id)
	{
		return imageCacheTaskArray.get(id);
	}

	public void setImageCacheTask(int id, ImageCacheTask imageCacheTask)
	{
		if (imageCacheTask == null)
			imageCacheTaskArray.remove(id);
		else
			imageCacheTaskArray.put(id, imageCacheTask);
	}

	public void sendToMainService(Intent intent)
	{
		ComponentName component = new ComponentName(this, MainService.class);
		intent.setComponent(component);

		startService(intent);
	}
}
