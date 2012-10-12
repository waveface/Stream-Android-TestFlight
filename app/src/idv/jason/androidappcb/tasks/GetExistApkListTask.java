package idv.jason.androidappcb.tasks;

import idv.jason.androidappcb.Constants;
import idv.jason.androidappcb.data.AppDataEntity;
import idv.jason.androidappcb.data.AppDataEntity.AppData;
import idv.jason.androidappcb.utils.FIleUtils;

import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class GetExistApkListTask extends AsyncTask<String, Void, AppDataEntity>{
	public static final String TAG = GetExistApkListTask.class.getSimpleName();
	private Context mContext;

	public GetExistApkListTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected AppDataEntity doInBackground(String... arg0) {
		String path = arg0[0];
		
		String json = FIleUtils.readFile(path);
		AppDataEntity appDataEntity = new Gson().fromJson(json, AppDataEntity.class);
		

		if(appDataEntity != null && appDataEntity.apps != null) {
			for(int i=0; i<appDataEntity.apps.size(); ++i) {
				AppData app = appDataEntity.apps.get(i);
				app.apkName = app.path.substring(app.path.lastIndexOf("=") +1); 
			}
			return appDataEntity;
		}
		return null;
	}

	@Override
	protected void onPostExecute(AppDataEntity appDataEntity) {
		Intent intent = new Intent(Constants.ACTION_READ_LIST_COMPLETE);
		if(appDataEntity != null) {
			Bundle data = new Bundle();
			data.putParcelable(Constants.DATA_APK_LIST, appDataEntity);
			intent.putExtras(data);
		}
		mContext.sendBroadcast(intent);
    }
}
