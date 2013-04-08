package com.quanturium.androcloud.changelog;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.quanturium.androcloud.models.ChangelogEntryItemModel;
import com.quanturium.androcloud.models.ChangelogEntryModel;

public class ChangelogEntryXmlParser extends XmlParser<ChangelogEntryModel>
{

	public ChangelogEntryXmlParser(Context context)
	{
		super(context);
	}

	@Override
	protected ChangelogEntryModel readItem(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		String tagName;
		ChangelogEntryModel entry = new ChangelogEntryModel();

		tagName = parser.getName();
		
		if(tagName.equals("version"))
		{
			entry.setVersionCode(Integer.parseInt(readTagAttribute(tagName, "code", parser)));
			entry.setVersionName(readTagAttribute(tagName, "name", parser));
			entry.setDate(readTagAttribute(tagName, "date", parser));
		}
		
		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
				continue;

			tagName = parser.getName();

			if (tagName.equals("item"))
			{
				entry.addItem(new ChangelogEntryItemModel(readTagAttribute(tagName,"type",parser), readTagString(tagName, parser)));
			}
		}

		return entry;
	}

}
