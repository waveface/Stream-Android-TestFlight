package idv.jason.androidappcb.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import idv.jason.androidappcb.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class DownloadFileTask extends AsyncTask<Void, Integer, Void>{
	public static String TAG = DownloadFileTask.class.getSimpleName();
	
	private String mSavePath;
	private String mDownloadPath;
	private Context mContext;
	private File mDownloadedFile = null;
	
	private int mTotalSize = 0;
	private int mCurrentSize = 0;
	
	private String mFileName;
	
	public DownloadFileTask(Context context, String downloadPath, String savepath, String fileName) {
		Log.d(TAG, "path:" + downloadPath);
		mContext = context;
		mSavePath = savepath;
		mDownloadPath = downloadPath;
		mFileName = fileName;
	}

	@Override
	protected Void doInBackground(Void... params) {
		publishProgress(0);
		mDownloadedFile = getFileFromUrl(mDownloadPath, mSavePath, mFileName);
		return null;
	}
	
	public File getFileFromUrl(String url, String saveLocation, String fileName) {
		File savePath = new File(saveLocation);
		if(savePath.exists() == false)
			savePath.mkdir();
		File file = new File(saveLocation + "/" + fileName);
		if(file.exists()) {
			return file;
		}
		
		InputStream is = null;
		try {
			URL myURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();   
	        conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
	        conn.connect();   
	        is = conn.getInputStream();   
	        mTotalSize = conn.getContentLength();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
		if (is == null) {
			return null;
		}
		
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			long lastTime = 0, currTime = 0;
			byte buf[] = new byte[1024];
			do {
				int numread = is.read(buf);
				if (numread <= 0) {
					break;
				}
				mCurrentSize += numread;
				currTime = System.currentTimeMillis();
				if(currTime - lastTime > 100) {
					lastTime = currTime;
					publishProgress(mCurrentSize, mTotalSize);
				}
				fos.write(buf, 0, numread);
			} while (true);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		Intent intent = new Intent(Constants.ACTION_DOWNLOAD_STATUS);
		Log.d(TAG, "value="+values[0].toString());
		intent.putExtra(Constants.DATA_DOWNLOAD_PROGRESS, values[0]);
		if(values.length > 1)
			intent.putExtra(Constants.DATA_DOWNLOAD_MAX_PROGRESS, values[1]);
		mContext.sendBroadcast(intent);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Intent intent = new Intent(Constants.ACTION_DOWNLOAD_APK_COMPLETE);
		if(mDownloadedFile != null) {
			intent.putExtra(Constants.DATA_FILE_PATH, mDownloadedFile.getAbsolutePath());
		}
		mContext.sendBroadcast(intent);
	}

}
