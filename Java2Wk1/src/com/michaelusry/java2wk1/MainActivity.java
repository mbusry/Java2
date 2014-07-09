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


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView TitleTextView;
	private TextView DepthTextView;
	private TextView MagTextView;
	private Button readButton;
	private Button writeButton;

	FileManager m_file;

	Context m_context;
	String m_file_name = "quake_json_from_url.txt";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_context = this;
		m_file = FileManager.getInstance();
		
//		TitleTextView = (TextView) findViewById(R.id.TitleTextView);
//		DepthTextView = (TextView) findViewById(R.id.DepthTextView);
//		MagTextView = (TextView) findViewById(R.id.MagTextView);



		readButton = (Button)findViewById(R.id.btn_read);
		readButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(m_context, "readButton", Toast.LENGTH_SHORT).show();
			}
		});
		
		writeButton = (Button) findViewById(R.id.btn_write);
		writeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Toast.makeText(m_context, "writeButton", Toast.LENGTH_SHORT).show();

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


	public void displayData() {

	}

}
