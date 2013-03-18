package com.quanturium.androcloud2.tools;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Tools
{
	public static final SimpleDateFormat	dateFormat			= new SimpleDateFormat("MM.dd.yyyy HH:mm");
	public static final SimpleDateFormat	dateFormatSimple	= new SimpleDateFormat("MM.dd.yyyy");

	private static final long				SECOND				= 1000;
	private static final long				MINUTE				= SECOND * 60;
	private static final long				HOUR				= MINUTE * 60;
	private static final long				DAY					= HOUR * 24;
	private static final long				MONTH				= DAY * 30;
	private static final long				YEAR				= DAY * 365;

	public static String implode(String delim, String[] args)
	{
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < args.length; i++)
		{
			if (i > 0)
				sb.append(delim);

			sb.append(args[i]);
		}

		return sb.toString();
	}

	public static String md5(String key)
	{
		byte[] uniqueKey = key.getBytes();
		byte[] hash = null;
		StringBuffer hashString = new StringBuffer();
		try
		{

			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);

			for (int i = 0; i < hash.length; ++i)
			{
				String hex = Integer.toHexString(hash[i]);
				if (hex.length() == 1)
				{
					hashString.append('0');
					hashString.append(hex.charAt(hex.length() - 1));
				}
				else
				{
					hashString.append(hex.substring(hex.length() - 2));
				}
			}

		} catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hashString.toString();
	}

	public static String getFilename(Context context, Uri uri)
	{
		String fileName = "";
		String scheme = uri.getScheme();

		if (scheme.equals("file"))
		{
			fileName = uri.getLastPathSegment();
		}
		else if (scheme.equals("content"))
		{
			String[] proj = { MediaStore.Images.Media.TITLE };
			Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

			if (cursor != null && cursor.getCount() != 0)
			{
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
				cursor.moveToFirst();
				fileName = cursor.getString(columnIndex);
			}
		}
		else
		{
			return null;
		}

		return fileName;
	}

	public static File uriToFile(Context context, Uri uri)
	{
		File file = null;
		String scheme = uri.getScheme();

		if (scheme.equals("file"))
		{
			file = new File(uri.getPath());
		}
		else if (scheme.equals("content"))
		{
			String[] proj = { MediaStore.MediaColumns.DATA };
			Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);

			if (cursor != null && cursor.getCount() != 0)
			{
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
				cursor.moveToFirst();
				file = new File(cursor.getString(columnIndex));
			}
		}
		else
		{
			return null;
		}

		return file;
	}

	public static String getDateFormated(Date date, SimpleDateFormat format)
	{
		return format.format(date);
	}

	public static String getElapsedTimeFrom(Date date)
	{
		long a1 = System.currentTimeMillis();
		long a2 = date.getTime();
		long diff = a1 - a2;
		int value = 0;

		if (diff / YEAR > 0)
		{
			value = (int) (diff / YEAR);
			return  value + " year" + (value > 1 ? "s" : "") + " ago";
		}
		else if (diff / MONTH > 0)
		{
			value = (int) (diff / MONTH);
			return  value + " month" + (value > 1 ? "s" : "") + " ago";
		}
		else if (diff / DAY > 0)
		{
			value = (int) (diff / DAY);
			return  value + " day" + (value > 1 ? "s" : "") + " ago";
		}
		else if (diff / HOUR > 0)
		{
			value = (int) (diff / HOUR);
			return  value + " hour" + (value > 1 ? "s" : "") + " ago";
		}
		else if (diff / MINUTE > 0)
		{
			value = (int) (diff / MINUTE);
			return  value + " minute" + (value > 1 ? "s" : "") + " ago";
		}
		else if(diff / SECOND > 0)
		{
			value = (int) (diff / SECOND);
			return  value + " second" + (value > 1 ? "s" : "") + " ago";
		}
		else
		{
			return "Just now";
		}
	}
}
