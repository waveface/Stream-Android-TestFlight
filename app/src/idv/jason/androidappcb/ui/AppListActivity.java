package idv.jason.androidappcb.ui;

import java.io.File;

import idv.jason.androidappcb.Constants;
import idv.jason.androidappcb.R;
import idv.jason.androidappcb.data.AppDataEntity;
import idv.jason.androidappcb.data.AppDataEntity.AppData;
import idv.jason.androidappcb.tasks.DownloadFileTask;
import idv.jason.androidappcb.tasks.GetApkListTask;
import idv.jason.androidappcb.tasks.GetExistApkListTask;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

public class AppListActivity extends ListActivity implements OnItemClickListener {
	public static final String TAG = AppListActivity.class.getSimpleName();
	private String mDownloadPath;
	private ProgressDialog mProgress;
	
	private boolean mDownloading = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		
		setContentView(R.layout.app_list_activity);

		File externalStorage = Environment.getExternalStorageDirectory();
		mDownloadPath = externalStorage.getAbsolutePath() + "/"
				+ Constants.APP_SAVE_DIR;
		
		File file = new File(mDownloadPath);
		if(file.exists() == false)
			file.mkdir();

		if(savedInstanceState == null) {
			GetExistApkListTask getApk = new GetExistApkListTask(AppListActivity.this);
			getApk.execute(mDownloadPath + "/" + Constants.APP_LIST_SAVE_NAME, null, null);
		} else {
			boolean downloading = savedInstanceState.getBoolean(Constants.DATA_DOWNLOADING, false);
			if(downloading)
				showProgressDialog();
		}
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_READ_LIST_COMPLETE);
		filter.addAction(Constants.ACTION_DOWNLOAD_LIST_COMPLETE);
		filter.addAction(Constants.ACTION_DOWNLOAD_APK_COMPLETE);
		filter.addAction(Constants.ACTION_DOWNLOAD_STATUS);
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(Constants.DATA_DOWNLOADING, mDownloading);
	}
	
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	private void setupAdapter(AppDataEntity apps) {
		AppListAdapter adapter = new AppListAdapter(this, apps);
		getListView().setAdapter(adapter);
		
		getListView().setOnItemClickListener(this);
	}
	
	private void dismissProgressDialog() {
		if(mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
			mDownloading = false;
		}
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(Constants.ACTION_READ_LIST_COMPLETE.equals(action)) {
				if(intent.getExtras() != null) {
					AppDataEntity apps = intent.getExtras().getParcelable(Constants.DATA_APK_LIST);
					setupAdapter(apps);
				}

				mProgress = ProgressDialog.show(AppListActivity.this, getString(R.string.downloading), getString(R.string.downloading_apk_list));
				GetApkListTask getApk = new GetApkListTask(AppListActivity.this);
				getApk.execute(mDownloadPath + "/" + Constants.APP_LIST_SAVE_NAME, null, null);
				
			} else if(Constants.ACTION_DOWNLOAD_LIST_COMPLETE.equals(action)) { 
				dismissProgressDialog();
				if(intent.getExtras() != null) {
					ImageView image = (ImageView) findViewById(R.id.image_server_status);
					image.setImageResource(R.drawable.greed_dot);
					AppDataEntity apps = intent.getExtras().getParcelable(Constants.DATA_APK_LIST);
					setupAdapter(apps);
				}
				
			} else if(Constants.ACTION_DOWNLOAD_APK_COMPLETE.equals(action)) {
				dismissProgressDialog();
				String path = intent.getStringExtra(Constants.DATA_FILE_PATH);
				if(path == null) {
					return;
				}
				openApk(new File(path));
			} else if(Constants.ACTION_DOWNLOAD_STATUS.equals(action)) {
				int progress = intent.getIntExtra(Constants.DATA_DOWNLOAD_PROGRESS, 0);
				int maxProgress = intent.getIntExtra(Constants.DATA_DOWNLOAD_MAX_PROGRESS, 0);
				if(mProgress != null) {
					mProgress.setProgress(progress);
					mProgress.setMax(maxProgress);
				}
			}
		}
		
	};

	private void openApk(File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	private void showProgressDialog() {
		mProgress = new ProgressDialog(this);
		mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgress.setTitle(getString(R.string.downloading));
		mProgress.setMessage(getString(R.string.downloading_apk));
		mProgress.setCancelable(false);
		mProgress.setIndeterminate(false);
		mProgress.show();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
		AppData app = (AppData) view.getTag();
		showProgressDialog();
		mDownloading = true;
		DownloadFileTask task = new DownloadFileTask(this, app.path, mDownloadPath, true);
		task.execute(null, null, null);
		
	}
}
