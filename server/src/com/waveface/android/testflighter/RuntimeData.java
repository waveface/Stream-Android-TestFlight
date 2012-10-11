package com.waveface.android.testflighter;

import java.io.File;

import com.google.gson.Gson;
import com.waveface.android.testflighter.model.Installer;
import com.waveface.android.testflighter.model.InstallersResponse;
import com.waveface.android.testflighter.util.FIleUtils;

public class RuntimeData {
	public static String SOTRED_PATH;
	public static String APK_PATH;
	public static InstallersResponse installers;
    public static void handleInstallers(Installer installer){
 
    	if(installers.installers==null){
    		Installer[] is = new Installer[1];
    		is[0] = installer;
    		installers.installers = is;
    	}
    	else{
    		int length = installers.installers.length;
    		String newInstallerName = installer.name;
        	String name = null;
        	boolean hasMatchName = false;
        	int matchPosition = -1;
        	Installer instance = null;
        	//compare name
	    	for(int i = 0 ; i< length; i++){
	    		instance = installers.installers[i];
	    		name = instance.name;
	    		if(newInstallerName.equals(name)){
	    			hasMatchName = true;
	    			matchPosition = i ;
	    			break;
	    		}
	    	}
	    	Installer[] is = null;
	    	if(hasMatchName){
	    		installers.installers[matchPosition] = installer;
	    	}
	    	else{
	    		if(length == 1 && installers.installers[0]==null){
		    		is = new Installer[1];
		    		is[0] = installer;
	    		}
	    		else{
		    		is = new Installer[length+1];
		    		for(int i = 0 ; i < length ; i++){
		    			is[i] = installers.installers[i];
		    		}
		    		is[length] = installer;
	    		}
	    		installers.installers = is;
	    	}
    	}
	    FIleUtils.writeFile(RuntimeData.SOTRED_PATH+File.separator+Constant.STORE_JSON_NAME, new Gson().toJson(installers), false);
    }
}
