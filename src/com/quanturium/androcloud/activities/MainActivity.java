package com.quanturium.androcloud.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.quanturium.androcloud.Constants;
import com.quanturium.androcloud.MyApplication;
import com.quanturium.androcloud.R;
import com.quanturium.androcloud.adapters.AddFileAdapter;
import com.quanturium.androcloud.adapters.MenuAdapter;
import com.quanturium.androcloud.fragments.AboutFragment;
import com.quanturium.androcloud.fragments.AddFileDropdownFragment;
import com.quanturium.androcloud.fragments.AddFileInlineFragment;
import com.quanturium.androcloud.fragments.FileDetailsFragment;
import com.quanturium.androcloud.fragments.FilesMainFragment;
import com.quanturium.androcloud.fragments.FilesTrashFragment;
import com.quanturium.androcloud.fragments.HomeFragment;
import com.quanturium.androcloud.fragments.MenuFragment;
import com.quanturium.androcloud.fragments.PreferencesFragment;
import com.quanturium.androcloud.fragments.TransfertFragment;
import com.quanturium.androcloud.fragments.UserFragment;
import com.quanturium.androcloud.listeners.FragmentListener;
import com.quanturium.androcloud.tools.Prefs;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class MainActivity extends SlidingActivity implements FragmentListener, OnBackStackChangedListener
{
	Handler						mHandler				= new Handler();

	public final static int		DELAY_MENU_HIDE			= 300;

	private Runnable			mHideMenuRunnable		= new Runnable()
														{
															@Override
															public void run()
															{
																getSlidingMenu().toggle();
															}
														};

	private BroadcastReceiver	mBroadcastReceiver		= new BroadcastReceiver()
														{

															@Override
															public void onReceive(Context context, Intent intent)
															{
																MainActivity.this.onReceiveMessageFromService(intent);
															}

														};

	private final static String	TAG						= "MainActivity";

	public boolean				changesOccuredOnFiles	= false;

	private void setupCrittercism()
	{
		JSONObject crittercismConfig = new JSONObject();
		try
		{
		    crittercismConfig.put("shouldCollectLogcat", true); // send logcat data for devices with API Level 16 and higher
		}
		catch (JSONException je){}

		Crittercism.init(getApplicationContext(), "5164b0b25f72163a5d000002", crittercismConfig);
		
		String crittercismUsername = Prefs.getPreferences(getApplicationContext()).getString(Prefs.EMAIL, null);
		if(crittercismUsername != null)
			Crittercism.setUsername(crittercismUsername);
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		/**
		 * Crittercism
		 */
		setupCrittercism();		
		
		// Set default preferences
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		if (!Prefs.getPreferences(this).getBoolean(Prefs.LOGGED_IN, false)) // Not logged in
		{
			goToSplashActivity(false);
		}

		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.activity_main_behind);

		this.initialize(savedInstanceState);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();

		IntentFilter iff = new IntentFilter();
		iff.addAction(Constants.INTENT_ACTION_TRANSFERTS_RUNNING);

		this.registerReceiver(this.mBroadcastReceiver, iff);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		this.unregisterReceiver(mBroadcastReceiver);
	}

	private void initialize(Bundle savedInstanceState)
	{
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindWidth(500);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		FragmentManager fm = getFragmentManager();
		fm.addOnBackStackChangedListener(this);
		FragmentTransaction ft = fm.beginTransaction();

		MenuFragment menuFragment = new MenuFragment();
		ft.replace(R.id.fragment_menu_frame, menuFragment, "MenuFragment");

		Intent i = getIntent();

		if (i != null && i.getAction() != null && i.getAction().equals(Constants.INTENT_ACTION_DISPLAY_TRANSFERTS))
		{
			TransfertFragment transfertFragment = new TransfertFragment();
			ft.replace(R.id.fragment_content_frame, transfertFragment, "ContentFragment");
		}
		else if (savedInstanceState == null)
		{
			HomeFragment homeFragment = new HomeFragment();
			ft.replace(R.id.fragment_content_frame, homeFragment, "ContentFragment");
		}

		ft.commit();
	}

	@Override
	protected void onNewIntent(Intent i)
	{
		if (i != null && i.getAction() != null && i.getAction().equals(Constants.INTENT_ACTION_DISPLAY_TRANSFERTS))
		{
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			TransfertFragment transfertFragment = new TransfertFragment();
			ft.replace(R.id.fragment_content_frame, transfertFragment, "ContentFragment");
			ft.addToBackStack(null);
			ft.commit();
		}
	}

	protected void onReceiveMessageFromService(Intent intent)
	{
		String action = intent.getAction();

		if (action.equals(Constants.INTENT_ACTION_TRANSFERTS_RUNNING))
		{
			int number[] = intent.getIntArrayExtra(Constants.INTENT_ACTIONN_TASKS_RUNNING_NUMBER_KEY);

			FragmentManager fm = getFragmentManager();
			MenuFragment menuFragment = (MenuFragment) fm.findFragmentByTag("MenuFragment");

			if (menuFragment != null)
				menuFragment.setBackgroundTasksRunning(number);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				toggle();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void goToSplashActivity(boolean directToLogin)
	{
		Intent intent = new Intent(this, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		if (directToLogin)
		{
			intent.putExtra("animated", false);
			intent.putExtra("login", true);
		}
		else
		{
			intent.putExtra("animated", true);
		}

		startActivity(intent);
		finish();
	}

	public void setActionBarNavigationModeList(boolean value)
	{
		if (value)
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		else
			getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}

	@Override
	public void onBackStackChanged()
	{

	}

	private void replaceContentFragment(Fragment fragment)
	{
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		// t.setCustomAnimations(R.animator.slide_in_right_delayed1, R.animator.fade_out, R.animator.slide_in_right_delayed1, R.animator.fade_out);

		// boolean prefDisplayAnimationsEnabled = Prefs.getPreferences(this).getBoolean(Prefs.SLIDING_ANIMATIONS_ENABLED, true);
		// if (animated && prefDisplayAnimationsEnabled)
		// ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);

		ft.replace(R.id.fragment_content_frame, fragment, "ContentFragment");

		ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	public void onMenuItemSelected(int position)
	{
		Fragment fragment = null;

		switch (position)
		{
			case MenuAdapter.ITEM_USER:

				fragment = new UserFragment();

				break;

			case MenuAdapter.ITEM_HOME:

				fragment = new HomeFragment();

				break;

			case MenuAdapter.ITEM_PREFERENCES:

				fragment = new PreferencesFragment();

				break;

			case MenuAdapter.ITEM_ABOUT:

				fragment = new AboutFragment();

				break;
				
			case MenuAdapter.ITEM_RATETHEAPP :
				
				String packageName = getApplicationContext().getPackageName();
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				return;

			case MenuAdapter.ITEM_SHOW_ALL:

				fragment = new FilesMainFragment();

				break;

			case MenuAdapter.ITEM_SHOW_TRASH:

				fragment = new FilesTrashFragment();

				break;

			case MenuAdapter.ITEM_TRANSFERTS:

				fragment = new TransfertFragment();

				break;

			case MenuAdapter.ITEM_ADD_FILE:

				onAddFileMenuClicked();
				return;
		}

		replaceContentFragment(fragment);

		if (getSlidingMenu().isMenuShowing())
			this.mHandler.postDelayed(mHideMenuRunnable, DELAY_MENU_HIDE); // we delay the "toggle off" of the menu
	}

	@Override
	public void onFilesItemSelected(int itemId)
	{
		Fragment fragment = new FileDetailsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.ITEM_ID_KEY, itemId);
		fragment.setArguments(bundle);

		replaceContentFragment(fragment);
	}

	@Override
	public void onHomeItemSelected(int position)
	{
		Fragment fragment = new FilesMainFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.DROPDOWN_INDEX_KEY, position);
		fragment.setArguments(bundle);

		replaceContentFragment(fragment);
	}

	@Override
	public void onAddFileMenuClicked()
	{
		onAddFileClicked(1);
	}

	@Override
	public void onAddFileActionClicked()
	{
		onAddFileClicked(2);
	}

	private void onAddFileClicked(int type)
	{
		Fragment fragment = null;
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		if (type == 1)
			fragment = (AddFileInlineFragment) fm.findFragmentByTag("AddFileInlineFragment");
		else
			fragment = (AddFileDropdownFragment) fm.findFragmentByTag("AddFileDropdownFragment");

		if (fragment == null)
		{
			if (type == 1)
			{
				fragment = new AddFileInlineFragment();
				ft.add(R.id.addFileMenuFrameLayout, fragment, "AddFileInlineFragment");
			}
			else
			{
				fragment = new AddFileDropdownFragment();
				ft.add(R.id.addFileActionFramelayout, fragment, "AddFileDropdownFragment");
				ft.addToBackStack(null);
			}

			ft.commit();
		}
		else
		{
			if (type == 1)
			{
				ft.remove(fragment);
				ft.commit();
			}
			else
				fm.popBackStack();
		}
	}

	@Override
	public void onAddFileItemSelected(int position)
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);

		switch (position)
		{
			case AddFileAdapter.ITEM_IMAGE:

				Intent intentImage = new Intent();
				intentImage.setAction(Intent.ACTION_GET_CONTENT);
				intentImage.setType("image/*");
				startActivityForResult(Intent.createChooser(intentImage, "Choose a picture"), Constants.ADD_FILE_RETURN_CODE);

				break;

			case AddFileAdapter.ITEM_BOOKMARK:

				// TODO : popup to add a bookmark
				Toast.makeText(this, R.string.to_be_implemented, Toast.LENGTH_SHORT).show();

				break;

			case AddFileAdapter.ITEM_TEXT:

				Intent intentText = new Intent();
				intentText.setAction(Intent.ACTION_GET_CONTENT);
				intentText.setType("text/plain");
				startActivityForResult(Intent.createChooser(intentText, "Choose a text file"), Constants.ADD_FILE_RETURN_CODE);

				break;

			case AddFileAdapter.ITEM_AUDIO:

				Intent intentAudio = new Intent();
				intentAudio.setAction(Intent.ACTION_GET_CONTENT);
				intentAudio.setType("audio/*");
				startActivityForResult(Intent.createChooser(intentAudio, "Choose an audio file"), Constants.ADD_FILE_RETURN_CODE);

				break;

			case AddFileAdapter.ITEM_VIDEO:

				Intent intentVideo = new Intent();
				intentVideo.setAction(Intent.ACTION_GET_CONTENT);
				intentVideo.setType("video/*");
				startActivityForResult(Intent.createChooser(intentVideo, "Choose a video"), Constants.ADD_FILE_RETURN_CODE);

				break;

			case AddFileAdapter.ITEM_UNKNOWN:

				Intent intentUnknown = new Intent();
				intentUnknown.setAction(Intent.ACTION_GET_CONTENT);
				intentUnknown.setType("file/*");
				startActivityForResult(Intent.createChooser(intentUnknown, "Choose a file"), Constants.ADD_FILE_RETURN_CODE);

				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (requestCode == Constants.ADD_FILE_RETURN_CODE && resultCode == RESULT_OK)
		{
			intent.setAction(Constants.INTENT_ACTION_UPLOAD);
			((MyApplication) getApplication()).sendToMainService(intent);
		}
	}

	@Override
	public void onUserLoggedOut()
	{
		Prefs.getPreferences(getApplicationContext()).edit().remove(Prefs.USER_INFOS).commit();
		Prefs.getPreferences(getApplicationContext()).edit().remove(Prefs.EMAIL).commit();
		Prefs.getPreferences(getApplicationContext()).edit().remove(Prefs.PASSWORD).commit();
		Prefs.getPreferences(getApplicationContext()).edit().remove(Prefs.HASH).commit();
		Prefs.getPreferences(getApplicationContext()).edit().putBoolean(Prefs.LOGGED_IN, false).commit();

		goToSplashActivity(true);
	}
}
