package idv.jason.androidappcb.utils;

import idv.jason.androidappcb.R;
import idv.jason.androidappcb.data.AppData;

public class Utils {
	public static String getApkName(AppData app) {
		int dotPos = app.apkName.lastIndexOf(".");
		if(dotPos == -1)
			return null;
		String extension = app.apkName
				.substring(dotPos + 1, app.apkName.length())
				.toLowerCase();
		String name = app.apkName.substring(0,	dotPos);
		if(extension == null || extension.length() == 0 ||
				name == null || name.length() == 0)
			return null;
		return name + "." + extension;
	}
	
	public static int getIconFromName(String name) {
		int resId = -1;
		if(name.toLowerCase().contains("daily")) {
			if(name.toLowerCase().contains("production")) {
				resId = R.drawable.daily_production;
			} else {
				resId = R.drawable.daily_develop;
			}
		} else if(name.toLowerCase().contains("internal")) {
			if(name.toLowerCase().contains("production")) {
				resId = R.drawable.internal_production;
			} else {
				resId = R.drawable.internal_develop;
			}
		} else if(name.toLowerCase().contains("rc")) {
			if(name.toLowerCase().contains("jelleybean")) {
				resId = R.drawable.rc_jb;
			} else {
				resId = R.drawable.rc_gb;
			}
		} else if(name.toLowerCase().contains("testflight")) {
			resId = R.drawable.test_flight;
		}
		return resId;
	}
}
