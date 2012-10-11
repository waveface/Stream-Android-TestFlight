package idv.jason.androidappcb.utils;

import idv.jason.androidappcb.data.AppDataEntity.AppData;

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
		return name + "_" + app.versionCode + "_"+ app.versionName + "." + extension;
	}
}
