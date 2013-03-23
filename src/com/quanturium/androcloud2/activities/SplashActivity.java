package com.quanturium.androcloud2.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quanturium.androcloud2.MyApplication;
import com.quanturium.androcloud2.R;

public class SplashActivity extends Activity implements OnClickListener
{
	private final static int	LOGIN_CODE		= 1;
	private final static int	REGISTER_CODE	= 2;

	LinearLayout				container;
	LinearLayout				textContainer;
	LinearLayout				buttonsContainer;
	Button						buttonLogin;
	Button						buttonRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Intent i = getIntent();

		setUI(savedInstanceState == null && i.getBooleanExtra("animated", true) ? true : false);

		if (i.getBooleanExtra("register", false))
		{
			onClick(buttonRegister);
		}
		else if (i.getBooleanExtra("login", false))
		{
			onClick(buttonLogin);
		}
	}
	
	@Override
	protected void onStart()
	{
		((MyApplication)getApplication()).getTracker().sendView("splash");
		super.onStart();
	}

	private void setUI(boolean animated)
	{
		container = (LinearLayout) findViewById(R.id.splashLayout);
		textContainer = (LinearLayout) findViewById(R.id.splashTextLayout);
		buttonsContainer = (LinearLayout) findViewById(R.id.splashButtonsLayout);
		buttonLogin = (Button) findViewById(R.id.splashButtonLogin);
		buttonRegister = (Button) findViewById(R.id.splashButtonRegister);
		final View view = (View) findViewById(R.id.splashHack);

		buttonsContainer.addOnLayoutChangeListener(new OnLayoutChangeListener()
		{

			@Override
			public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom)
			{
				if (left == 0 && top == 0 && right == 0 && bottom == 0)
					return;

				int height = v.getHeight();
				view.getLayoutParams().height = height;
			}
		});

		if (animated)
		{
			TranslateAnimation containerAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -2, Animation.RELATIVE_TO_SELF, 0);
			containerAnimation.setDuration(1200);
			containerAnimation.setInterpolator(new DecelerateInterpolator());
			container.setAnimation(containerAnimation);

			AlphaAnimation textContainerAnimation = new AlphaAnimation(0, 1);
			textContainerAnimation.setDuration(1000);
			textContainerAnimation.setInterpolator(new DecelerateInterpolator());
			textContainerAnimation.setStartOffset(1500);
			textContainer.setAnimation(textContainerAnimation);

			TranslateAnimation buttonsContainerAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 2, Animation.RELATIVE_TO_SELF, 0);
			buttonsContainerAnimation.setDuration(500);
			buttonsContainerAnimation.setInterpolator(new DecelerateInterpolator());
			buttonsContainerAnimation.setStartOffset(2000);
			buttonsContainer.setAnimation(buttonsContainerAnimation);
		}

		buttonLogin.setOnClickListener(this);
		buttonRegister.setOnClickListener(this);

		((TextView) findViewById(R.id.splashBrand)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/AirstreamNF.ttf"));
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.splashButtonLogin:

				Intent i1 = new Intent(this, LoginActivity.class);
				startActivityForResult(i1, 1);

				break;

			case R.id.splashButtonRegister:

				Intent i2 = new Intent(this, RegisterActivity.class);
				startActivityForResult(i2, 2);

				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case LOGIN_CODE:
				
				if(resultCode == RESULT_OK)
				{
					Toast.makeText(this, "Loggin successful", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					startActivity(intent);
					finish();
				}

				break;

			case REGISTER_CODE:
				
				if(resultCode == RESULT_OK)
				{
					Toast.makeText(this, "Registration and login successful", Toast.LENGTH_SHORT).show();
					
					Intent intent = new Intent(this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					startActivity(intent);
					finish();
				}

				break;
		}
	}
}
