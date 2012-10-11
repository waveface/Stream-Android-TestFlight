package idv.jason.androidappcb.ui;


import idv.jason.androidappcb.R;
import idv.jason.androidappcb.data.AppDataEntity;
import idv.jason.androidappcb.data.AppDataEntity.AppData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter{
	private AppDataEntity mApps;
	private LayoutInflater mInflater;
	
	public AppListAdapter(Context context, AppDataEntity apps) {
		mApps = apps;
		mInflater = LayoutInflater.from(context);
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
		}
		TextView tv = (TextView) convertView.findViewById(R.id.createDate);
		tv.setText(app.createdDate);
		
		tv = (TextView) convertView.findViewById(R.id.appName);
		tv.setText(app.name);
		
		tv = (TextView) convertView.findViewById(R.id.version);
		tv.setText(app.versionCode);
		
		convertView.setTag(app.path);
		
		return convertView;
	}

}
