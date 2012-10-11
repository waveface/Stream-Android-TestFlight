package com.waveface.android.testflighter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.waveface.android.testflighter.util.FIleUtils;
import com.waveface.android.testflighter.model.*;

public class GetInstallers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mStorePath; 
	public void init(ServletConfig config) throws ServletException {
    	mStorePath = config.getInitParameter(Constant.STORE_PATH);
    	RuntimeData.SOTRED_PATH = mStorePath;
    	//GET JSON INSTALLERS OBJECT SAVED
		String json = FIleUtils.readFile(RuntimeData.SOTRED_PATH+File.separator+Constant.STORE_JSON_NAME);
		if(json != null)
			RuntimeData.installers = new Gson().fromJson(json, InstallersResponse.class);
		else
			RuntimeData.installers = new InstallersResponse();
		
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
//		String json = FIleUtils.readFile(RuntimeData.SOTRED_PATH+File.separator+Constant.STORE_JSON_NAME);
		String json = new Gson().toJson(RuntimeData.installers);
		PrintWriter out = response.getWriter();
		out.print(json); 
	}
}
