package com.quanturium.androcloud.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.quanturium.androcloud.MyApplication;
import com.quanturium.androcloud.tools.Prefs;

public class ShareActivity extends Activity
{
	private final static String	TAG	= "ShareActivity";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		
		if(intent != null)
		{
			if(Prefs.getPreferences(this).getBoolean(Prefs.LOGGED_IN, false))
			{
				((MyApplication)getApplication()).getTracker().sendEvent("user_action", "share", "file", null);
				((MyApplication)getApplication()).sendToMainService(intent);
			}
			else
			{
				Toast.makeText(this, "You need to be logged in with your CloudApp account to upload a file", Toast.LENGTH_LONG).show();
				startActivity(new Intent(this, MainActivity.class));
			}			
		}
			
		
		finish();
	}
}
