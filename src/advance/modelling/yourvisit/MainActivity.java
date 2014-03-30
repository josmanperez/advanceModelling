package advance.modelling.yourvisit;

import java.util.List;

import advance.modelling.yourvistit.R;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnShowLocation;
	ImageButton btnShowLocationImage;

	private ProgressBar mProgressBar;
	private TextView addressName;

	private final String TAG = "GPS-Tracking";
	private List<Address> address = null;
	private final int MAX_NUMBER_OF_RESULT_ADDRESS = 1;

	private boolean FLAG_FOR_VISIBILITY = false;

	private double latitude = 0.0;
	private double longitude = 0.0;

	// GPSTracker class
	GPSTracker gps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// This is for showing the address
		// final Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

		mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);

		btnShowLocationImage = (ImageButton) findViewById(R.id.searchImageButton);

		addressName = (TextView) findViewById(R.id.textAddress);

		// show location button click event
		btnShowLocationImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// create class object
				gps = new GPSTracker(MainActivity.this);

				// check if GPS enabled
				if (gps.canGetLocation()) {

					latitude = gps.getLatitude();
					longitude = gps.getLongitude();

					// \n is for new line
					// Toast.makeText(
					// getApplicationContext(),
					// "Your Location is - \nLat: " + latitude
					// + "\nLong: " + longitude, Toast.LENGTH_LONG)
					// .show();

					// Show the city on the text field
					TextView textLatitude = (TextView) findViewById(advance.modelling.yourvistit.R.id.latitudeText);
					TextView textLongitude = (TextView) findViewById(advance.modelling.yourvistit.R.id.longitudText);

					// This is for shown the address

					// Geocoder geoCoder2 = new Geocoder(MainActivity.this);
					// try {
					// address = geoCoder2.getFromLocation(latitude,
					// longitude, 3);
					// showToastText(address.toString()).show();
					// Log.i(TAG, "aqui " + address.toString());
					// } catch (Exception e) { // // TODO Auto-generated catch
					// // block
					// Log.i(TAG, e.toString() + " cachis");
					//
					// }

					// Log.i(TAG,address.toString());
					// Geocoder(getApplicationContext(),
					// Locale.getDefault());
					// List<Address> addresses = geoCoder2.getFromLocation(
					// latitude, longitude, MAX_NUMBER_OF_RESULT_ADDRESS);
					// e.printStackTrace();

					// textView.setText(address.toString());

					new LoadProgressBar().execute(latitude, longitude);

					textLatitude.setText("Latitude: "
							+ Double.valueOf(latitude).toString());
					textLongitude.setText("Longitude: "
							+ Double.valueOf(longitude).toString());
					textLatitude.setVisibility(View.VISIBLE);
					textLongitude.setVisibility(View.VISIBLE);
					// if (textLatitude.getVisibility() == View.VISIBLE
					// || textLongitude.getVisibility() == View.VISIBLE) {
					// btnShowLocation.setVisibility(View.INVISIBLE);
					// FLAG_FOR_VISIBILITY = true;
					// }

				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

			}
		});
	}

	public Toast showToastText(String text) {
		return Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(advance.modelling.yourvistit.R.menu.main,
				menu);
		return true;
	}

	// This method is selected when the user clicks on Restaurant
	public void restaurantChoice(View v) {
		if (longitude == 0.0 && latitude == 0.0) {
			showToastText("Please find your location first").show();
		} else {
			Intent i = new Intent(this, Restaurant.class);
			Log.i(TAG, "lat: " + latitude);
			i.putExtra("Lat", latitude);
			i.putExtra("Long", longitude);
			startActivity(i);
		}
	}
	
	
	// This method is selected when a user clicks on drinks button
	public void drinksChoice(View v) {
		if (longitude == 0.0 && latitude == 0.0) {
			showToastText("Please find your location first").show();
		} else {
			Intent i = new Intent(this, Drinks.class);
			i.putExtra("Lat", latitude);
			i.putExtra("Long", longitude);
			startActivity(i);
		}

	}
	
	// This method is shown when a user clicks on hotels button
	public void hotelChoice(View v) {
		if (longitude == 0.0 && latitude == 0.0) {
			showToastText("Please find your location first").show();
		} else {
			Intent i = new Intent(this, Hotels.class);
			i.putExtra("Lat", latitude);
			i.putExtra("Long", longitude);
			startActivity(i);
		}
		
	}

	// AsynTask class
	class LoadProgressBar extends AsyncTask<Double, Integer, Void> {

		private boolean internetAccess = true;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			mProgressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Double... params) {
			// TODO Auto-generated method stub
			Geocoder geoCoder2 = new Geocoder(getApplicationContext());
			try {
				Log.i(TAG, params[0] + " " + params[1]);
				address = geoCoder2.getFromLocation(params[0], params[1], 3);
				Log.i(TAG, "aqui " + address.toString());
			} catch (Exception e) { // // TODO Auto-generated catch block
				Log.i(TAG, e.toString() + " cachis ");
				internetAccess = false;

			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			mProgressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mProgressBar.setVisibility(View.GONE);
			try {
				if (!internetAccess) {
					Toast.makeText(getApplicationContext(),
							"Unable to find location, turn on your internet",
							Toast.LENGTH_LONG).show();
				}
				addressName.setText(address.get(0).getAddressLine(0).toString()
						+ " " + address.get(0).getAddressLine(1).toString());
				addressName.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				Log.i(TAG, e.toString());
			}
			super.onPostExecute(result);
		}
	}

}
