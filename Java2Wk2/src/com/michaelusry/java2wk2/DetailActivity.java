package com.michaelusry.java2wk2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetailActivity extends Activity{

	// variables
	static String TAG = "DetailActivity";
	String detail_title = null;
	String detail_mag = null;
	String detail_depth = null;
	String quakeURL = null;
	String detail_lng = null;
	String detail_lat = null;
	String detail_north = null;
	String detail_west = null;
	String detail_time = null;
	RatingBar starRating;
	TextView title;
	TextView link;
	TextView north;
	TextView west;
	TextView lat;
	TextView lng;
	TextView depth;
	TextView mag;
	TextView time;

	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);
		
		Log.i(TAG, "onCreate() Started");
		
		//text views
		
//title = (TextView) findViewById(R.id.detail_title);

	}
			
	public void onClick(View view){
		
		Log.i(TAG,"intent: Launch web browser");
		
		Uri uriFromString = Uri.parse(quakeURL);
		
		Intent launchWeb = new Intent(Intent.ACTION_VIEW, uriFromString);
		
		startActivity(launchWeb);
		
	}
	
	@Override
	public void finish() {

		Intent data = new Intent(getBaseContext(),MainActivity.class);
		
//		starRating = (RatingBar) findViewById(R.id.);
		
		
		
	}
}
