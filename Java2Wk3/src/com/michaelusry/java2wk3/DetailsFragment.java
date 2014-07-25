/**
 * 
 */
package com.michaelusry.java2wk3;


import com.michaelusry.java2wk3.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment{
	
	// variables
	static String TAG = "DetailActivity";
	TextView title;
	TextView link;
	TextView north;
	TextView west;
	TextView lat;
	TextView lng;
	TextView depth;
	TextView mag;
	TextView time;


	/* (non-Javadoc)
	 * @see android.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.detail_activity, container);
		
		//moved from the DetailsFragment
		// TextView targets
		title = (TextView)view.findViewById(R.id.detail_title);		
		link = (TextView)view.findViewById(R.id.detail_link);		
		north = (TextView)view.findViewById(R.id.detail_north);		
		west = (TextView)view.findViewById(R.id.detail_west);		
		lat = (TextView)view.findViewById(R.id.detail_lat);		
		lng = (TextView)view.findViewById(R.id.detail_lng);		
		depth = (TextView)view.findViewById(R.id.detail_depth);		
		mag = (TextView)view.findViewById(R.id.detail_mag);		
		time = (TextView)view.findViewById(R.id.detail_time);

		
		
		return view;
	}

	public void updateView(String detail_title, String detail_link,
			String detail_north, String detail_west, String detail_lat,
			String detail_lng, String detail_depth, String detail_mag,
			String detail_time) {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "DetailsFragment.updateView");
		// MOVED FROM DetailActivity
		
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
}