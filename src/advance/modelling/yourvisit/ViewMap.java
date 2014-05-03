package advance.modelling.yourvisit;

import advance.modelling.yourvistit.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewMap extends Activity {
  static final LatLng HAMBURG = new LatLng(53.558, 9.927);
  static final LatLng KIEL = new LatLng(53.551, 9.993);
  private GoogleMap map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.view_map);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
        .getMap();
    Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
        .title("Hamburg"));
    Marker kiel = map.addMarker(new MarkerOptions()
        .position(KIEL)
        .title("Kiel")
        .snippet("Kiel is cool")
        .icon(BitmapDescriptorFactory
            .fromResource(R.drawable.ic_launcher)));

    // Move the camera instantly to hamburg with a zoom of 15.
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

    // Zoom in, animating the camera.
    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

} 
//
//import advance.modelling.yourvistit.R;
//import android.app.Fragment;
//import android.os.Bundle;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapView;
//
//public class ViewMap extends MapActivity {
//
//	private GoogleMap map;
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.view_map);
//
//		MapView mapView = (MapView) findViewById(R.id.map);
//		
//		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
//
//	}
//
//	@Override
//	protected boolean isRouteDisplayed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//}
