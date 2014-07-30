/**
 * 
 */
package com.michaelusry.java2wk4;

import java.util.ArrayList;
import java.util.HashMap;

import com.michaelusry.java2wk4.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author michael
 *
 */
public class MainFragment extends Fragment{
	// declare variables
	static String TAG = MainFragment.class.getSimpleName();
	static ListView list;
	static ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
	static Context m_context;
	public static SimpleAdapter adapter;


	// interface
	public interface listItemSelected {
		
		void passData(int arg2);
	}

	// private connection
	private listItemSelected parent;

	@Override
	public void onAttach(Activity activity) {
		
		System.out.println("MAINFRAGMENT.onAttach");
		// TODO Auto-generated method stub
		super.onAttach(activity);

		if (activity instanceof listItemSelected) {

			System.out.println("MAINFRAGMENT.instanceof");

			parent = (listItemSelected) activity;

		} else {
			throw new ClassCastException(activity.toString()
					+ "must implement listItemSelected() method");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("MAINFRAGMENT.onCreateView");

		Log.i(TAG, "MainFragment onCreateView");
		
		
		// TODO Auto-generated method stub
		
		//declare content
		m_context = getActivity().getBaseContext();
		
		//inflate activity_main
		View view = inflater.inflate(R.layout.activity_main, container);
		
		//find the list by ID
		list = (ListView) view.findViewById(R.id.list);
		
		//inflate custom list header
		View customListHeader = inflater.inflate(R.layout.list_header, container);
		
		list.addHeaderView(customListHeader,container,false);
		
		//add header to customer list
		list.setOnItemClickListener(new OnItemClickListener() {

			// when you click the list send to the DetailActivity
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("MainFragment.onItemClick");
				 System.out.println("arrayList arg2: " + (arg2));
				// System.out.println("arrayList: " + arrayList);
				
				parent.passData(arg2);

			}
		});

		return view;
		
	}
	
	public void updateList(ArrayList<HashMap<String, String>> passedArray){
		Log.i(TAG, "Updating list inside MainFragment");
		System.out.println("MAINFRAGMENT.updateList");

		arrayList = passedArray;
		
		SimpleAdapter adapter = new SimpleAdapter(m_context, arrayList,
				R.layout.list_row, new String[] { "title", "mag",
						"depth" }, new int[] { R.id.title });

		list.setAdapter(adapter);


	}

}
