package com.quanturium.androcloud2.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.quanturium.androcloud2.Constants;
import com.quanturium.androcloud2.MyApplication;
import com.quanturium.androcloud2.MyExceptionHandler;
import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.adapters.MenuAdapter;
import com.quanturium.androcloud2.fragments.AboutFragment;
import com.quanturium.androcloud2.fragments.AddFilesDropdownFragment;
import com.quanturium.androcloud2.fragments.AddFilesInlineFragment;
import com.quanturium.androcloud2.fragments.FileDetailsFragment;
import com.quanturium.androcloud2.fragments.FilesMainFragment;
import com.quanturium.androcloud2.fragments.FilesTrashFragment;
import com.quanturium.androcloud2.fragments.HomeFragment;
import com.quanturium.androcloud2.fragments.MenuFragment;
import com.quanturium.androcloud2.fragments.PreferencesFragment;
import com.quanturium.androcloud2.fragments.TransfertFragment;
import com.quanturium.androcloud2.fragments.UserFragment;
import com.quanturium.androcloud2.listeners.FragmentListener;
import com.quanturium.androcloud2.tools.Prefs;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

public class MainActivity extends SlidingActivity implements FragmentListener, OnBackStackChangedListener
{
	// public int LAST_ITEM_CLICKED = -1;
	Handler						mHandler			= new Handler();

	public final static int		DELAY_MENU_HIDE		= 300;

	private Runnable			mHideMenuRunnable	= new Runnable()
													{
														@Override
														public void run()
														{
															getSlidingMenu().toggle();
														}
													};

	private BroadcastReceiver	mBroadcastReceiver	= new BroadcastReceiver()
													{

														@Override
														public void onReceive(Context context, Intent intent)
														{
															MainActivity.this.onReceiveMessageFromService(intent);
														}

													};

	private final static String	TAG					= "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		if (requestCode == Constants.ADD_FILE_RETURN_CODE && resultCode == RESULT_OK)
		{
			intent.setAction(Constants.INTENT_ACTION_UPLOAD);
			((MyApplication) getApplication()).sendToMainService(intent);
		}
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

	private void replaceContentFragment(Fragment fragment, boolean animated)
	{
		FragmentTransaction t = this.getFragmentManager().beginTransaction();

		// t.setCustomAnimations(R.animator.slide_in_right_delayed1, R.animator.fade_out, R.animator.slide_in_right_delayed1, R.animator.fade_out);

		boolean prefDisplayAnimationsEnabled = Prefs.getPreferences(this).getBoolean(Prefs.SLIDING_ANIMATIONS_ENABLED, true);
		if (animated && prefDisplayAnimationsEnabled)
			t.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);

		t.replace(R.id.fragment_content_frame, fragment, "ContentFragment");

		t.addToBackStack(null);
		t.commit();
	}

	@Override
	public void onMenuItemSelected(int position)
	{
		// if (position == LAST_ITEM_CLICKED) // we don't want a click on the same item to be possible
		// return;

		// LAST_ITEM_CLICKED = position;

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

			case MenuAdapter.ITEM_SHOW_ALL:

				fragment = new FilesMainFragment();

				break;

			case MenuAdapter.ITEM_SHOW_TRASH:

				fragment = new FilesTrashFragment();

				break;

			case MenuAdapter.ITEM_TRANSFERTS:

				fragment = new TransfertFragment();
				
				break;

			case MenuAdapter.ITEM_ADD_FILES:

				onAddFilesMenuClicked();
				// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				// intent.setType("file/*");
				// startActivityForResult(intent, Constants.ADD_FILE_RETURN_CODE);
				return;
		}

		replaceContentFragment(fragment, false);

		if (getSlidingMenu().isMenuShowing())
			this.mHandler.postDelayed(mHideMenuRunnable, DELAY_MENU_HIDE); // we delay the "toggle off" of the menu
	}

	@Override
	public void onFilesItemSelected(int itemId)
	{
		Fragment fragment = new FileDetailsFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.TASK_ID_KEY, itemId);
		fragment.setArguments(bundle);

		replaceContentFragment(fragment, true);
	}

	@Override
	public void onHomeItemSelected(int position)
	{
		Fragment fragment = new FilesMainFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.DROPDOWN_INDEX_KEY, position);
		fragment.setArguments(bundle);

		replaceContentFragment(fragment, false);
	}

	@Override
	public void onAddFilesMenuClicked()
	{
		onAddFilesClicked(1);
	}

	@Override
	public void onAddFilesActionClicked()
	{
		onAddFilesClicked(2);
	}

	private void onAddFilesClicked(int type)
	{
		Fragment fragment = null;
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		if (type == 1)
			fragment = (AddFilesInlineFragment) fm.findFragmentByTag("AddFilesInlineFragment");
		else
			fragment = (AddFilesDropdownFragment) fm.findFragmentByTag("AddFilesDropdownFragment");

		if (fragment == null)
		{
			if (type == 1)
			{
				fragment = new AddFilesInlineFragment();
				ft.add(R.id.addFilesMenuFrameLayout, fragment, "AddFilesInlineFragment");
			}
			else
			{
				fragment = new AddFilesDropdownFragment();
				ft.add(R.id.addFilesActionFramelayout, fragment, "AddFilesDropdownFragment");
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
	public void onAddFilesItemSelected(int position)
	{
		Log.i(TAG, "cool");
	}

	@Override
	public void onUserLoggedOut()
	{
		goToSplashActivity(true);
	}
}
