package com.waveface.android.testflighter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.waveface.android.testflighter.model.Installer;
import com.waveface.android.testflighter.util.StringUtil;

/**
 * Servlet implementation class UpdateRelease
 */
public class UpdateRelease extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String mHttpHeader =null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateRelease() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
    	if(mHttpHeader==null){
    		mHttpHeader = request.getScheme()+"://"+request.getRemoteAddr()+":"
    			+request.getLocalPort()+request.getContextPath()+Constant.DOWNLOAD_SERVLET;
    	}
    	response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter(Constant.NAME);
		String versionCode = request.getParameter(Constant.VERSION_CODE);
		String versionName = request.getParameter(Constant.VERSION_NAME);
		String path = request.getParameter(Constant.PATH);
		String features = request.getParameter(Constant.FEATURES);
		String buildNumber = request.getParameter(Constant.BUILD_NUMBER);
		String action = request.getParameter("action");
		if(action!=null){
			if(action.equals("delete")){				
			}
		}
		PrintWriter out = response.getWriter();
	    if(name!=null && versionCode!=null && versionName!=null && path!=null){
			Installer installer = new Installer();
			installer.name = name ;
			installer.versionCode = versionCode ;
			installer.versionName = versionName ;
			installer.buildNumber = buildNumber ;
			installer.path = mHttpHeader+path;
			installer.features = features ;
			installer.createdDate = StringUtil.formatDate(new Date());
	    	RuntimeData.handleInstallers(installer);
			out.print(new Gson().toJson(installer));
	    }
	    else{
	    	
	    	out.print("{\"error\":\"wrong parameter!\"}");
	    }
		//Send Email
	}
}
