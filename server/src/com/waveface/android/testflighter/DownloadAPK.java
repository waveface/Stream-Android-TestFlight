package com.waveface.android.testflighter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadAPK extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BYTES_DOWNLOAD = 1024;
	private String mAPKPath; 
	public void init(ServletConfig config) throws ServletException {
		mAPKPath = config.getInitParameter(Constant.APK_PATH);
    	RuntimeData.APK_PATH = mAPKPath;
    }
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String filename=request.getParameter("filename");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment;filename="+filename);
		File apkFile = new File(RuntimeData.APK_PATH+File.separator+filename);
		InputStream is =  new FileInputStream(apkFile);
		response.setContentLength((int) apkFile.length());
		int read = 0;
		byte[] bytes = new byte[BYTES_DOWNLOAD];
		OutputStream os = response.getOutputStream();

		while ((read = is.read(bytes)) != -1) {
			os.write(bytes, 0, read);
		}
		is.close();
		os.flush();
		os.close();
	}
}
