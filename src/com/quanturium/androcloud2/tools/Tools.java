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
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy HH:mm");
	public static final SimpleDateFormat dateFormatSimple = new SimpleDateFormat("MM.dd.yyyy");
	
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
			String[] proj =
			{ MediaStore.Images.Media.TITLE };
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
			String[] proj =
			{ MediaStore.MediaColumns.DATA };
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
}
