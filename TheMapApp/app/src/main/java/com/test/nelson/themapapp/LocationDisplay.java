package com.test.nelson.themapapp;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocationDisplay extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_display);
        setUpMapIfNeeded();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }




    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private Marker startPos;
    private Marker newPos;

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
//        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));
        mMap.setMyLocationEnabled(true);
        
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location mylocation = locationManager.getLastKnownLocation(provider);
        
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        
        double latitude = mylocation.getLatitude();
        double longitude = mylocation.getLongitude();
        
        LatLng latLng = new LatLng(latitude, longitude);
        
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        startPos = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));
    }

    private void handleNewLocation(Location location, String[] info) {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(info[0])
                .snippet(info[1]);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        newPos = mMap.addMarker(options);
        newPos.showInfoWindow();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Set up params, add to it with params.put("key", "value")
        HashMap<String, String> params = new HashMap<String, String>();



        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh:
//                Toast.makeText(getApplicationContext(), "Refreshed!", Toast.LENGTH_SHORT).show();

                // BEGIN POST REQUEST


                String url = "https://api.parse.com/1/functions/hello";


                // Set up Listeners
                JsonObjectRequest req = new JsonObjectRequest(url, new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Location loc = new Location("loc");
                                    String[] info = new String[2];
                                    loc.setLatitude(response.getJSONObject("result").getDouble("lat"));
                                    loc.setLongitude(response.getJSONObject("result").getDouble("long"));
                                    info[0] = response.getJSONObject("result").getString("name");
                                    info[1] = response.getJSONObject("result").getString("desc");

                                    handleNewLocation(loc, info);

                                    Button mapButton = (Button)findViewById(R.id.map_button);
                                    mapButton.setText(response.getJSONObject("result").getString("name"));

                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    //Toast.makeText(getApplicationContext(), "Response is: "+ response.toString(), Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.e("Error: ", error.getMessage());
                            }
                        }

                ) {
                    // Add Headers to Request
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Parse-Application-Id", "8iB1L60tFmDFbJqEjsoBkHhufhZX804LYOFgbAqW");
                        headers.put("X-Parse-REST-API-Key", "r4CMjjqu4pKt7XEqEmjtXaO8LbAZ0aeGUHshs8k6");
                        return headers;
                    }
                };
                
                queue.add(req);
                
                return true;

            case R.id.menu_reset:

                if(newPos != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPos.getPosition(), 14));
                    Button mapButton = (Button)findViewById(R.id.map_button);
                    mapButton.setText("HARBOR");
                    newPos.remove();
                    startPos.showInfoWindow();
                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }

            case R.id.menu_payload:
                String copylot = "https://www.copylot.io/api/v1.0/android/payload";

                JsonObjectRequest request = new JsonObjectRequest(copylot, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    VolleyLog.v("Response:%n %s", response.toString(4));
                                    Toast.makeText(getApplicationContext(), "Response is: "+ response.toString(), Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                    }
                }) {
                    // Add Headers to Request
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        String auth = "Basic "
                                + Base64.encodeToString(("4:rVUa6e4xx1avlMiFypg0ca6yDN8nLBtpPU4Y").getBytes(),
                                Base64.NO_WRAP);
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };

// add the request object to the queue to be executed
                queue.add(request);


                return true;



            default:
                return super.onOptionsItemSelected(item);


        }
    }

}

