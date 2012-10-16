package idv.jason.androidappcb.tasks;

import idv.jason.androidappcb.Constants;
import idv.jason.androidappcb.data.AppDataEntity;
import idv.jason.androidappcb.data.AppData;
import idv.jason.androidappcb.network.HttpInvoker;
import idv.jason.androidappcb.utils.FIleUtils;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class GetApkListTask extends AsyncTask<String, Void, AppDataEntity>{
	public static final String TAG = GetApkListTask.class.getSimpleName();
	private Context mContext;
	private AppDataEntity mSourceList;

	public GetApkListTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected AppDataEntity doInBackground(String... arg0) {
		String path = arg0[0];
		String filename = arg0[1];
		String json = HttpInvoker.getStringFromUrl(path);
		if(json!=null && !json.equals("ConnectTimeoutException")) {
			FIleUtils.writeFile(filename, json, false);
			mSourceList = new Gson().fromJson(json, AppDataEntity.class);
			if(mSourceList != null && mSourceList.apps != null) {
				for(int i=0; i<mSourceList.apps.size(); ++i) {
					AppData app = mSourceList.apps.get(i);
					app.apkName = app.path.substring(app.path.lastIndexOf("=") +1); 
				}
				return mSourceList;
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(AppDataEntity param) {
		Intent intent = new Intent(Constants.ACTION_DOWNLOAD_LIST_COMPLETE);
		if(param != null) {
			Bundle data = new Bundle();
			data.putParcelable(Constants.DATA_APK_LIST, mSourceList);
			intent.putExtras(data);
		}
		mContext.sendBroadcast(intent);
    }
}
