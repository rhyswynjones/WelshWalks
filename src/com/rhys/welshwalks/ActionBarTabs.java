package com.rhys.welshwalks;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class ActionBarTabs extends ActionBarActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection_list);
		Log.i("Logged:", "onCreate");
	}
	
}
