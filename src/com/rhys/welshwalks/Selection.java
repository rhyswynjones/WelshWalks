package com.rhys.welshwalks;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class Selection extends ActionBarActivity {
	Vibrator vibrate;
	ListView listView;
	static final ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection_list);
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		listView = (ListView) findViewById(R.id.list);
		SimpleAdapter adapter = new SimpleAdapter(this, arrayList, R.layout.list_item, 
				new String[] {"name", "distance", "difficulty"}, 
				new int[] {R.id.name, R.id.distance, R.id.difficulty});
		
		arrayList.clear();
		populateList();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Object o = parent.getItemAtPosition(position);
				String rowParent = String.valueOf(o);
				
				if(rowParent.contains("name=Llangennech"))
				{
					vibrate.vibrate(100);
					Map.mapSelector = 1;
					Intent i = new Intent(getApplicationContext(), Map.class);
					startActivity(i);
				}
				else if(rowParent.contains("name=Swansea Bay"))
				{
					vibrate.vibrate(100);
					Map.mapSelector = 2;
					Intent i = new Intent(getApplicationContext(), Map.class);
					startActivity(i);
				}
				else if(rowParent.contains("name=Pen Y Fan"))
				{
					vibrate.vibrate(100);
					Map.mapSelector = 3;
					Intent i = new Intent(getApplicationContext(), Map.class);
					startActivity(i);					
				}
				else
				{
					vibrate.vibrate(100);
					Toast soon = Toast.makeText(getApplicationContext(), "Coming soon!", Toast.LENGTH_SHORT);
					soon.show();
				}
			}});
	}
	
	private void populateList(){
		
		HashMap<String,String> temp = new HashMap<String,String>();
		temp.put("name","Llangennech");
		temp.put("distance", "1.1km");
		temp.put("difficulty", "Easy");
		arrayList.add(temp);
		
		HashMap<String,String> temp1 = new HashMap<String,String>();
		temp1.put("name","Swansea Bay");
		temp1.put("distance", "7km");
		temp1.put("difficulty", "Medium");
		arrayList.add(temp1);
		
		HashMap<String,String> temp2 = new HashMap<String,String>();
		temp2.put("name","Pen Y Fan");
		temp2.put("distance", "7.3km");
		temp2.put("difficulty", "Hard");
		arrayList.add(temp2);
		
		HashMap<String,String> temp3 = new HashMap<String,String>();
		temp3.put("name","Skirrid Fawr");
		temp3.put("distance", "6.4km");
		temp3.put("difficulty", "Medium");
		arrayList.add(temp3);
		
		HashMap<String,String> temp4 = new HashMap<String,String>();
		temp4.put("name","Cwm Ratgoed");
		temp4.put("distance", "4km");
		temp4.put("difficulty", "Easy");
		arrayList.add(temp4);
		
		HashMap<String,String> temp5 = new HashMap<String,String>();
		temp5.put("name","Abergynolwyn");
		temp5.put("distance", "9.1km");
		temp5.put("difficulty", "Hard");
		arrayList.add(temp5);
		
		HashMap<String,String> temp6 = new HashMap<String,String>();
		temp6.put("name","Cardiff Bay");
		temp6.put("distance", "8km");
		temp6.put("difficulty", "Easy");
		arrayList.add(temp6);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
			case R.id.action_selection:
				Intent selectTab = new Intent("com.rhys.welshwalks.SELECTION");
				startActivity(selectTab);
				return true;
			case R.id.action_map:
				Intent mapTab = new Intent("com.rhys.welshwalks.MAP");
				startActivity(mapTab);
				return true;
			case R.id.action_details:
				Intent detailsTab = new Intent("com.rhys.welshwalks.DETAILS");
				startActivity(detailsTab);
				return true;
			case R.id.action_legalnotices:
				Intent legalNoticesTab = new Intent("com.rhys.welshwalks.LEGALNOTICESACTIVITY");
				startActivity(legalNoticesTab);
				return true;
			case R.id.action_help:
				Intent helpTab = new Intent("com.rhys.welshwalks.HELPVIDACTIVITY");
				startActivity(helpTab);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
}

