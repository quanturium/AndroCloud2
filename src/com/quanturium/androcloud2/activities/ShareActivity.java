package com.quanturium.androcloud2.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.quanturium.androcloud2.MyApplication;

public class ShareActivity extends Activity
{
	private final static String	TAG	= "ShareActivity";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		
		if(intent != null)
			((MyApplication)getApplication()).sendToMainService(intent);
		
		finish();
	}
}
