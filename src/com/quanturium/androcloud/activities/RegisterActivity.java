package com.quanturium.androcloud.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cloudapp.api.CloudApp;
import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppAccount;
import com.cloudapp.impl.CloudAppImpl;
import com.quanturium.androcloud.MyApplication;
import com.quanturium.androcloud.R;
import com.quanturium.androcloud.tools.Prefs;

public class RegisterActivity extends Activity implements OnClickListener
{
	EditText						emailText;
	EditText						passwordText;
	CheckBox						tosCheckbox;
	Button							registerButton;
	private final static Handler	handler				= new Handler();
	private ProgressDialog			registerRunningDialog	= null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		emailText = (EditText) findViewById(R.id.registerEmail);
		passwordText = (EditText) findViewById(R.id.registerPassword);
		registerButton = (Button) findViewById(R.id.registerBouton);
		tosCheckbox = (CheckBox) findViewById(R.id.registerTOS);
		tosCheckbox.setOnClickListener(this);
		registerButton.setOnClickListener(this);		
	}
	
	@Override
	protected void onStart()
	{
		((MyApplication)getApplication()).getTracker().sendView("register");
		super.onStart();
	}
	
	@Override
	protected void onDestroy()
	{
		if (registerRunningDialog != null)
			registerRunningDialog.dismiss();
		
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				Intent intent = new Intent(this, SplashActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("animated", false);
				startActivity(intent);
				return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void registerRunningDialog()
	{
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Registration");
		dialog.setCancelable(false);

		registerRunningDialog = dialog;
		registerRunningDialog.show();
	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.registerTOS :
			
			if(((CheckBox)v).isChecked())			
				startActivity(new Intent(this, TosActivity.class));
			
			break;
			
		case R.id.registerBouton :
		
			final String email = emailText.getText().toString().trim();
			final String password = passwordText.getText().toString().trim();
			if (email.equals("") || password.equals(""))
			{
				Toast.makeText(this, "All the fields need to be filled", Toast.LENGTH_SHORT).show();
			}
			else if (!tosCheckbox.isChecked())
			{
				Toast.makeText(this, "You must accept the term of service", Toast.LENGTH_SHORT).show();
			}
			else
			{
				registerRunningDialog();
				
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						CloudApp api = new CloudAppImpl();
	
						try
						{
							final CloudAppAccount account = api.createAccount(email, password, true);
	
							if (account != null && account.getEmail().equals(email))
							{
								handler.post(new Runnable()
								{
	
									@Override
									public void run()
									{
										registerRunningDialog.cancel();
	
										int hash = (email + password).hashCode();
	
										Prefs.getPreferences(RegisterActivity.this).edit().putString(Prefs.USER_INFOS, account.getJson().toString()).commit();
										Prefs.getPreferences(RegisterActivity.this).edit().putString(Prefs.EMAIL, email).commit();
										Prefs.getPreferences(RegisterActivity.this).edit().putString(Prefs.PASSWORD, password).commit();
										Prefs.getPreferences(RegisterActivity.this).edit().putInt(Prefs.HASH, hash).commit();
										Prefs.getPreferences(RegisterActivity.this).edit().putBoolean(Prefs.LOGGED_IN, true).commit();
	
										Intent returnIntent = new Intent();
										setResult(RESULT_OK, returnIntent);
										finish();
									}
								});
							}
	
						} catch (final CloudAppException e)
						{
							handler.post(new Runnable()
							{
								@Override
								public void run()
								{
									registerRunningDialog.cancel();
	
									switch(e.getCode())
									{
										case 422:
											Toast.makeText(RegisterActivity.this, "Registration failed. Your email address must be valid and your password must be at least 4 characters long", Toast.LENGTH_LONG).show();
											break;
											
										case 406 :
											Toast.makeText(RegisterActivity.this, "Registration failed. An account with " + email + " already exists", Toast.LENGTH_LONG).show();
											break;
										
										default :
											Toast.makeText(RegisterActivity.this, "Registration failed. Check your connection", Toast.LENGTH_SHORT).show();
											break;
									}																
								}
							});
	
							e.printStackTrace();
						}
					}
				}).start();
			}
			
			break;
		}
	}
}
