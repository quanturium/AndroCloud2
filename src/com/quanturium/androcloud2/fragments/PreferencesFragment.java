package com.quanturium.androcloud2.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.quanturium.androcloud2.R;
import com.quanturium.androcloud2.activities.MainActivity;
import com.quanturium.androcloud2.databases.FilesDatabase;
import com.quanturium.androcloud2.listeners.FragmentListener;
import com.quanturium.androcloud2.tools.Prefs;

public class PreferencesFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
{
	private FragmentListener	mCallbacks	= null;
	private final static String	TAG			= "PreferencesFragment";

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);

		if (!(activity instanceof FragmentListener))
			throw new IllegalStateException("Activity must implement fragment's callbacks.");

		this.mCallbacks = (FragmentListener) activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		((MainActivity) getActivity()).setActionBarNavigationModeList(false);
		getActivity().getActionBar().setTitle("Preferences");

		addPreferencesFromResource(R.xml.preferences);

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		prefs.registerOnSharedPreferenceChangeListener(this);

		setClearCacheAction();

		setSummaryNbFileToDisplay();

		setSummaryDirectoryType();
		setSummaryDirectory(Prefs.DIRECTORY_MOVIES);
		setSummaryDirectory(Prefs.DIRECTORY_MUSICS);
		setSummaryDirectory(Prefs.DIRECTORY_PICTURES);
		setSummaryDirectory(Prefs.DIRECTORY_TEXTS);
		setSummaryDirectory(Prefs.DIRECTORY_UNKOWN);

		setSummaryUploadAction();
		setSummaryDownloadAction();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		view.setBackgroundResource(android.R.color.white);
	}

	@Override
	public void onDetach()
	{
		this.mCallbacks = null;
		super.onDetach();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		if (key.equals(Prefs.FILES_PER_REQUEST))
		{
			Prefs.getPreferences(getActivity()).edit().putBoolean(Prefs.FILES_PER_REQUEST_CHANGED, true).commit();
		}

		if (key.equals(Prefs.DIRECTORY_MOVIES) || key.equals(Prefs.DIRECTORY_MUSICS) || key.equals(Prefs.DIRECTORY_PICTURES) || key.equals(Prefs.DIRECTORY_TEXTS) || key.equals(Prefs.DIRECTORY_UNKOWN))
			setSummaryDirectory(key);

		if (key.equals(Prefs.SAVING_TYPE))
		{
			setSummaryDirectoryType();
			setSummaryDirectory(Prefs.DIRECTORY_MOVIES);
			setSummaryDirectory(Prefs.DIRECTORY_MUSICS);
			setSummaryDirectory(Prefs.DIRECTORY_PICTURES);
			setSummaryDirectory(Prefs.DIRECTORY_TEXTS);
			setSummaryDirectory(Prefs.DIRECTORY_UNKOWN);
		}

		if (key.equals(Prefs.DOWNLOAD_NOTIFICATION_ACTION))
			setSummaryDownloadAction();

		if (key.equals(Prefs.UPLOAD_NOTIFICATION_ACTION))
			setSummaryUploadAction();

		if (key.equals(Prefs.FILES_PER_REQUEST))
			setSummaryNbFileToDisplay();
	}

	private void setSummaryNbFileToDisplay()
	{
		Preference preferencesNbFileToDisplay = findPreference(Prefs.FILES_PER_REQUEST);
		String preferencesNbFileToDisplayValue = Prefs.getPreferences(getActivity()).getString(Prefs.FILES_PER_REQUEST, "20");

		preferencesNbFileToDisplay.setSummary(preferencesNbFileToDisplayValue);
	}

	private void setSummaryDownloadAction()
	{
		Preference preferencesDownloadAction = findPreference(Prefs.DOWNLOAD_NOTIFICATION_ACTION);
		String preferencesDownloadActionValue = Prefs.getPreferences(getActivity()).getString(Prefs.DOWNLOAD_NOTIFICATION_ACTION, "0");

		String[] preferencesDownloadActions = getResources().getStringArray(R.array.notificationDownloadAction);

		preferencesDownloadAction.setSummary("On click : " + preferencesDownloadActions[Integer.valueOf(preferencesDownloadActionValue)]);
	}

	private void setSummaryUploadAction()
	{
		Preference preferencesUploadAction = findPreference(Prefs.UPLOAD_NOTIFICATION_ACTION);
		String preferencesUploadActionValue = Prefs.getPreferences(getActivity()).getString(Prefs.UPLOAD_NOTIFICATION_ACTION, "0");

		String[] preferencesUploadActions = getResources().getStringArray(R.array.notificationUploadAction);

		preferencesUploadAction.setSummary("On click : " + preferencesUploadActions[Integer.valueOf(preferencesUploadActionValue)]);
	}

	private void setSummaryDirectory(String key)
	{
		Preference preferencesDirectory = findPreference(key);
		String prefType = Prefs.getPreferences(getActivity()).getString(Prefs.SAVING_TYPE, "0");

		if (prefType.equals("2")) // custom
		{
			preferencesDirectory.setEnabled(true);
		}
		else if (prefType.equals("1"))
		{
			preferencesDirectory.setEnabled(false);

			Prefs.getPreferences(getActivity()).edit().putString(key, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()).commit();
		}
		else
		{
			preferencesDirectory.setEnabled(false);
			String directory = null;

			if (key.equals(Prefs.DIRECTORY_PICTURES))
				directory = Environment.DIRECTORY_PICTURES;
			else if (key.equals(Prefs.DIRECTORY_MUSICS))
				directory = Environment.DIRECTORY_MUSIC;
			else if (key.equals(Prefs.DIRECTORY_MOVIES))
				directory = Environment.DIRECTORY_MOVIES;
			else if (key.equals(Prefs.DIRECTORY_TEXTS))
				directory = Environment.DIRECTORY_DOWNLOADS;
			else if (key.equals(Prefs.DIRECTORY_UNKOWN))
				directory = Environment.DIRECTORY_DOWNLOADS;

			if (directory != null)
				Prefs.getPreferences(getActivity()).edit().putString(key, Environment.getExternalStoragePublicDirectory(directory).getPath()).commit();
		}

		String preferencesDirectoryValue = Prefs.getPreferences(getActivity()).getString(key, "");

		preferencesDirectory.setSummary(preferencesDirectoryValue);
	}

	private void setSummaryDirectoryType()
	{
		Preference preferencesSavingType = findPreference(Prefs.SAVING_TYPE);
		String preferencesSavingTypeValue = Prefs.getPreferences(getActivity()).getString(Prefs.SAVING_TYPE, "0");

		String[] preferencesUploadActions = getResources().getStringArray(R.array.savingType);

		preferencesSavingType.setSummary(preferencesUploadActions[Integer.valueOf(preferencesSavingTypeValue)]);
	}

	private void setClearCacheAction()
	{
		Preference preferencesClearCache = findPreference(Prefs.CLEAR_CACHE);

		preferencesClearCache.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{

			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("Are you sure you want to delete the temporary files ?").setCancelable(true).setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int id)
					{
						// Cache.removeAll(getActivity()); TODO to be implemented
						dialog.cancel();
						Toast.makeText(getActivity(), "Cache cleared", Toast.LENGTH_SHORT).show();
					}
				}).setNegativeButton("No", null);

				AlertDialog alert = builder.create();
				alert.show();

				return true;
			}
		});
	}
}
