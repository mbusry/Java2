package com.michaelusry.java2wk4;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class FavoritesActivity extends Activity {

	// Declare the variables
	ListView favsList;
	TextView favTitle;
	static ArrayList<HashMap<String, String>> favsArray = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.favorites_activity);
		super.onCreate(savedInstanceState);

		// Get the favsArray from the main activity
		favsArray = MainActivity.favoritesArray;

		// Target the elements
		favsList = (ListView) findViewById(R.id.favorites_list);
		favTitle = (TextView) findViewById(R.id.favorites_title);

		if (favsArray.isEmpty()) {
			favTitle.setText("No Favorites");
		} else {

			// Create a SimpleAdapter to populate the list
			SimpleAdapter favsAdapter = new SimpleAdapter(this, favsArray,
					R.layout.favorites_list,
					new String[] { "title", "mag", "depth" },
					new int[] { R.id.stars });

			// set the adapter for the list
			favsList.setAdapter(favsAdapter);
		}

	}

}
