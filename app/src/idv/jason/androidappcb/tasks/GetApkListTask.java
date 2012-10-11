package idv.jason.androidappcb.tasks;

import idv.jason.androidappcb.Constants;
import idv.jason.androidappcb.data.AppDataEntity;
import idv.jason.androidappcb.network.HttpInvoker;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class GetApkListTask extends AsyncTask<Void, Void, Void>{
	public static final String TAG = GetApkListTask.class.getSimpleName();
	private Context mContext;
	private AppDataEntity mSourceList;

	public GetApkListTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected Void doInBackground(Void... arg0) {
		String jsonOutput = HttpInvoker.getStringFromUrl(Constants.DOWNLOAD_PATH_LIST_PREFIX);
		if(jsonOutput!=null && !jsonOutput.equals("ConnectTimeoutException")) {
			mSourceList = new Gson().fromJson(jsonOutput, AppDataEntity.class);
			if(mSourceList != null) {
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void param) {
		Intent intent = new Intent(Constants.ACTION_DOWNLOAD_LIST_COMPLETE);
		if(mSourceList != null) {
			Bundle data = new Bundle();
			data.putParcelable(Constants.DATA_APK_LIST, mSourceList);
			intent.putExtras(data);
		}
		mContext.sendBroadcast(intent);
    }
}
