/*
 * Project		Java2Wk1
 * 
 * package		com.michaelusry.java2wk2
 * 
 * @author		Michael Usry
 * 
 * date			Jul 8, 2014
 * 
 * purpose: This will access a json list of the last 50 quakes recorded.  The app checks
 * for network connection.  If there is no connection it will check to see if there is a local
 * file and display it as well as an alert.  If there is a connection the data is saved to a
 * file and then displayed on the screen.	
 * 
 */
package com.michaelusry.java2wk2;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.michaelusry.java2wk2.R;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	ConnectionStatus cs = new ConnectionStatus();
	
	static JSONArray dataArray = null;

	static ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		m_context = this;
		fileManager = FileManager.getInstance();

		// find the list by ID
		list = (ListView) findViewById(R.id.list);

		// inflate the custom list header
		View customHeader = this.getLayoutInflater().inflate(
				R.layout.list_header, null);
		// add the header to the custom list
		list.addHeaderView(customHeader);

		// checking for a saved instance
		if (savedInstanceState != null) {
			Log.i(TAG, "Restoring state");

			savedInstanceState.getSerializable("saved");
			if (arrayList != null) {
				SimpleAdapter adapter = new SimpleAdapter(m_context, arrayList,
						R.layout.list_row, new String[] { "title", "mag",
								"depth" }, new int[] { R.id.title, R.id.mag,
								R.id.depth });

				list.setAdapter(adapter);
			}

		} else {
			// check connection status
			if (cs.isOnline(m_context)) {

			} else {

				File fileCheck = getBaseContext().getFileStreamPath(filename);
				if (fileCheck.exists()) {
					// if the user is not connected let them know it is required
					// but
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
				// System.out.println("no file here run getData()");
				getData();
			}
		}
		list.setOnItemClickListener(new OnItemClickListener() {
			
			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				System.out.println("detailActivity:onClick");
				System.out.println("arrayList arg2: " + (arg2));
				try {
					System.out.println("dataArray(JSON).get(arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("title"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				Intent detailActivity = new Intent(getBaseContext(),
						DetailActivity.class);

				// Add array info to send to detailActivity
				
				try {
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("title"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("link"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("north"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("west"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("lat"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("lng"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("depth"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("mag"));
					System.out.println("dataArray (arg2) : " + ((JSONObject) dataArray.get(arg2)).getString("time"));

				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					String thisTitle = ((JSONObject) dataArray.get(arg2)).getString("title");
					String thisLink = ((JSONObject) dataArray.get(arg2)).getString("link");

					String thisNorth = ((JSONObject) dataArray.get(arg2)).getString("north");

					String thisWest = ((JSONObject) dataArray.get(arg2)).getString("west");

					String thisLat = ((JSONObject) dataArray.get(arg2)).getString("lat");

					String thisLng = ((JSONObject) dataArray.get(arg2)).getString("lng");

					String thisDepth = ((JSONObject) dataArray.get(arg2)).getString("depth");
					
					String thisMag = ((JSONObject) dataArray.get(arg2)).getString("mag");

					String thisTime = ((JSONObject) dataArray.get(arg2)).getString("time");
					
					detailActivity.putExtra("title", thisTitle);
					detailActivity.putExtra("link", thisLink);
					detailActivity.putExtra("north", thisNorth);
					detailActivity.putExtra("west", thisWest);
					detailActivity.putExtra("lat", thisLat);
					detailActivity.putExtra("lng", thisLng);
					detailActivity.putExtra("depth", thisDepth);
					detailActivity.putExtra("mag", thisMag);
					detailActivity.putExtra("time", thisTime);
					
					startActivityForResult(detailActivity, 0);


				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
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
		String title = null;
		JSONObject quakeObject = null;
		String depth = null;
		String mag = null;
//		String north = null;
//		String west = null;
//		String lat = null;
//		String lng = null;
//		String timeStamp = null;
//		String link = null;

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
//			 System.out.println("dataArray: " + dataArray);
//			 System.out.println("dataArray.length: " + dataArray.length());

			try {
				quakeObject = (JSONObject) dataArray.get(i);

				title = quakeObject.getString("title");

				mag = quakeObject.getString("mag");

//				link = quakeObject.getString("link");

//				north = quakeObject.getString("north");
//
//				west = quakeObject.getString("west");
//
//				lat = quakeObject.getString("lat");
//
//				lng = quakeObject.getString("lng");

				depth = quakeObject.getString("depth");

//				timeStamp = quakeObject.getString("time");

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
