package com.michaelusry.java2wk3;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends IntentService {

	public static final String MESSENGER_KEY = "messenger";

	String quakeURL = "http://www.kuakes.com/json/";
	String response = "";

	public MyService() {
		super("MyService");

	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();
		Messenger messenger = (Messenger) extras.get(MESSENGER_KEY);


		try {
			// create the URL from the variable at the top
			URL url = new URL(quakeURL);

			// create a new URL connection from the URL and open the connection
			URLConnection myConnection = url.openConnection();
			// create a BufferedInputStream from the connection
			BufferedInputStream bin = new BufferedInputStream(
					myConnection.getInputStream());

			byte[] contextByte = new byte[1024];
			int byteRead = 0;

			// create a StringBuffer
			StringBuffer responseBuffer = new StringBuffer();

			// append the bytes to a string until the file is completely written
			// to the string
			while ((byteRead = bin.read(contextByte)) != -1) {
				response = new String(contextByte, 0, byteRead);
				responseBuffer.append(response);
			}
			// create a string from the responseBuffer
			response = responseBuffer.toString();
			Log.i("GetJSONService", "response string created successfully");
		} catch (IOException e) {

			Log.e("getJSONString", e.toString());
		}

		// get a built in message
		Message message = Message.obtain();
		// pass result ok back to the handler
		message.arg1 = Activity.RESULT_OK;
		// pass the string back to the handler
		message.obj = response;

		try {
			messenger.send(message);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
			
			
			
}
