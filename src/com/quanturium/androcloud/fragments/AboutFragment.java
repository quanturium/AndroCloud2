package com.quanturium.androcloud.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.quanturium.androcloud.Constants;
import com.quanturium.androcloud.FragmentInitParams;
import com.quanturium.androcloud.MyApplication;
import com.quanturium.androcloud.R;
import com.quanturium.androcloud.changelog.ChangelogEntryXmlParser;
import com.quanturium.androcloud.models.ChangelogEntryItemModel;
import com.quanturium.androcloud.models.ChangelogEntryModel;

public class AboutFragment extends AbstractFragment implements OnClickListener
{
	AlertDialog	alertChangelog	= null;

	@Override
	protected FragmentInitParams init()
	{
		return new FragmentInitParams(R.layout.fragment_about, "About", null, false, false);
	}
	
	@Override
	public void onStart()
	{
		((MyApplication) getActivity().getApplication()).getTracker().sendView("about");
		super.onStart();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		setUI(savedInstanceState);
		fillUI();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		if (alertChangelog != null && alertChangelog.isShowing())
			outState.putBoolean(Constants.ABOUT_ALERT_CHANGELOG_KEY, true);

		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy()
	{
		if (alertChangelog != null)
			alertChangelog.dismiss();

		super.onDestroy();
	}

	private void setUI(Bundle savedInstanceState)
	{
		if (savedInstanceState != null)
		{
			if (savedInstanceState.containsKey(Constants.ABOUT_ALERT_CHANGELOG_KEY))
				createAlertChangelog();
		}
	}

	private void fillUI()
	{
		Button button = (Button) getActivity().findViewById(R.id.changelogButton);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		createAlertChangelog();
	}

	private void createAlertChangelog()
	{
		AssetManager assetManager = getActivity().getAssets();
		List<ChangelogEntryModel> versions = null;
		try
		{
			InputStream changelogXml = assetManager.open("changelog/changelog.xml");
			versions = new ChangelogEntryXmlParser(getActivity()).parse(changelogXml);
			Collections.sort(versions);

		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (XmlPullParserException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

		if (versions != null)
		{
			int offset = 0;
			ImageSpan is;
			
			for (ChangelogEntryModel version : versions)
			{
				stringBuilder.append("Version " + version.getVersionName() + " (" + version.getDate() + ")\n");
				stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), offset, stringBuilder.length(), 0);
				offset = stringBuilder.length();

				for (ChangelogEntryItemModel item : version.getItems())
				{					

					switch (item.getType())
					{
						case NEW:
							
							is = new ImageSpan(getActivity(), R.drawable.ic_changelog_new);

							break;

						case FIXED:
						default:
							
							is = new ImageSpan(getActivity(), R.drawable.ic_changelog_fixed);

							break;
					}
					
					stringBuilder.append("-  " + item.getData() + "\n");
					stringBuilder.setSpan(is, offset, offset+1, 0);
					offset = stringBuilder.length();
				}

				stringBuilder.append("\n");
				offset = stringBuilder.length();
			}
		}

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.alertdialog_changelog, null);
		TextView textview = (TextView) view.findViewById(R.id.changelogText);
		textview.setText(stringBuilder);

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle("What's new");
		builder.setView(view);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dialog.dismiss();
			}
		});

		alertChangelog = builder.create();
		alertChangelog.show();
	}
}
