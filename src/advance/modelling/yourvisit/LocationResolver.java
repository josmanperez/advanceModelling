package advance.modelling.yourvisit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import advance.modelling.yourvistit.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LocationResolver extends Activity {

	Location location;
	PlaceRecord place = null;
	private final String TAG = "GPS-Tracking";

	ProgressBar mProgressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.location_resolver);

		Intent pastIntent = getIntent();

		mProgressBar = (ProgressBar) findViewById(R.id.progressBarLocationResolver);

		double latitude = pastIntent.getDoubleExtra("Latitude", 0);
		double longitude = pastIntent.getDoubleExtra("Longitude", 0);

		// Toast.makeText(this, "lat: " + latitude + " Lon: " + longitude,
		// Toast.LENGTH_LONG).show();

		location = new Location("locationGeoNames");
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		// PlaceRecord placeRecord = new PlaceRecord(location);

		new PlaceDownloaderTask2().execute(location);

		// String countryName = placeRecord.getCountryName();
		// Log.i("Resolver", countryName);
		// Toast.makeText(this, countryName, Toast.LENGTH_LONG).show();

	}

	class PlaceDownloaderTask2 extends
			AsyncTask<Location, Integer, PlaceRecord> {

		// Change to false if you don't have network access
		private static final boolean HAS_NETWORK = true;

		Bitmap bmp;

		// TODO - put your www.geonames.org account name here.
		private static final String USERNAME = "josmanperez";

		private HttpURLConnection mHttpUrl;
		// private WeakReference<PlaceViewActivity> mParent;
		private final Bitmap mStubBitmap = null;
		private final Location mockLoc1 = new Location(
				LocationManager.NETWORK_PROVIDER);
		private final Location mockLoc2 = new Location(
				LocationManager.NETWORK_PROVIDER);

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected PlaceRecord doInBackground(Location... location) {

			if (HAS_NETWORK) {

				place = getPlaceFromURL(generateURL(USERNAME, location[0]));

				if ("" != place.getCountryName()) {
					place.setLocation(location[0]);
				} else {
					place = null;
				}

			} else {
				place = new PlaceRecord(location[0]);
				// Log.i(TAG,location[0].toString());
				if (location[0].distanceTo(mockLoc1) < 100) {
					place.setCountryName("United States");
					place.setPlace("The Greenhouse");
					place.setFlagBitmap(mStubBitmap);
				} else {
					place.setCountryName("United States");
					place.setPlace("Berwyn");
					place.setFlagBitmap(mStubBitmap);
				}
			}

			return place;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			mProgressBar.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		private PlaceRecord getPlaceFromURL(String... params) {
			String result = null;
			BufferedReader in = null;

			try {

				URL url = new URL(params[0]);
				mHttpUrl = (HttpURLConnection) url.openConnection();
				in = new BufferedReader(new InputStreamReader(
						mHttpUrl.getInputStream()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line + "\n");
				}
				result = sb.toString();

			} catch (MalformedURLException e) {

			} catch (IOException e) {

			} finally {
				try {
					if (null != in) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				mHttpUrl.disconnect();
			}

			// Log.e(TAG,"result: " +result);
			return placeDataFromXml(result);
		}

		private PlaceRecord placeDataFromXml(String xmlString) {
			DocumentBuilder builder;
			String countryName = "";
			String countryCode = "";
			String placeName = "";

			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();

			try {
				builder = factory.newDocumentBuilder();
				Document document = builder.parse(new InputSource(
						new StringReader(xmlString)));
				NodeList list = document.getDocumentElement().getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					Node curr = list.item(i);

					NodeList list2 = curr.getChildNodes();

					for (int j = 0; j < list2.getLength(); j++) {

						Node curr2 = list2.item(j);
						if (curr2.getNodeName() != null) {

							if (curr2.getNodeName().equals("countryName")) {
								countryName = curr2.getTextContent();
							} else if (curr2.getNodeName()
									.equals("countryCode")) {
								countryCode = curr2.getTextContent();
							} else if (curr2.getNodeName().equals("name")) {
								placeName = curr2.getTextContent();
							}
						}
					}
				}
			} catch (DOMException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Toast.makeText(getApplicationContext(), "nombre: "+countryName,
			// Toast.LENGTH_LONG).show();

			// Log.e(TAG, "nombre: " + countryName);
			// Log.e(TAG, "place: " + placeName);

			URL url;
			try {
				url = new URL(generateFlagURL(countryCode.toLowerCase()));
				bmp = BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());
				// // imageFlag.setImageBitmap(bmp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// } catch (IOException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
			}

			return new PlaceRecord(generateFlagURL(countryCode.toLowerCase()),
					countryName, placeName);
		}

		private String generateURL(String username, Location location) {

			return "http://www.geonames.org/findNearbyPlaceName?username="
					+ username + "&style=full&lat=" + location.getLatitude()
					+ "&lng=" + location.getLongitude();
		}

		private String generateFlagURL(String countryCode) {
			return "http://www.geonames.org/flags/x/" + countryCode + ".gif";
		}

		@Override
		protected void onPostExecute(PlaceRecord result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			mProgressBar.setVisibility(View.GONE);

			// Refresh the values
			ImageView imageFlag = (ImageView) findViewById(R.id.imageViewFlag);
			imageFlag.setImageBitmap(bmp);

			// try {
			// URL url = new URL(place.getFlagUrl());
			// HttpURLConnection connection = (HttpURLConnection)
			// url.openConnection();
			// connection.setDoInput(true);
			// connection.connect();
			// InputStream input = connection.getInputStream();
			// //Bitmap myBitmap = BitmapFactory.decodeStream(input);
			// //imageFlag.setImageBitmap(myBitmap);
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

			// URL url;
			// try {
			// url = new URL(place.getFlagUrl());
			// Bitmap bmp = BitmapFactory.decodeStream(url.openConnection()
			// .getInputStream());
			// // imageFlag.setImageBitmap(bmp);
			// } catch (MalformedURLException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			if (place != null) {
				TextView textVCountry = (TextView) findViewById(R.id.textViewCountryName);
				textVCountry.setText(place.getCountryName());
				TextView textVPlace = (TextView) findViewById(R.id.textViewResolverPlace);
				textVPlace.setText(place.getPlace());
			}
		}
	}
}
