package com.awrtechnologies.valor.valorfireplace.uicomponents;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesManager {

	private static String PREFS_NAME = "vogilaPazz";

	public static String getPreferenceByKey(Context context, String key) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(key, "");
	}

	public static void clearAllPreferences(Context context) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		Editor e = settings.edit();
		e.clear();
		e.commit();
	}

	public static void setPreferenceByKey(Context context, String key,
			String value) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		Editor e = settings.edit();
		e.putString(key, value);
		e.commit();
	}

	public static boolean getPreferenceBooleanByKey(Context context, String key) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		// System.out.println("key : "+settings.getBoolean(key, false));
		return settings.getBoolean(key, false);
	}

	public static void setPreferenceBooleanByKey(Context context, String key,
			boolean value) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		Editor e = settings.edit();
		e.putBoolean(key, value);
		e.commit();
	}

	public static int getPreferenceIntByKey(Context context, String key) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		// System.out.println("key : "+settings.getBoolean(key, false));
		return settings.getInt(key, Integer.MIN_VALUE);
	}

	public static void setPreferenceIntByKey(Context context, String key,
			int value) {
		SharedPreferences settings = context
				.getSharedPreferences(PREFS_NAME, 0);
		Editor e = settings.edit();
		e.putInt(key, value);
		e.commit();
	}
	
	

}
