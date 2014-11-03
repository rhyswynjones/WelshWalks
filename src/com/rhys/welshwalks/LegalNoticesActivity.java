package com.rhys.welshwalks;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class LegalNoticesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_legal_notices);
	
		((TextView) findViewById(R.id.textId)).setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this));
	}

}
