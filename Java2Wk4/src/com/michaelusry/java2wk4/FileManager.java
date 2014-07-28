package com.michaelusry.java2wk4;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileManager {

	private static FileManager m_instance;

	// The constructor
	// When using a singleton it returns nothing

	private FileManager() {

	}

	// Singleton
	public static FileManager getInstance() {
		if (m_instance == null) {
			m_instance = new FileManager();

		}
		return m_instance;
	}

	// method to write the JSON data to a txt file.
	public Boolean writeToFile(Context context, String filename, String content) {

		System.out.println("In Filemanager:writeToFile");

		Boolean result = false;

		FileOutputStream fos = null;

		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			Log.i("WRITE STRING FILE", "success");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("FILE NOT FOUND", e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("I/O ERROR", e.toString());

		}
		System.out.println("in writeToFile");
		return result;
	}

	// read file
	public static String readFromFile(Context context, String filename) {

		System.out.println("In Filemanager:readToFile");

		String content = "";

		FileInputStream fis = null;

		try {
			fis = context.openFileInput(filename);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] contentBytes = new byte[1024];
		int bytesRead = 0;
		StringBuffer contentBuffer = new StringBuffer();

		try {

			while ((bytesRead = bis.read(contentBytes)) != -1) {

				content = new String(contentBytes, 0, bytesRead);
				contentBuffer.append(content);
			}

		} catch (IOException e) {
			Log.e("FILE INPUT", e.toString());
		}

		content = contentBuffer.toString();

		try {
			fis.close();
		} catch (IOException e) {
			Log.e("CLOSE FILE ERROR", e.toString());
		}
		Log.i("FILEMANAGER", "File read sucessfully");
		return content;
	}
}
