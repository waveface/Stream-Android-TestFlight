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
		if(name.toLowerCase().contains("debug") || name.toLowerCase().contains("dev")) {
			resId = R.drawable.logo_debug;
		} else if(name.toLowerCase().contains("release")) {
			resId = R.drawable.logo_release;
		} else if(name.toLowerCase().contains("rc")) {
			resId = R.drawable.logo_rc;
		} else if(name.toLowerCase().contains("testflight")) {
			resId = R.drawable.test_flight;
		}
		return resId;
	}
}
