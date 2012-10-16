package com.waveface.android.testflighter.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Utils {
	public static void uploadFileToS3(Runtime runtime, PrintWriter out,
			String path) {
		Process process = null;
		String[] command = { "/usr/local/bin/s3cmd", "-f", "-P", "put", path, "s3://wammer-pkgs" };
		try {
			process = runtime.exec(command);
			DataInputStream in = new DataInputStream(process.getInputStream());
			BufferedReader d = new BufferedReader(new InputStreamReader(in));
			// Read and print the output
			String line = null;
			while ((line = d.readLine()) != null) {
				out.println(line);
			}
		} catch (Exception e) {
			out.println("Exception");
		}
	}
}
