package idv.jason.androidappcb.ui;

import idv.jason.androidappcb.Constants;
import idv.jason.androidappcb.R;
import idv.jason.androidappcb.data.AppData;
import idv.jason.androidappcb.utils.Utils;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class ApkInfoDialog extends DialogFragment{
	private AppData mApp;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.apk_detail_dialog, container, false);
		mApp = getArguments().getParcelable(Constants.DATA_APP);

		getDialog().setTitle(mApp.name);
		getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);
		getDialog().setCanceledOnTouchOutside(true);
		
		TextView tv = (TextView) view.findViewById(R.id.text_version_code_content);
		tv.setText(mApp.versionCode);
		tv = (TextView) view.findViewById(R.id.text_version_name_content);
		tv.setText(mApp.versionName);
		tv = (TextView) view.findViewById(R.id.text_create_date_content);
		tv.setText(mApp.createdDate);
		tv = (TextView) view.findViewById(R.id.text_change_log_content);
		tv.setText(mApp.features);
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		int iconId = Utils.getIconFromName(mApp.name);
		if(iconId != -1)
			getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, iconId);
	}
}
