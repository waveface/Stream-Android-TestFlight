package com.waveface.android.testflighter.model;

import com.google.gson.annotations.SerializedName;

public class Installer {

	@SerializedName("name")
	public String name;

	@SerializedName("versionCode")
	public String versionCode;

	@SerializedName("versionName")
	public String versionName;

	@SerializedName("createdDate")
	public String createdDate;

	@SerializedName("image")
	public String image;

	@SerializedName("apk")
	public String apk;

	@SerializedName("path")
	public String path;
	
	@SerializedName("features")
	public String features;
	
	@SerializedName("buildNumber")
	public String buildNumber;
}