package com.quanturium.androcloud.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.quanturium.androcloud.MyApplication;
import com.quanturium.androcloud.R;

public class TosActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tos);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		try {
			InputStream is = getAssets().open("tos/tos.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
	        while (line != null) {
	            sb.append(line+"\n");
	            line = br.readLine();
	        }
			
			((TextView)findViewById(R.id.tosText)).setText(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
	protected void onStart()
	{
		((MyApplication)getApplication()).getTracker().sendView("tos");
		super.onStart();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent(this, RegisterActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
}
