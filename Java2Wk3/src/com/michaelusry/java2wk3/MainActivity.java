/*
 * Project		Java2Wk3
 * 
 * package		com.michaelusry.java2wk3
 * 
 * @author		Michael Usry
 * 
 * date			Jul 24, 2014
 * 
 * purpose: This will access a json list of the last 50 quakes recorded.  The app checks
 * for network connection.  If there is no connection it will check to see if there is a local
 * file and display it as well as an alert.  If there is a connection the data is saved to a
 * file and then displayed on the screen.  When you select a list of quakes another activity is
 * displayed showing more information, the ability to launch a web browser and assign a star
 * rating.  A saved state allows info to be passed back to the main activity.
 * 
 * Week 3 addition: fragments.  If the device is rotated to landscape then the main and detail
 * fragments are displayed side by side. 	
 * 
 */
package com.michaelusry.java2wk3;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.michaelusry.java2wk3.R;
import com.michaelusry.javaWk4.ConnectionStatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity implements
		MainFragment.listItemSelected {

	static String TAG = MainActivity.class.getSimpleName();
	static ListView list;
	static MyService collector;
	static FileManager fileManager;
	static MainFragment MainFrag;

	static Context m_context;
	static String filename = "quake_json.txt";

	final MyHandler myHandler = new MyHandler(this);

	String thisTitle = null;
	String thisLink = null;
	String thisNorth = null;
	String thisWest = null;
	String thisLat = null;
	String thisLng = null;
	String thisDepth = null;
	String thisMag = null;
	String thisTime = null;

	ConnectionStatus cs = new ConnectionStatus();

	static JSONArray dataArray = null;

	static ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.out.println("MAINACTIVITY.onCreate");

		setContentView(R.layout.fragment_main);

		m_context = this;
		fileManager = FileManager.getInstance();
		MainFrag = (MainFragment) getFragmentManager().findFragmentById(
				R.id.fragment_main);

		/*
		 * // find the list by ID list = (ListView) findViewById(R.id.list);
		 * 
		 * // inflate the custom list header View customHeader =
		 * this.getLayoutInflater().inflate( R.layout.list_header, null); // add
		 * the header to the custom list list.addHeaderView(customHeader);
		 */
		// checking for a saved instance
		if (savedInstanceState != null) {
			System.out.println("MAINACTIVITY.savedInstanceState !=null");

			Log.i(TAG, "Restoring state");

			savedInstanceState.getSerializable("saved");

			if (arrayList != null) {
				System.out.println("MAINACTIVITY.arrayList !=null");

				Log.i(TAG, "arrayList != null(savedInstance");
				System.out
						.println("Going into MAINFRAG from  = MAINACTIVITY.arrayList !=null");

				MainFrag.updateList(arrayList);

				/*
				 * - moved to MainFragment.updateList SimpleAdapter adapter =
				 * new SimpleAdapter(m_context, arrayList, R.layout.list_row,
				 * new String[] { "title", "mag", "depth" }, new int[] {
				 * R.id.title, R.id.mag, R.id.depth });
				 * 
				 * list.setAdapter(adapter);
				 */
			}

		} else {
			// check connection status
			if (cs.isOnline(m_context)) {
				// checking to see if the file is saved locally, if not get it.
				System.out.println("MAINACTIVITY.cs.isOnline");

				File fileCheck = getBaseContext().getFileStreamPath(filename);
				if (fileCheck.exists()) {
					System.out.println("MAINACTIVITY.fileCheck.exists");

					System.out.println("going to parse,ToList()");
					parseJSONToList();
				} else {
					System.out.println("no file here run getData()");
					getData();
				}

			} else {
				// no internet connection
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
							"An internet connection is required and I have no local file.")
							.setPositiveButton("ok", null);
					alert.show();
					return;
				}
			}

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
					Log.e(TAG, "Data not created");
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
		String depth = null;
		String mag = null;
		JSONObject quakeObject = null;

		String dataString = FileManager.readFromFile(m_context, filename);
		// System.out.println("dataString: " + dataString);

		try {

			dataArray = new JSONArray(dataString);
			// System.out.println("dataArray(JSON): " + dataArray);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (int i = 1; i < dataArray.length(); i++) {
			// System.out.println("dataArray(JSON): " + dataArray);
			// System.out.println("dataArray(JSON).length: " +
			// dataArray.length());

			try {
				quakeObject = (JSONObject) dataArray.get(i);

				title = quakeObject.getString("title");

				mag = quakeObject.getString("mag");

				depth = quakeObject.getString("depth");

			} catch (JSONException e) {
				e.printStackTrace();
			}

			HashMap<String, String> quakeList = new HashMap<String, String>();

			quakeList.put("title", title);
			quakeList.put("depth", depth);
			quakeList.put("mag", mag);

			arrayList.add(quakeList);
			// System.out.println("parsing JSON: arrayList");

		}
		// call the MainFrag to display the arrayList just created
		System.out.println("HEADED INTO MAINFRAG.UPATELIST");
		MainFrag.updateList(arrayList);
	}

	// The result when coming back from the Intent (DetailActivity)
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult()");

		// variables
		String quakeTitle = "";
		String starRating = "";

		// extra info from the intent

		Log.i(TAG, "I have extras");

		Float starFloat = data.getFloatExtra("stars", 0);
		quakeTitle = data.getStringExtra("title");
		starRating = Float.toString(starFloat);

		// show AlertDialog

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(quakeTitle + " now has " + starRating + " stars");

		alert.show();
		super.onActivityResult(requestCode, resultCode, data);

	}

	// If there is a saved Instance
	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
		System.out.println("onSaveInstanceState/arrayList: ");

		// check that the array is not null and not empty. then save the array
		// to the bundle
		if (arrayList != null && !arrayList.isEmpty()) {
			outState.putSerializable("saved", (Serializable) arrayList);
			Log.i(TAG, "State Saved");
		}

	}

	// this is used to display the data if the device is in portrait mode.

	public void passDataPort(int arg2) {

		System.out.println("passDataPort");

		Intent detailActivity = new Intent(m_context, DetailActivity.class);

		// Add array info to send to detailActivity

		try {
			thisTitle = ((JSONObject) dataArray.get(arg2)).getString("title");
			thisLink = ((JSONObject) dataArray.get(arg2)).getString("link");

			thisNorth = ((JSONObject) dataArray.get(arg2)).getString("north");

			thisWest = ((JSONObject) dataArray.get(arg2)).getString("west");

			thisLat = ((JSONObject) dataArray.get(arg2)).getString("lat");

			thisLng = ((JSONObject) dataArray.get(arg2)).getString("lng");

			thisDepth = ((JSONObject) dataArray.get(arg2)).getString("depth");

			thisMag = ((JSONObject) dataArray.get(arg2)).getString("mag");

			thisTime = ((JSONObject) dataArray.get(arg2)).getString("time");

			// info to send to the Intent
			detailActivity.putExtra("title", thisTitle);
			detailActivity.putExtra("link", thisLink);
			detailActivity.putExtra("north", thisNorth);
			detailActivity.putExtra("west", thisWest);
			detailActivity.putExtra("lat", thisLat);
			detailActivity.putExtra("lng", thisLng);
			detailActivity.putExtra("depth", thisDepth);
			detailActivity.putExtra("mag", thisMag);
			detailActivity.putExtra("time", thisTime);

			// start the activity
			startActivityForResult(detailActivity, 0);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// used if the device is in landscape mode
	@Override
	public void passData(int arg2) {
		// TODO Auto-generated method stub

		System.out.println("passData.arg2 = " + arg2);

		DetailsFragment dFrag = (DetailsFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_detail);

		// if dFrag !=null and isInLayout call dFrag and pass strings

		if (dFrag != null && dFrag.isInLayout()) {
			System.out.println("dFrag.isInLayout");
			try {
				thisTitle = ((JSONObject) dataArray.get(arg2))
						.getString("title");
				thisLink = ((JSONObject) dataArray.get(arg2)).getString("link");

				thisNorth = ((JSONObject) dataArray.get(arg2))
						.getString("north");

				thisWest = ((JSONObject) dataArray.get(arg2)).getString("west");

				thisLat = ((JSONObject) dataArray.get(arg2)).getString("lat");

				thisLng = ((JSONObject) dataArray.get(arg2)).getString("lng");

				thisDepth = ((JSONObject) dataArray.get(arg2))
						.getString("depth");

				thisMag = ((JSONObject) dataArray.get(arg2)).getString("mag");

				thisTime = ((JSONObject) dataArray.get(arg2)).getString("time");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			dFrag.updateView(thisTitle, thisLink, thisNorth, thisWest, thisLat,
					thisLng, thisDepth, thisMag, thisTime);
		} else {
			System.out.println("go to ->passDataPort");
			passDataPort(arg2);
		}

	}

	// clicking the button goes to the web site
	public void onClick(View view) {

		Log.i(TAG, "intent: Launch web browser");

		Uri uriFromString = Uri.parse(thisLink);

		Intent launchWeb = new Intent(Intent.ACTION_VIEW, uriFromString);

		startActivity(launchWeb);

	}

}
