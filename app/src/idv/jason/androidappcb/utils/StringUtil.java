package idv.jason.androidappcb.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

public class StringUtil {
	public static final String TAG = StringUtil.class.getSimpleName();

	public static String getStringFromFile(File file) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(
			        new InputStreamReader(
			        new FileInputStream(file)));
			if(reader != null) {
				StringBuilder builder = new StringBuilder();
				try {
					for (String line = null; (line = reader.readLine()) != null;) {
						builder.append(line).append("\n");
					}
				} catch (IOException e) {
					Log.e(TAG, "Can't read stream", e);
				}
				reader.close();
				return builder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, "FileNotFoundException", e);
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		}
		return null;
	}
}
