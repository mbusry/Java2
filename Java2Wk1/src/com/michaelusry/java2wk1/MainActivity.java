/*
 * Project		Java2Wk1
 * 
 * package		com.michaelusry.java2wk1
 * 
 * @author		Michael Usry
 * 
 * date			Jul 8, 2014
 * 
 * The application should contain at least the following components:

1 Intent to launch the Service
1 Service correctly constructed in the MainActivity
1 Web Call From the Service (API included)
1 JSON Array stored in an Internal File within the Service onHandleIntent() method
1 Handler method in the MainActivity to be used to update the UI (either in that method or calling a method to update)
1 JSON string read from a file and correctly parsed and displayed from the Handler method
	
 * 
 */
package com.michaelusry.java2wk1;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.michaelusry.javaWk4.ConnectionStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {

	public static final String TAG = MainActivity.class.getSimpleName();
	static ListView list;
	static MyService collector;
	static FileManager fileManager;

	static Context m_context;
	static String filename = "quake_json.txt";

	final MyHandler myHandler = new MyHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.println("In MainActivity:Beginning");

		setContentView(R.layout.activity_main);


		m_context = this;
		fileManager = FileManager.getInstance();

		// find the list by ID
		list = (ListView) findViewById(R.id.list);

		// inflate the custom list header
		View ListHeader = this.getLayoutInflater().inflate(
				R.layout.list_header, null);
		// add the header to the custom list
		list.addHeaderView(ListHeader);

		ConnectionStatus cs = new ConnectionStatus();

		if (cs.isOnline(this)) {

		} else {
			File fileCheck = getBaseContext().getFileStreamPath(filename);
			if (fileCheck.exists()) {
				// if the user is not connected let them know it is required but
				// show old results form the file
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle(
						"No connection to the internet.  I'll be using local data.")
						.setPositiveButton("ok", null);

				alert.show();
				parseJSONToList();
			} else {
				// if the user is not connected let them know it is required
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle(
						"An internet connection is required. I have no local file.")
						.setPositiveButton("ok", null);
				alert.show();
				return;
			}
		}

		File fileCheck = getBaseContext().getFileStreamPath(filename);
		if (fileCheck.exists()) {
			System.out.println("going to parseJSONToList()");
			parseJSONToList();
		} else {
			System.out.println("no file here run getData()");
			getData();
		}

		// SimpleAdapter adapter = new SimpleAdapter(this,TitleTextView,
		// R.layout.TitleTextView, new String[]{"title","depth","mag"});

		// set Adapter
		// setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private static class MyHandler extends Handler {

		private final WeakReference<MainActivity> myActivity;

		public MyHandler(MainActivity activity) {
			myActivity = new WeakReference<MainActivity>(activity);

		}

		public void handleMessage(Message message) {

			MainActivity activity = myActivity.get();

			if (activity != null) {
				Object returnObject = message.obj;

				if (message.arg1 == RESULT_OK && returnObject != null) {

					Log.i("MAIN ACTIVITY", "handleMessage()");

					String response = (String) returnObject;

					fileManager = FileManager.getInstance();

					fileManager.writeToFile(m_context, filename, response);

					Log.i(TAG, "File written to device");
					// Call Method to read/parse quake_json.txt file
					parseJSONToList();

				} else {
					Log.i(TAG, "Data not created");
				}
			}
		}
	}

	public void getData() {
		Messenger newMessenger = new Messenger(myHandler);

		System.out.println("getData");

		Intent newIntent = new Intent(this, MyService.class);

		newIntent.putExtra(MyService.MESSENGER_KEY, newMessenger);

		startService(newIntent);
	}

	public static void parseJSONToList() {

		System.out.println("parseJSONToList");
		// vars
		JSONArray dataArray = null;
		String title = null;
		JSONObject quakeObject = null;
		String depth = null;
//		JSONObject magObject = null;
		String mag = null;

		ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

		String dataString = FileManager.readFromFile(m_context, filename);
		System.out.println("dataString: " + dataString);

		try {

			dataArray = new JSONArray(dataString);
			System.out.println("dataArray: " + dataArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (int i = 1; i < dataArray.length(); i++) {
			// System.out.println("dataArray: " + dataArray);
			// System.out.println("dataArray.length: " + dataArray.length());

			try {
				quakeObject = (JSONObject) dataArray.get(i);

				title = quakeObject.getString("title");

				depth = quakeObject.getString("depth");

				mag = quakeObject.getString("mag");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			HashMap<String, String> quakeList = new HashMap<String, String>();

			quakeList.put("title", title);
			quakeList.put("depth", depth);
			quakeList.put("mag", mag);

			arrayList.add(quakeList);

		}

		SimpleAdapter adapter = new SimpleAdapter(m_context, arrayList,
				R.layout.list_row, new String[] { "title", "depth", "mag" },
				new int[] { R.id.title, R.id.depth, R.id.mag });

		list.setAdapter(adapter);
		
	}

}
