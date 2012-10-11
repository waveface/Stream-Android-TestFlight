package idv.jason.androidappcb.ui;


import java.io.File;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import idv.jason.androidappcb.Constants;
import idv.jason.androidappcb.R;
import idv.jason.androidappcb.data.AppDataEntity;
import idv.jason.androidappcb.data.AppDataEntity.AppData;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter{
	private AppDataEntity mApps;
	private LayoutInflater mInflater;
	private String mDownloadPath;
	
	public AppListAdapter(Context context, AppDataEntity apps) {
		mApps = apps;
		mInflater = LayoutInflater.from(context);
		File externalStorage = Environment.getExternalStorageDirectory();
		mDownloadPath = externalStorage.getAbsolutePath() + "/"
				+ Constants.APP_SAVE_DIR;
	}

	@Override
	public int getCount() {
		return mApps.apps.size();
	}

	@Override
	public Object getItem(int position) {
		return mApps.apps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mApps.apps.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.app_list_item, null);
		}
		AppData app = mApps.apps.get(position);
		ImageView image = (ImageView) convertView.findViewById(R.id.app_ic);
		if(app.name.toLowerCase().contains("debug") || app.name.toLowerCase().contains("dev")) {
			image.setImageResource(R.drawable.logo_debug);
		} else if(app.name.toLowerCase().contains("release")) {
			image.setImageResource(R.drawable.logo_release);
		} else if(app.name.toLowerCase().contains("rc")) {
			image.setImageResource(R.drawable.logo_rc);
		} else if(app.name.toLowerCase().contains("testflight")) {
			image.setImageResource(R.drawable.test_flight);
		}
		
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
		DateTime date = parser.parseDateTime(app.createdDate);
		StringBuilder builder = new StringBuilder();
		builder.append(date.getYear());
		builder.append("/");
		builder.append(date.getMonthOfYear());
		builder.append("/");
		builder.append(date.getDayOfMonth());
		
		TextView tv = (TextView) convertView.findViewById(R.id.createDate);
		tv.setText(builder.toString());
		
		tv = (TextView) convertView.findViewById(R.id.appName);
		tv.setText(app.name);
		
		tv = (TextView) convertView.findViewById(R.id.version);
		tv.setText(app.versionName + " (" + app.versionCode + ")");

		image = (ImageView)convertView.findViewById(R.id.image_exist);
		if(new File(mDownloadPath + "/" + app.apkName).exists())
			image.setVisibility(View.VISIBLE);
		else
			image.setVisibility(View.INVISIBLE);
		
		convertView.setTag(app);
		
		return convertView;
	}

}
