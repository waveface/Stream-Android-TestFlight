package idv.jason.androidappcb.ui;

import idv.jason.androidappcb.Constants;
import idv.jason.androidappcb.R;
import idv.jason.androidappcb.RuntimeData;
import idv.jason.androidappcb.TestFlight;
import idv.jason.androidappcb.data.AppDataEntity;
import idv.jason.androidappcb.data.AppData;
import idv.jason.androidappcb.tasks.DownloadFileTask;
import idv.jason.androidappcb.tasks.GetApkListTask;
import idv.jason.androidappcb.tasks.GetExistApkListTask;
import idv.jason.androidappcb.utils.Utils;

import java.io.File;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AppListFragment extends ListFragment implements
		OnItemClickListener {
	public static final String TAG = AppListFragment.class.getSimpleName();
	private String mDownloadPath;
	private ProgressDialog mProgress;

	private boolean mDownloading = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.app_list_fragment, container,
				false);
		return view;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setHasOptionsMenu(true);
		File externalStorage = Environment.getExternalStorageDirectory();
		mDownloadPath = externalStorage.getAbsolutePath() + "/"
				+ Constants.APP_SAVE_DIR;

		File file = new File(mDownloadPath);
		if (file.exists() == false)
			file.mkdir();

		if (savedInstanceState == null) {
			mProgress = ProgressDialog.show(getActivity(),
					getString(R.string.downloading),
					getString(R.string.downloading_apk_list));
			TestFlight app = (TestFlight) getActivity().getApplication();
			app.getApkList();
		} else {
			boolean downloading = savedInstanceState.getBoolean(
					Constants.DATA_DOWNLOADING, false);
			if (downloading)
				showProgressDialog();
		}

		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_READ_LIST_COMPLETE);
		filter.addAction(Constants.ACTION_DOWNLOAD_LIST_COMPLETE);
		filter.addAction(Constants.ACTION_DOWNLOAD_LIST_FAIL);
		filter.addAction(Constants.ACTION_DOWNLOAD_APK_COMPLETE);
		filter.addAction(Constants.ACTION_DOWNLOAD_APK_FAIL);
		filter.addAction(Constants.ACTION_DOWNLOAD_STATUS);
		getActivity().registerReceiver(mReceiver, filter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(Constants.DATA_DOWNLOADING, mDownloading);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	View.OnClickListener mImageClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			AppData app = (AppData) ((View) v.getParent()).getTag();
			ApkInfoDialog dialog = new ApkInfoDialog();
			Bundle data = new Bundle();
			data.putParcelable(Constants.DATA_APP, app);
			dialog.setArguments(data);
			dialog.show(getFragmentManager(), "details");
		}
	};

	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mReceiver);
	}

	private void setupAdapter(AppDataEntity apps) {
		AppListAdapter adapter = (AppListAdapter) getListView().getAdapter();
		if (adapter != null)
			adapter = new AppListAdapter(getActivity(), apps,
					adapter.getApps(), mImageClickListener);
		else
			adapter = new AppListAdapter(getActivity(), apps, null,
					mImageClickListener);
		getListView().setAdapter(adapter);

		getListView().setOnItemClickListener(this);
	}

	private void dismissProgressDialog() {
		if (mProgress != null) {
			mProgress.dismiss();
			mProgress = null;
			mDownloading = false;
		}
	}

	private void setupServerInfo() {
		TextView tv = (TextView) getActivity().findViewById(
				R.id.text_server_location);
		if (Constants.SERVER_LOCAL.equals(RuntimeData.SERVER_PATH)) {
			tv.setText(getActivity().getString(R.string.server_location,
					"LOCAL"));
		} else if (Constants.SERVER_CI.equals(RuntimeData.SERVER_PATH)) {
			tv.setText(getActivity().getString(R.string.server_location, "CI"));
		} else if (Constants.SERVER_S3.equals(RuntimeData.SERVER_PATH)) {
			tv.setText(getActivity().getString(R.string.server_location, "S3"));
		} else {
			tv.setText(getActivity()
					.getString(R.string.server_location, "fail"));
		}
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constants.ACTION_READ_LIST_COMPLETE.equals(action)) {
				setupServerInfo();
				if (intent.getExtras() != null) {
					AppDataEntity apps = intent.getExtras().getParcelable(
							Constants.DATA_APK_LIST);
					setupAdapter(apps);
				}
			} else if(Constants.ACTION_DOWNLOAD_LIST_FAIL.equals(action)) {
				dismissProgressDialog();
				setupServerInfo();
			} else if (Constants.ACTION_DOWNLOAD_LIST_COMPLETE.equals(action)) {
				if (intent.getExtras() != null) {
					dismissProgressDialog();
					setupServerInfo();
					ImageView image = (ImageView) getActivity().findViewById(
							R.id.image_server_status);
					image.setImageResource(R.drawable.green_dot);
					AppDataEntity apps = intent.getExtras().getParcelable(
							Constants.DATA_APK_LIST);
					setupAdapter(apps);
				}

			} else if (Constants.ACTION_DOWNLOAD_APK_COMPLETE.equals(action)) {
				dismissProgressDialog();
				String path = intent.getStringExtra(Constants.DATA_FILE_PATH);
				if (path == null) {
					return;
				}
				AppListAdapter adapter = (AppListAdapter) getListView()
						.getAdapter();
				adapter.notifyDataSetChanged();
				openApk(new File(path));
			} else if (Constants.ACTION_DOWNLOAD_STATUS.equals(action)) {
				int progress = intent.getIntExtra(
						Constants.DATA_DOWNLOAD_PROGRESS, 0);
				int maxProgress = intent.getIntExtra(
						Constants.DATA_DOWNLOAD_MAX_PROGRESS, 0);
				if (mProgress != null) {
					mProgress.setProgress(progress);
					mProgress.setMax(maxProgress);
				}
			} else if(Constants.ACTION_DOWNLOAD_APK_FAIL.equals(action)) {
				dismissProgressDialog();
				Toast.makeText(getActivity(), getString(R.string.download_fail), Toast.LENGTH_SHORT).show();
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
		mProgress = new ProgressDialog(getActivity());
		mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgress.setTitle(getString(R.string.downloading));
		mProgress.setMessage(getString(R.string.downloading_apk));
		mProgress.setCancelable(false);
		mProgress.setIndeterminate(false);
		mProgress.show();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position,
			long arg3) {
		AppData app = (AppData) view.getTag();
		showProgressDialog();
		mDownloading = true;
		DownloadFileTask task = new DownloadFileTask(getActivity(), app.path,
				mDownloadPath, Utils.getApkName(app));
		task.execute(null, null, null);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mProgress = ProgressDialog.show(getActivity(),
				getString(R.string.downloading),
				getString(R.string.downloading_apk_list));
		TestFlight app = (TestFlight) getActivity().getApplication();
		app.getApkList();
		return true;
	}
}
