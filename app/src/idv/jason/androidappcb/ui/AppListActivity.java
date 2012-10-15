package idv.jason.androidappcb.ui;

import idv.jason.androidappcb.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class AppListActivity extends FragmentActivity {
	public static final String TAG = AppListActivity.class.getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.app_list_activity);
		if(savedInstanceState == null) {
			AppListFragment fragment = new AppListFragment();
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.container_root, fragment).commit();
		}
	}
}
