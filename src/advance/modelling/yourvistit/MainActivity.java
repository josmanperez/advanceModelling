package advance.modelling.yourvistit;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnShowLocation;

	private final String TAG = "GPS-Tracking";
	private List<Address> address = null;
	private final int MAX_NUMBER_OF_RESULT_ADDRESS = 1;

	// GPSTracker class
	GPSTracker gps;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// This is for showing the address
		final Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

		btnShowLocation = (Button) findViewById(R.id.buttonRetrieve);

		// show location button click event
		btnShowLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// create class object
				gps = new GPSTracker(MainActivity.this);

				// check if GPS enabled
				if (gps.canGetLocation()) {

					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();

					// \n is for new line
					Toast.makeText(
							getApplicationContext(),
							"Your Location is - \nLat: " + latitude
									+ "\nLong: " + longitude, Toast.LENGTH_LONG)
							.show();

					// Show the city on the text field
					TextView textLatitude = (TextView) findViewById(R.id.latitudeText);
					TextView textLongitude = (TextView) findViewById(R.id.longitudText);

					// This is for shown the address
					// Geocoder geoCoder2 = new
					// Geocoder(getApplicationContext(),
					// Locale.getDefault());
					// List<Address> addresses = geoCoder2.getFromLocation(
					// latitude, longitude, MAX_NUMBER_OF_RESULT_ADDRESS);
					// e.printStackTrace();

					// textView.setText(address.toString());
					textLatitude.setText("Latitude: "
							+ Double.valueOf(latitude).toString());
					textLongitude.setText("Longitude: "
							+ Double.valueOf(longitude).toString());
					textLatitude.setVisibility(View.VISIBLE);
					textLongitude.setVisibility(View.VISIBLE);
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
