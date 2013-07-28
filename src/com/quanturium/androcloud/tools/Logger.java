package com.quanturium.androcloud.tools;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.quanturium.androcloud.Constants;

public class Logger {

	private static void log(int priority, String tag, String msg) {
		if (Constants.DEBUG) {
			Log.println(priority, tag, msg);
		} else {
			Crashlytics.log(priority, tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		Logger.log(Log.INFO, tag, msg);
	}

	public static void e(String tag, String msg) {
		Logger.log(Log.ERROR, tag, msg);
	}

	public static void v(String tag, String msg) {
		Logger.log(Log.VERBOSE, tag, msg);
	}

	public static void w(String tag, String msg) {
		Logger.log(Log.WARN, tag, msg);
	}

}
