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
	String detail_link = null;
	String detail_north = null;
	String detail_west = null;
	String detail_lat = null;
	String detail_lng = null;
	String detail_depth = null;
	String detail_mag = null;
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
		
		System.out.println("detail_activity started");
		Log.i(TAG, "onCreate() Started");
		
		//text views
		title = (TextView)findViewById(R.id.detail_title);		
		link = (TextView)findViewById(R.id.detail_link);		
		north = (TextView)findViewById(R.id.detail_north);		
		west = (TextView)findViewById(R.id.detail_west);		
		lat = (TextView)findViewById(R.id.detail_lat);		
		lng = (TextView)findViewById(R.id.detail_lng);		
		depth = (TextView)findViewById(R.id.detail_depth);		
		mag = (TextView)findViewById(R.id.detail_mag);		
		time = (TextView)findViewById(R.id.detail_time);
		
		// launch intent,  get extras from intent
		
		Intent mainIntent = getIntent();
		mainIntent.getExtras();
		
		// get strings from mainIntent
		detail_title = mainIntent.getStringExtra("title");
		detail_link = mainIntent.getStringExtra("link");
		detail_north = mainIntent.getStringExtra("north");
		detail_west = mainIntent.getStringExtra("west");
		detail_lat = mainIntent.getStringExtra("lat");
		detail_lng = mainIntent.getStringExtra("lng");
		detail_depth = mainIntent.getStringExtra("depth");
		detail_mag = mainIntent.getStringExtra("mag");
		detail_time = mainIntent.getStringExtra("time");
		
		// set the text of views
		title.setText(detail_title);
		link.setText("URL: " + detail_link);
		north.setText("North: " + detail_north);
		west.setText("West: " + detail_west);
		lat.setText("Latitude: " + detail_lat);
		lng.setText("Longitude: " + detail_lng);
		depth.setText("Depth: " + detail_depth);
		mag.setText("Magnitude: " + detail_mag);
		time.setText("Time: " + detail_time);


	}
		
	//clicking the button goes to the web site
	public void onClick(View view){
		
		Log.i(TAG,"intent: Launch web browser");
		
		Uri uriFromString = Uri.parse(detail_link);
		
		Intent launchWeb = new Intent(Intent.ACTION_VIEW, uriFromString);
		
		startActivity(launchWeb);
		
	}
	
	//when the intent finishes send the info back to the MainActivity
	@Override
	public void finish() {

		Intent data = new Intent(getBaseContext(),MainActivity.class);
		
		starRating = (RatingBar) findViewById(R.id.detail_rating);
		
		//value from ratings bar
		Float stars = starRating.getRating();
		
		//put data into extras
		data.putExtra("title", detail_title);
		data.putExtra("stars", stars);
		
		setResult(RESULT_OK, data);
		
		Log.i(TAG,"Rating and title passed to MainActivity");
		
		super.finish();
		
		
	}
}
