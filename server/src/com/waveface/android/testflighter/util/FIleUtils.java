package com.waveface.android.testflighter.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FIleUtils {

	public static boolean writeFile(String filename,String data,boolean append){
		boolean isSuccessed = false;
		  try {
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(data);
			out.flush();
			isSuccessed = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccessed;
	}
	public static String readFile(String filename){
	    File file = new File(filename);
	    int ch;
	    StringBuffer strContent = new StringBuffer("");
	    FileInputStream fin = null;
	    try {
	      fin = new FileInputStream(file);
	      while ((ch = fin.read()) != -1)
	        strContent.append((char) ch);
	      fin.close();
	    } catch (Exception e) {
	      System.out.println(e);
	    }
	    return strContent.toString();
	}
}
