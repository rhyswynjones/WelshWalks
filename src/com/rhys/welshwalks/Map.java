package com.rhys.welshwalks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map extends ActionBarActivity implements LocationListener, OnMarkerClickListener, OnMapLongClickListener {
	Context context = this;
	GoogleMap googlemap;
	static int mapSelector = 0;
	static final ArrayList<HashMap<String, String>> userMarkers = new ArrayList<HashMap<String, String>>();
	boolean markerClicked;
	Vibrator vibrate;
	private Polyline llanPolyline, swanseaBayPolyline, penyfanPolyline;
	private Marker 
	llangennech, llanParking, llanAtt1, 
	llanInfo1, llanInfo2, llanInfo3, llanInfo4, llanInfo5, llanInfo6, llanInfo7, llanInfo8,
	swanseaBay, sBayParking, sBayAtt1, sBayAtt2, sBayAtt3, sBayAtt4, sBayAtt5, sBayAtt6, sBayAtt7, 
	sBayInfo1, sBayInfo2, sBayInfo3, sBayInfo4, sBayInfo5, sBayInfo6, sBayInfo7, sBayInfo8, sBayInfo9, sBayInfo10, sBayInfo11,
	penyfan, penParking, penAtt1, penAtt2, penAtt3, 
	penInfo1, penInfo2, penInfo3, penInfo4, penInfo5, penInfo6, penInfo7, penInfo8, penInfo9, penInfo10;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(isGooglePlay()){
			setContentView(R.layout.map);
			setUpMap();
		}
		
		vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
	
	}
	
	//sets up the content of the options menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map_menu, menu);
		return true;
	}
	
	//sets the actions of each menu option
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
			
			case R.id.action_normalMap:
				googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				return true;
			case R.id.action_hybridMap:
				googlemap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				return true;
			case R.id.action_satelliteMap:
				googlemap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				return true;
			case R.id.action_terrainMap:
				googlemap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
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

	//checks if googlePlay services are available. If not if this ensures it doesn't crash
	private boolean isGooglePlay() {
		
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		if(status == ConnectionResult.SUCCESS){
			Log.i("GooglePlay", "Connected with google play!!!");
			return(true);
		}
		else{
			((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 10)).show();
			//Toast.makeText(this, "Google Play NOT working!", Toast.LENGTH_SHORT);
		}
		return(false);
	}

	
	private void setUpMap() {
		
		//null check to confirm we have not already instantiated the map
		if(googlemap == null){
			googlemap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
			//check if we were successful in obtaining the map
			if(googlemap != null){
				
				googlemap.setMyLocationEnabled(true);

				LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
				String provider = lm.getBestProvider(new Criteria(), true);
				
				if(provider == null){
					onProviderDisabled(provider);
				}
				
				Location loc = lm.getLastKnownLocation(provider);
				
				if(loc != null){
					onLocationChanged(loc);
				}			
				
				addAllMarkers();
				setInitialCameraPosition();
				googlemap.setOnMapLongClickListener(this);
					
				googlemap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick(Marker marker) {
						String key = marker.getTitle().toString();
						for(HashMap<String, String> h : userMarkers){
							if(h.containsKey(key)){
								h.clear();
							}
						}
						marker.remove();
					}
				});
				
				googlemap.setOnMarkerClickListener(this);
				markerClicked = false;
				
			}
		}
	}
	


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
		
		googlemap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
		googlemap.animateCamera(CameraUpdateFactory.zoomTo(8));
		
	}
	

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onMapLongClick(final LatLng latlng) {
		
		LayoutInflater li = LayoutInflater.from(context);
		final View v = li.inflate(R.layout.map_pressed, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(v);
		builder.setCancelable(false);
		
		builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				vibrate.vibrate(100);
				EditText title = (EditText) v.findViewById(R.id.edit_title);
				String t = title.getText().toString();
				String mll = latlng.toString();
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(t, mll);
				userMarkers.add(hm);
				
				googlemap.addMarker(new MarkerOptions()
				.title(title.getText().toString())
				.position(latlng)
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
				);
				
				
			}
		});
		
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
	}
	
	
	public void setInitialCameraPosition(){
		//set the map's initial camera position
		if(mapSelector == 0){
			//over the centre of Wales
			LatLng ll = new LatLng(52.254131,-3.809509);
			googlemap.moveCamera(CameraUpdateFactory.newLatLng(ll));
			googlemap.animateCamera(CameraUpdateFactory.zoomTo(7));
		}
		if(mapSelector == 1){
			//over the Llangennech walk
			LatLng ll = new LatLng(51.70301,-4.090235);
			googlemap.moveCamera(CameraUpdateFactory.newLatLng(ll));
			googlemap.animateCamera(CameraUpdateFactory.zoomTo(14));
			llangennechTrail();
			mapSelector = 0;
		}
		if(mapSelector == 2){
			//over the Swansea Bay walk
			LatLng ll = new LatLng(51.568263,-3.987887);
			googlemap.moveCamera(CameraUpdateFactory.newLatLng(ll));
			googlemap.animateCamera(CameraUpdateFactory.zoomTo(14));
			swanseaBayTrail();
			mapSelector = 0;
		}
		if(mapSelector == 3){
			//over the Pen Y Fan walk
			LatLng ll = new LatLng(51.874181,-3.476722);
			googlemap.moveCamera(CameraUpdateFactory.newLatLng(ll));
			googlemap.animateCamera(CameraUpdateFactory.zoomTo(14));
			penyfanTrail();
			mapSelector = 0;
		}
	}
	
	public void addAllMarkers(){
		// adding all saved markers within ArrayList userMarkers to the map
		for (HashMap<String, String> h : userMarkers) {
			for (Entry<String, String> e : h.entrySet()) {
				String key = e.getKey();
				String value = e.getValue();
				
				// editing the saved string in the HashMap to use latlng coordinates
				String[] first = value.split(" ");
				first[1] = first[1].replaceAll("[(]", "");
				first[1] = first[1].replaceAll("[)]", "");
				String[] second = first[1].split(",");
				
				googlemap.addMarker(new MarkerOptions()
				.title(key)
				.position(new LatLng(Double.valueOf(second[0]), Double.valueOf(second[1])))
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
				);
			}
		}
		
		//add default markers
		llangennech = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.702263, -4.089186))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		
		swanseaBay = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.565679, -3.985781))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		
		penyfan = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.872329, -3.479823))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		if(marker.equals(llangennech)){
			if(llanPolyline != null){
				llanPolyline.remove();
				llanPolyline = null;
				llanParking.remove();
				llanParking = null;
				llanAtt1.remove();
				llanAtt1 = null;
				llanInfo1.remove();
				llanInfo1 = null;
				llanInfo2.remove();
				llanInfo2 = null;
				llanInfo3.remove();
				llanInfo3 = null;
				llanInfo4.remove();
				llanInfo4 = null;
				llanInfo5.remove();
				llanInfo5 = null;
				llanInfo6.remove();
				llanInfo6 = null;
				llanInfo7.remove();
				llanInfo7 = null;
				llanInfo8.remove();
				llanInfo8 = null;
				vibrate.vibrate(100);
			}
			else{ 
				llangennechTrail();
				vibrate.vibrate(100);
			}
		}
		else{
			markerClicked = true;
		}
		
		if(marker.equals(swanseaBay)){
			if(swanseaBayPolyline != null){
				swanseaBayPolyline.remove();
				swanseaBayPolyline = null;
				sBayParking.remove();
				sBayParking = null;
				sBayAtt1.remove();
				sBayAtt1 = null;
				sBayAtt2.remove();
				sBayAtt2 = null;
				sBayAtt3.remove();
				sBayAtt3 = null;
				sBayAtt4.remove();
				sBayAtt4 = null;
				sBayAtt5.remove();
				sBayAtt5 = null;
				sBayAtt6.remove();
				sBayAtt6 = null;
				sBayAtt7.remove();
				sBayAtt7 = null;
				sBayInfo1.remove();
				sBayInfo1 = null;
				sBayInfo2.remove();
				sBayInfo2 = null;
				sBayInfo3.remove();
				sBayInfo3 = null;
				sBayInfo4.remove();
				sBayInfo4 = null;
				sBayInfo5.remove();
				sBayInfo5 = null;
				sBayInfo6.remove();
				sBayInfo6= null;
				sBayInfo7.remove();
				sBayInfo7 = null;
				sBayInfo8.remove();
				sBayInfo8 = null;
				sBayInfo9.remove();
				sBayInfo9 = null;
				sBayInfo10.remove();
				sBayInfo10 = null;
				sBayInfo11.remove();
				sBayInfo11 = null;
				vibrate.vibrate(100);
			}
			else{ 
				swanseaBayTrail();
				vibrate.vibrate(100);
			}
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penyfan)){
			if(penyfanPolyline != null){
				penyfanPolyline.remove();
				penyfanPolyline = null;
				penParking.remove();
				penParking = null;
				penAtt1.remove();
				penAtt1 = null;
				penAtt2.remove();
				penAtt2 = null;
				penAtt3.remove();
				penAtt3 = null;
				penInfo1.remove();
				penInfo1 = null;
				penInfo2.remove();
				penInfo2 = null;
				penInfo3.remove();
				penInfo3 = null;
				penInfo4.remove();
				penInfo4 = null;
				penInfo5.remove();
				penInfo5 = null;
				penInfo6.remove();
				penInfo6 = null;
				penInfo7.remove();
				penInfo7 = null;
				penInfo8.remove();
				penInfo8 = null;
				penInfo9.remove();
				penInfo9 = null;
				penInfo10.remove();
				penInfo10 = null;
				vibrate.vibrate(100);
			}
			else{ 
				penyfanTrail();
				vibrate.vibrate(100);
			}
		}
		else{
			markerClicked = true;
		}
		
		if(marker.equals(llanInfo1)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info1);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.702541, -4.088828 \n\nDistance to Start: 0km \n\nDistance to End: 1.1km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(llanInfo2)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info2);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.703294, -4.088847 \n\nDistance to Start: 0.1km \n\nDistance to End: 1km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(llanInfo3)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info3);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.705311, -4.09027 \n\nDistance to Start: 0.3km \n\nDistance to End: 0.8km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(llanInfo4)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info4);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.708041, -4.093001 \n\nDistance to Start: 0.8km \n\nDistance to End: 0.3km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(llanInfo5)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info5);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.70828, -4.093079 \n\nDistance to Start: 0.8km \n\nDistance to End: 0.3km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(llanInfo6)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info6);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.708666, -4.093735 \n\nDistance to Start: 0.9km \n\nDistance to End: 0.2km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(llanInfo7)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info7);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.708549, -4.093869 \n\nDistance to Start: 0.9km \n\nDistance to End: 0.2km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(llanInfo8)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.llan_info8);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.708549, -4.093869 \n\nDistance to Start: 1.1km \n\nDistance to End: 0km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.BLUE);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo1)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info1);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.564539, -3.989258 \n\nDistance to Start: 0.3km \n\nDistance to End: 6.7km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo2)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info2);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.568094, -4.008564 \n\nDistance to Start: 1.0km \n\nDistance to End: 5.2km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo3)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info3);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.564526, -4.014476 \n\nDistance to Start: 2.6km \n\nDistance to End: 4.4km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo4)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info4);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.565723, -4.025964 \n\nDistance to Start: 3.6km \n\nDistance to End: 3.4km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo5)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info5);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.570491, -4.030966 \n\nDistance to Start: 4.3km \n\nDistance to End: 2.7km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo6)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info6);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.569584, -4.036706 \n\nDistance to Start: 4.8km \n\nDistance to End: 2.2km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo7)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info7);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.569034, -4.03648 \n\nDistance to Start: 4.9km \n\nDistance to End: 2.1km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo8)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info8);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.568433, -4.042663 \n\nDistance to Start: 5.4km \n\nDistance to End: 1.6km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo9)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info9);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.566016, -4.046008 \n\nDistance to Start: 5.8km \n\nDistance to End: 1.2km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo10)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info10);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.564799, -4.053057 \n\nDistance to Start: 6.4km \n\nDistance to End: 0.6km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(sBayInfo11)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.sbay_info11);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.565536, -4.055594 \n\nDistance to Start: 6.6km \n\nDistance to End: 0.4km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.RED);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo1)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info1);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.875972, -3.475789 \n\nDistance to Start: 0.5km \n\nDistance to End: 6.8km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo2)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info2);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.877864, -3.472044 \n\nDistance to Start: 1km \n\nDistance to End: 6.3km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo3)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info3);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.88099, -3.460758 \n\nDistance to Start: 1.8km \n\nDistance to End: 5.5km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo4)){
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info4);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.882847, -3.448173 \n\nDistance to Start: 2.6km \n\nDistance to End: 4.7km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo5)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info5);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.881652, -3.44312 \n\nDistance to Start: 2.9km \n\nDistance to End: 4.4km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo6)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info6);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.882979, -3.438174 \n\nDistance to Start: 3.3km \n\nDistance to End: 4km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo7)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info7);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.88392, -3.436757 \n\nDistance to Start: 3.5km \n\nDistance to End: 3.8km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo8)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info8);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.879878, -3.442562 \n\nDistance to Start: 4km \n\nDistance to End: 3.3km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo9)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info9);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.870233, -3.462732 \n\nDistance to Start: 5.8km \n\nDistance to End: 1.5km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		if(marker.equals(penInfo10)){
			vibrate.vibrate(100);
			Toast t = new Toast(getBaseContext());
			LinearLayout toastLayout = new LinearLayout(getBaseContext());
			toastLayout.setOrientation(LinearLayout.VERTICAL);
			ImageView image = new ImageView(getBaseContext());
			Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.pen_info10);
			img = Bitmap.createScaledBitmap(img, 500, 500, true);
			image.setImageBitmap(img);
			TextView text = new TextView(getBaseContext());
			text.setText("Lat/Lng Coordinates: \n51.868087, -3.46947 \n\nDistance to Start: 6.3km \n\nDistance to End: 1km");
			text.setTypeface(null, Typeface.BOLD);
			text.setTextColor(Color.MAGENTA);
			text.setTextSize(18);
			text.setGravity(Gravity.CENTER);
			toastLayout.addView(image);
			toastLayout.addView(text);
			t.setView(toastLayout);
			t.setDuration(Toast.LENGTH_LONG);
			t.show();
		}
		else{
			markerClicked = true;
		}
		return true;
		
	}
	
	
	public void llangennechTrail(){
		PolylineOptions llanOptions = new PolylineOptions()
		.add(new LatLng(51.702263, -4.089186),
				new LatLng(51.702453, -4.088828),
				new LatLng(51.702614, -4.08886),
				new LatLng(51.702719, -4.088611),
				new LatLng(51.70537, -4.090188),
				new LatLng(51.705601, -4.090284),
				new LatLng(51.705701, -4.090142),
				new LatLng(51.706013, -4.090512),
				new LatLng(51.708091, -4.093044),
				new LatLng(51.708629, -4.093247),
				new LatLng(51.708642, -4.09389),
				new LatLng(51.708449, -4.093933),
				new LatLng(51.708127, -4.096465))
				.width(5)
				.color(Color.BLUE)
				.geodesic(true);
		
		Toast llanToast = Toast.makeText(getApplicationContext(), "Llangennech, Level: Easy, Length: 1.1km", Toast.LENGTH_SHORT);
		llanToast.show();
		
		llanPolyline = googlemap.addPolyline(llanOptions);
		
		llanParking = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.70222, -4.089116))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));
		
		llanAtt1 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.692005, -4.089387))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Always & Forever (Hotel)"));
		
		llanInfo1 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.702541, -4.088828))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		llanInfo2 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.703294, -4.088847))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		llanInfo3 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.705311, -4.09027))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		llanInfo4 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.708041, -4.093001))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		llanInfo5 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.70828, -4.093079))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		llanInfo6 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.708496, -4.093764))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		llanInfo7 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.708449, -4.093933))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		llanInfo8 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.70812, -4.096487))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
	}
	
	
	public void swanseaBayTrail(){
		PolylineOptions swanseaBayOptions = new PolylineOptions()
		.add(new LatLng(51.565679, -3.985781),
				new LatLng(51.565379, -3.986988), new LatLng(51.565056, -3.987831),
				new LatLng(51.564949, -3.988362), new LatLng(51.564539, -3.989258),
				new LatLng(51.564479, -3.98995), new LatLng(51.563805, -3.991709),
				new LatLng(51.563652, -3.992664), new LatLng(51.564009, -3.994107),
				new LatLng(51.564375, -3.994622), new LatLng(51.564489, -3.995212),
				new LatLng(51.564849, -3.995781), new LatLng(51.564822, -3.996178),
				new LatLng(51.564579, -3.996655), new LatLng(51.564889, -3.998184),
				new LatLng(51.565986, -4.000871), new LatLng(51.566296, -4.001134),
				new LatLng(51.566313, -4.001386), new LatLng(51.566526, -4.001719),
				new LatLng(51.566653, -4.002347), new LatLng(51.566733, -4.003237),
				new LatLng(51.566693, -4.003693), new LatLng(51.566743, -4.004149),
				new LatLng(51.567043, -4.00483), new LatLng(51.567077, -4.005077),
				new LatLng(51.567287, -4.005517), new LatLng(51.56778, -4.005624),
				new LatLng(51.568037, -4.006225), new LatLng(51.56796, -4.006552),
				new LatLng(51.567897, -4.00674), new LatLng(51.567913, -4.007411),
				new LatLng(51.567984, -4.007636), new LatLng(51.56796, -4.007947),
				new LatLng(51.568094, -4.008564), new LatLng(51.56821, -4.008655),
				new LatLng(51.568374, -4.00953), new LatLng(51.56835, -4.010715),
				new LatLng(51.5679, -4.013188), new LatLng(51.567193, -4.014899),
				new LatLng(51.566773, -4.015044), new LatLng(51.565426, -4.014261),
				new LatLng(51.564526, -4.014476), new LatLng(51.564836, -4.015999),
				new LatLng(51.564095, -4.021648), new LatLng(51.563969, -4.024207),
				new LatLng(51.564432, -4.025467), new LatLng(51.566993, -4.02691),
				new LatLng(51.56752, -4.028042), new LatLng(51.568264, -4.030387),
				new LatLng(51.570057, -4.030687), new LatLng(51.570448, -4.030623),
				new LatLng(51.570504, -4.031282), new LatLng(51.570074, -4.032538),
				new LatLng(51.570374, -4.033616), new LatLng(51.570311, -4.03397),
				new LatLng(51.570364, -4.034533), new LatLng(51.569591, -4.036389),
				new LatLng(51.569627, -4.036867), new LatLng(51.569461, -4.036931),
				new LatLng(51.569034, -4.03648), new LatLng(51.56857, -4.037097),
				new LatLng(51.567964, -4.041469), new LatLng(51.568487, -4.042494),
				new LatLng(51.56844, -4.042837), new LatLng(51.566746, -4.043449),
				new LatLng(51.566006, -4.044999), new LatLng(51.566506, -4.047445),
				new LatLng(51.566433, -4.048797), new LatLng(51.565569, -4.051485),
				new LatLng(51.564756, -4.052735), new LatLng(51.565152, -4.054564),
				new LatLng(51.565693, -4.055701), new LatLng(51.565396, -4.053024),
				new LatLng(51.565903, -4.052338), new LatLng(51.566603, -4.051909),
				new LatLng(51.567, -4.050782))
				.width(5)
				.color(Color.RED)
				.geodesic(true);
		
		Toast sBayToast = Toast.makeText(getApplicationContext(), "Swansea Bay, Level: Medium, Length: 7km", Toast.LENGTH_SHORT);
		sBayToast.show();
		
		swanseaBayPolyline = googlemap.addPolyline(swanseaBayOptions);
		
		sBayParking = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.565971, -3.98249))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));
		
		sBayAtt1 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.571407, -3.986616))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Verdi's (Cafe/Ice Cream Parlour)"));
		sBayAtt2 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.571594, -3.988686))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("The Coast House (Guest House)"));
		sBayAtt3 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.571921, -3.990274))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("The Bay Brasserie (Restaurant)"));
		sBayAtt4 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.572528, -3.992602))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Bay Divers Scuba Diving"));
		sBayAtt5 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.572628, -3.993664))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("The Kitchen Table (Cafe)"));
		sBayAtt6 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.567809, -4.018228))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Langland Bay Golf Club"));
		sBayAtt7 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.568793, -4.01009))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Langland Bay House (B & B)"));
		
		sBayInfo1 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.564539, -3.989258))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo2 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.568094, -4.008564))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo3 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.564526, -4.014476))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo4 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.565723, -4.025864))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo5 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.570491, -4.030966))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo6 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.569034, -4.03648))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo7 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.569034, -4.03648))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo8 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.568433, -4.042663))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo9 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.566016, -4.046008))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo10 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.564799, -4.053057))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		sBayInfo11 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.565536, -4.055594))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
	}

	
	public void penyfanTrail(){
		PolylineOptions penyfanOptions = new PolylineOptions()
		.add(new LatLng(51.872329, -3.479823),
				new LatLng(51.877389, -3.473665),
				new LatLng(51.882781, -3.450512),
				new LatLng(51.882847, -3.448173),
				new LatLng(51.881496, -3.443924),
				new LatLng(51.88392, -3.436757),
				new LatLng(51.882288, -3.439987),
				new LatLng(51.878367, -3.445137),
				new LatLng(51.873996, -3.453333),
				new LatLng(51.868087, -3.46947),
				new LatLng(51.867901, -3.471572),
				new LatLng(51.872168, -3.478739))
				.width(5)
				.color(Color.MAGENTA)
				.geodesic(true);
		
		Toast penyfanToast = Toast.makeText(getApplicationContext(), "Pen Y Fan, Level: Hard, Length: 7.3km", Toast.LENGTH_SHORT);
		penyfanToast.show();
		
		penyfanPolyline = googlemap.addPolyline(penyfanOptions);
		
		penParking = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.871892, -3.479458))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));
		
		penAtt1 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.88896, -3.459229))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Beacons Experience Adventure Activities"));
		
		penAtt2 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.900537, -3.479726))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Carno Farm (Self-Catering Cottages)"));
		
		penAtt3 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.826093, -3.4496))
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
		.title("Coed Owen Bunkhouse (Guest House)"));
		
		penInfo1 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.875972, -3.475789))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo2 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.877864, -3.472044))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo3 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.88099, -3.460758))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo4 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.882847, -3.448173))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo5 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.881652, -3.44312))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo6 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.882979, -3.438174))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo7 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.88392, -3.436757))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo8 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.879878, -3.442562))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo9 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.870233, -3.462732))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
		
		penInfo10 = googlemap.addMarker(new MarkerOptions()
		.position(new LatLng(51.868087, -3.46947))
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.info)));
	}

}

