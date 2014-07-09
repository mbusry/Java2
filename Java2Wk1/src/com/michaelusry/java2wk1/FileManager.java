package com.michaelusry.java2wk1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;


public class FileManager {
	
	private static FileManager m_instance;
	
	private FileManager(){
		
	}
	
	public static FileManager getInstance(){
		if(m_instance ==null){
			m_instance = new FileManager();
			
		}
		return m_instance;
	}
	
	public Boolean writeToFile (Context context, String filename, String content){
	
		Boolean result = false;
		
		FileOutputStream fos = null;
		
		try {
			fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			Log.i("WRITE STRING FILE", "success");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("FILE NOT FOUND",e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("I/O ERROR",e.toString());

		}
		
		return result;
	}

}
