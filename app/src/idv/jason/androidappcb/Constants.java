package idv.jason.androidappcb;

public class Constants {
	public static final String APP_SAVE_DIR = "TestFlight";
	public static final String APP_LIST_SAVE_NAME = "installers.json";
	public static final String DOWNLOAD_PATH_LIST_PREFIX = "GetInstallers";
	public static final String DOWNLOAD_PATH_APK_PREFIX = "DownloadAPK";
	
	public static final String SERVER_LOCAL = "http://192.168.1.144:8080/Stream-Android-TestFlight-Server/";
	public static final String SERVER_CI = 	"http://192.168.1.98:8081/Stream-Android-TestFlight-Server/";
	public static final String SERVER_S3 = "http://wammer-pkgs.s3.amazonaws.com/";
	
	
	public static final String ACTION_DOWNLOAD_APK_COMPLETE = "idv.jason.androidcb.action.DOWNLOAD_APK_COMPLETE";
	public static final String ACTION_READ_LIST_COMPLETE = "idv.jason.androidcb.action.READ_LIST_COMPLETE";
	public static final String ACTION_DOWNLOAD_LIST_COMPLETE = "idv.jason.androidcb.action.DOWNLOAD_LIST_COMPLETE";
	public static final String ACTION_DOWNLOAD_LIST_FAIL = "idv.jason.androidcb.action.DOWNLOAD_LIST_FAIL";
	
	public static final String ACTION_DOWNLOAD_STATUS = "idv.jason.androidcb.action.DOWNLOAD_STATUS	";
	
	public static final String DATA_APP = "idv.jason.androidcb.data.APP";
	public static final String DATA_APK_LIST = "idv.jason.androidcb.data.APK_LIST";
	public static final String DATA_FILE_PATH = "idv.jason.androidcb.data.FILE_PATH";
	public static final String DATA_DOWNLOAD_PROGRESS = "idv.jason.androidcb.data.DOWNLOAD_PROGRESS";
	public static final String DATA_DOWNLOAD_MAX_PROGRESS = "idv.jason.androidcb.data.DOWNLOAD_MAX_PROGRESS";
	public static final String DATA_DOWNLOADING = "idv.jason.androidcb.data.DOWNLOADING";
}
