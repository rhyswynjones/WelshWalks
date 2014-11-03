package com.rhys.welshwalks;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class HelpVidActivity extends Activity {
	VideoView vid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_vid);
		
		vid = (VideoView) findViewById(R.id.action_help_vid);
		String urlPath = "android.resource://" + getPackageName() + "/" + R.raw.googlemaps_help;
		vid.setVideoURI(Uri.parse(urlPath));
		
		MediaController mc = new MediaController(this);
		mc.setMediaPlayer(vid);
		vid.setMediaController(mc);
	}


}
