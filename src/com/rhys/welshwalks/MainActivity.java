package com.rhys.welshwalks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
	Vibrator vibrate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

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
				vibrate.vibrate(100);
				Intent selectTab = new Intent("com.rhys.welshwalks.SELECTION");
				startActivity(selectTab);
				return true;
			case R.id.action_map:
				vibrate.vibrate(100);
				Intent mapTab = new Intent("com.rhys.welshwalks.MAP");
				startActivity(mapTab);
				return true;
			case R.id.action_details:
				vibrate.vibrate(100);
				Intent detailsTab = new Intent("com.rhys.welshwalks.DETAILS");
				startActivity(detailsTab);
				return true;
			case R.id.action_legalnotices:
				vibrate.vibrate(100);
				Intent legalNoticesTab = new Intent("com.rhys.welshwalks.LEGALNOTICESACTIVITY");
				startActivity(legalNoticesTab);
				return true;
			case R.id.action_help:
				vibrate.vibrate(100);
				Intent helpTab = new Intent("com.rhys.welshwalks.HELPVIDACTIVITY");
				startActivity(helpTab);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}


