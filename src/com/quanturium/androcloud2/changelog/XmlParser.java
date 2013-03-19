package com.quanturium.androcloud2.changelog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

public abstract class XmlParser<T>
{
	protected static final String	ROOT_TAG	= "root";
	protected static final String	ITEM_TAG	= "version";
	protected static final String	ns			= null;		// We don't use namespaces
	protected Context				context;

	public XmlParser(Context context)
	{
		this.context = context;
	}

	public List<T> parse(InputStream in) throws XmlPullParserException, IOException
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readData(parser);
		} finally
		{
			in.close();
		}
	}

	protected List<T> readData(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		List<T> items = new ArrayList<T>();

		parser.require(XmlPullParser.START_TAG, ns, ROOT_TAG);

		while (parser.next() != XmlPullParser.END_TAG)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
				continue;

			String name = parser.getName();

			// Starts by looking for the item_tag
			if (name.equals(ITEM_TAG))
			{
				items.add(readItem(parser));
			}
			else
			{
				skip(parser);
			}
		}

		return items;
	}

	protected abstract T readItem(XmlPullParser parser) throws XmlPullParserException, IOException;

	protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		if (parser.getEventType() != XmlPullParser.START_TAG)
			throw new IllegalStateException();

		int depth = 1;

		while (depth != 0)
		{
			switch (parser.next())
			{
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}

	protected String readTagString(String tagName, XmlPullParser parser) throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, ns, tagName);

		String result = null;
		if (parser.next() == XmlPullParser.TEXT)
		{
			result = parser.getText();

			if (result.equals(""))
				result = null;

			parser.nextTag();
		}

		parser.require(XmlPullParser.END_TAG, ns, tagName);

		return result;
	}

	protected int readTagRessource(String tagName, XmlPullParser parser) throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, ns, tagName);

		String text = "";
		int result = 0;
		if (parser.next() == XmlPullParser.TEXT)
		{
			text = parser.getText();
			String[] values = text.split("\\.");

			if (values.length == 3)
				result = context.getResources().getIdentifier(values[2], values[1], context.getPackageName());

			parser.nextTag();
		}

		parser.require(XmlPullParser.END_TAG, ns, tagName);

		return result;
	}

	protected String readTagAttribute(String tagName, String attributeName, XmlPullParser parser) throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, ns, tagName);

		String result = "";

		int acount = parser.getAttributeCount();
		if (acount != -1)
		{
			for (int x = 0; x < acount; x++)
			{
				if (parser.getAttributeName(x).equals(attributeName))
					result = parser.getAttributeValue(x);
			}
		}

		return result;
	}
}
