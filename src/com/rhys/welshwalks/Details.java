package com.rhys.welshwalks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends ActionBarActivity implements OnClickListener {
	Button updateDetails, resetDetails;
	TextView dis, walks, avg;
	EditText disWalkedToday;
	String totalDistance, totalWalks, averageDis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		dis = (TextView) findViewById(R.id.distanceTV);
		walks = (TextView) findViewById(R.id.walksTV);
		avg = (TextView) findViewById(R.id.averageTV);
		disWalkedToday = (EditText) findViewById(R.id.enterdistancewalked);
		updateDetails = (Button) findViewById(R.id.updateDetailsButton);
		resetDetails = (Button) findViewById(R.id.resetDetailsButton);
		
		updateDetails.setOnClickListener(this);
		resetDetails.setOnClickListener(this);
		
		try{
			DetailsDatabase stat = new DetailsDatabase(this);
			stat.open();
			String d = stat.getDis();
			String w = stat.getWalks();
			stat.close();
			dis.setText(d);
			walks.setText(w);
			double dNum = Double.parseDouble(d);
			double wNum = Double.parseDouble(w);
			double aNum = dNum/wNum;
			String average = Double.toString(aNum);
			avg.setText(average);
			if(dNum == 0) {
				avg.setVisibility(View.GONE);
			}
		} catch(Exception e) {
			Toast.makeText(this, "Get saved values Failed!", Toast.LENGTH_SHORT).show();
		}
		
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

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		
			case R.id.updateDetailsButton:
				
				try {
					String distance = disWalkedToday.getText().toString();
					String totalDis = dis.getText().toString();
					String numOfWalks = walks.getText().toString();
					double wn = Double.parseDouble(numOfWalks);
					double dn = Double.parseDouble(distance);
					double tdn = Double.parseDouble(totalDis);
					double updatedDis = dn + tdn;
					double updatedWalks = wn + 1;
					String updatedNumOfWalks = Double.toString(updatedWalks);
					
					String updatedTotalDis = Double.toString(updatedDis);
					
					
					DetailsDatabase entry = new DetailsDatabase(Details.this);
					entry.open();
					if(dn != 0){
						entry.updateEntry(updatedTotalDis, updatedNumOfWalks);
						avg.setVisibility(View.VISIBLE);
					}
					else{
						
					}
					String d = entry.getDis();
					String w = entry.getWalks();
					entry.close();
					
					dis.setText(d);
					walks.setText(w);
					double dNum = Double.parseDouble(d);
					double wNum = Double.parseDouble(w);
					double aNum = dNum/wNum;
					String average = Double.toString(aNum);
					avg.setText(average);
				} catch(Exception e) {
					Toast.makeText(this, "Update Failed!", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case R.id.resetDetailsButton:

				try{
					DetailsDatabase entries = new DetailsDatabase(Details.this);
					entries.open();
					entries.updateEntry("0", "0");
					String d = entries.getDis();
					String w = entries.getWalks();
					entries.close();
					
					dis.setText(d);
					walks.setText(w);
					avg.setText("0");
				} catch(Exception e) {
					Toast.makeText(this, "Reset Failed!", Toast.LENGTH_SHORT).show();
				}
				break;
		}
		
	}
}


