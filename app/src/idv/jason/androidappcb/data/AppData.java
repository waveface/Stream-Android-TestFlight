package idv.jason.androidappcb.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;


public class AppData implements Parcelable{

	@SerializedName("name")
	public String name;
	
	@SerializedName("versionCode")
	public String versionCode;
	
	@SerializedName("versionName")
	public String versionName;
	
	@SerializedName("createdDate")
	public String createdDate;
	
	@SerializedName("path")
	public String path;
	
	@SerializedName("features")
	public String features;
	
	@SerializedName("buildNumber")
	public String buildNumber;
	
	public String apkName;
	
	public static final Parcelable.Creator<AppData> CREATOR = new Parcelable.Creator<AppData>() {
		@Override
		public AppData createFromParcel(Parcel in) {
			return new AppData(in);
		}

		@Override
		public AppData[] newArray(int size) {
			return new AppData[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
	
	public AppData() {
		
	}
	
	public AppData(Parcel in) {
		in.writeString(name);
		in.writeString(versionCode);
		in.writeString(versionName);
		in.writeString(createdDate);
		in.writeString(path);
		in.writeString(features);
		in.writeString(buildNumber);
		in.writeString(apkName);
	}

	@Override
	public void writeToParcel(Parcel out, int flag) {
		out.writeString(name);
		out.writeString(versionCode);
		out.writeString(versionName);
		out.writeString(createdDate);
		out.writeString(path);
		out.writeString(features);
		out.writeString(buildNumber);
		out.writeString(apkName);
	}
}
