package com.quanturium.androcloud2.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.cloudapp.api.CloudAppException;
import com.cloudapp.api.model.CloudAppItem;
import com.quanturium.androcloud2.tools.Prefs;

public class FilesDatabase extends SQLiteOpenHelper
{
	private static FilesDatabase	mInstance				= null;

	public final static String		TAG						= "FilesDatabase";

	private final static String		DATABASE_NAME_PREFIX	= "Files_";
	private final static String		DATABASE_NAME_SUFFIX	= ".db";
	private static final int		DATABASE_VERSION		= 8;

	public final static String		COL_ID					= "_id";
	public final static String		COL_DATE				= "orderDate";
	public final static String		COL_URL					= "url";
	public final static String		COL_TYPE				= "type";
	public final static String		COL_DATA				= "json";
	public final static String		COL_NAME				= "name";
	public final static String		COL_IS_TRASHED			= "is_trashed";

	private static final String		TABLE_NAME				= "Files";
	private static final String		TABLE_CREATE			= "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + ", " + COL_URL + ", " + COL_DATE + " INTEGER, " + COL_TYPE + " INTEGER," + COL_DATA + ", " + COL_IS_TRASHED + " INTEGER)";

	public static FilesDatabase getInstance(Context ctx)
	{
		if (mInstance == null)
			mInstance = new FilesDatabase(ctx.getApplicationContext());

		return mInstance;
	}

	public static void clean()
	{
		if (mInstance != null)
			mInstance.close();
		mInstance = null;
	}

	private FilesDatabase(Context context)
	{
		super(context, DATABASE_NAME_PREFIX + Prefs.getPreferences(context).getInt(Prefs.HASH, 0) + DATABASE_NAME_SUFFIX, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public void addFiles(CloudAppItem[] items)
	{
		for (CloudAppItem item : items)
		{
			int id;
			try
			{
				if ((id = getFileId(item.getUrl())) != -1)
					updateFile(id, item);
				else
					addFile(item);

			} catch (CloudAppException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public long addFile(CloudAppItem item)
	{
		ContentValues values = new ContentValues();
		try
		{
			values.put(COL_NAME, item.getName());
			values.put(COL_URL, item.getUrl());
			values.put(COL_DATE, (int) (item.getUpdatedAt().getTime() / 1000));
			values.put(COL_TYPE, item.getItemType().ordinal());
			values.put(COL_DATA, item.getJson().toString());
			values.put(COL_IS_TRASHED, item.isTrashed());

			SQLiteDatabase db = this.getWritableDatabase();
			long id = db.insert(TABLE_NAME, null, values);
			db.close();
			return id;

		} catch (CloudAppException e)
		{
			e.printStackTrace();
		}

		return -1;
	}

	public long updateFile(int id, CloudAppItem item)
	{
		ContentValues values = new ContentValues();
		try
		{
			values.put(COL_NAME, item.getName());
			values.put(COL_URL, item.getUrl());
			values.put(COL_DATE, (int) (item.getUpdatedAt().getTime() / 1000));
			values.put(COL_TYPE, item.getItemType().ordinal());
			values.put(COL_DATA, item.getJson().toString());
			values.put(COL_IS_TRASHED, item.isTrashed());

			SQLiteDatabase db = this.getWritableDatabase();
			db.update(TABLE_NAME, values, COL_ID + "=?", new String[] { String.valueOf(id) });
			db.close();

			return id;

		} catch (CloudAppException e)
		{
			e.printStackTrace();
		}

		return -1;
	}

	public void deleteFiles()
	{
		Log.w(TAG, "deleting all items in FilesDatabase");

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	public int getFileId(String url)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, COL_URL + "=?", new String[] { url }, null, null, null, null);
		
		if (cursor.getCount() == 1)
		{
			cursor.moveToFirst();
			return cursor.getInt(cursor.getColumnIndex(FilesDatabase.COL_ID));
		}

		return -1;
	}

	public Cursor getFile(int fileId, boolean trashed)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, null, COL_ID + "=? AND " + COL_IS_TRASHED + "=?", new String[] { String.valueOf(fileId), trashed ? "1" : "0" }, null, null, null, null);
		
		if(cursor.getCount() == 1)
		{
			cursor.moveToFirst();
			return cursor;
		}		
		
		return null;
	}

	public long getCount()
	{
		return DatabaseUtils.queryNumEntries(this.getReadableDatabase(), TABLE_NAME);
	}

	public static String getQuery(CloudAppItem.Type type, String string, boolean trashed)
	{
		String where;
		String trashedString = trashed ? "1" : "0";

		if (type == null)
		{
			if (string == null || string.length() == 0)
			{
				where = COL_IS_TRASHED + " = " + trashedString;
			}
			else
			{
				where = COL_IS_TRASHED + " = " + trashedString + " AND " + COL_NAME + " LIKE " + "'%" + string + "%'";
			}
		}
		else
		{
			if (string == null || string.length() == 0)
			{
				where = COL_IS_TRASHED + " = " + trashedString + " AND " + COL_TYPE + " = " + String.valueOf(type.ordinal());
			}
			else
			{
				where = COL_IS_TRASHED + " = " + trashedString + " AND " + COL_TYPE + " = " + String.valueOf(type.ordinal()) + " AND " + COL_NAME + " LIKE " + "'%" + string + "%'";
			}
		}

		return SQLiteQueryBuilder.buildQueryString(false, TABLE_NAME, null, where, null, null, COL_DATE + " DESC", null);
	}
}