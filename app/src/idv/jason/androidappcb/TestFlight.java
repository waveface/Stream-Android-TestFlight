package idv.jason.androidappcb;

import java.io.File;

import idv.jason.androidappcb.tasks.GetApkListTask;
import idv.jason.androidappcb.tasks.GetExistApkListTask;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class TestFlight extends Application{
	public static final String TAG = TestFlight.class.getSimpleName();
	private String mDownloadPath;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		File externalStorage = Environment.getExternalStorageDirectory();
		mDownloadPath = externalStorage.getAbsolutePath() + "/"
				+ Constants.APP_SAVE_DIR;
	}
	
	public void getApkList() {
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_DOWNLOAD_LIST_COMPLETE);
		filter.addAction(Constants.ACTION_READ_LIST_COMPLETE);
		registerReceiver(mReceiver, filter);
		
		RuntimeData.SERVER_PATH = Constants.SERVER_LOCAL;
		RuntimeData.INSTALLER_PATH = Constants.DOWNLOAD_PATH_LIST_PREFIX;
		RuntimeData.APK_PATH = Constants.DOWNLOAD_PATH_APK_PREFIX;
		
		GetExistApkListTask getApk = new GetExistApkListTask(TestFlight.this);
		getApk.execute(mDownloadPath + "/" + Constants.APP_LIST_SAVE_NAME, null, null);
		
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Bundle extras = intent.getExtras();
			if(Constants.ACTION_READ_LIST_COMPLETE.equals(action)) {
				testServer(RuntimeData.SERVER_PATH + RuntimeData.INSTALLER_PATH);				
			} else if(Constants.ACTION_DOWNLOAD_LIST_COMPLETE.equals(action)) {
				if(extras != null) {
					Log.d(TAG, "get app");
					unregisterReceiver(mReceiver);					
				} else {
					if(RuntimeData.SERVER_PATH.equals(Constants.SERVER_LOCAL)) {
						RuntimeData.SERVER_PATH = Constants.SERVER_CI;
						RuntimeData.INSTALLER_PATH = Constants.DOWNLOAD_PATH_LIST_PREFIX;
						RuntimeData.APK_PATH = Constants.DOWNLOAD_PATH_APK_PREFIX;
						
						testServer(RuntimeData.SERVER_PATH + RuntimeData.INSTALLER_PATH);
					} else if(RuntimeData.SERVER_PATH.equals(Constants.SERVER_CI)) {
						RuntimeData.SERVER_PATH = Constants.SERVER_S3;
						RuntimeData.INSTALLER_PATH = "installers.json";
						RuntimeData.APK_PATH = "";
						
						testServer(RuntimeData.SERVER_PATH + RuntimeData.INSTALLER_PATH);
					} else {
						RuntimeData.SERVER_PATH = "";
						RuntimeData.INSTALLER_PATH = "";
						RuntimeData.APK_PATH = "";
						unregisterReceiver(mReceiver);
						sendBroadcast(new Intent(Constants.ACTION_DOWNLOAD_LIST_FAIL));
					}
				}
			}
		}
	};
	
	private void testServer(String server) {
		GetApkListTask getApk = new GetApkListTask(this);
		getApk.execute(server, mDownloadPath + "/" + Constants.APP_LIST_SAVE_NAME, null, null);
	}

}
