package idv.jason.androidappcb.data;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AppDataEntity implements Parcelable{
	public static final Parcelable.Creator<AppDataEntity> CREATOR = new Parcelable.Creator<AppDataEntity>() {
		@Override
		public AppDataEntity createFromParcel(Parcel in) {
			return new AppDataEntity(in);
		}

		@Override
		public AppDataEntity[] newArray(int size) {
			return new AppDataEntity[size];
		}
	};
		
	@SerializedName("installers")
	public List<AppData> apps;
	
	
	public AppDataEntity(Parcel in) {
		int size = in.readInt();
		apps = new ArrayList<AppData>();
		for(int i=0; i<size; i++) {
			AppData app = new AppData();
			app.name = in.readString();
			app.versionCode = in.readString();
			app.versionName = in.readString();
			app.createdDate = in.readString();
			app.path = in.readString();
			app.features = in.readString();
			app.apkName = in.readString();
			app.buildNumber = in.readString();
			apps.add(app);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(apps.size());
		for(int i=0; i<apps.size(); ++i) {
			out.writeString(apps.get(i).name);
			out.writeString(apps.get(i).versionCode);
			out.writeString(apps.get(i).versionName);
			out.writeString(apps.get(i).createdDate);
			out.writeString(apps.get(i).path);
			out.writeString(apps.get(i).features);
			out.writeString(apps.get(i).apkName);
			out.writeString(apps.get(i).buildNumber);
		}
		
	}
}
