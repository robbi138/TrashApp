package com.example.austin.trashapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String GET_CAN_URL = "http://webdev.cse.msu.edu/~robbi138/trashApp/canRetrieve.php";


    private LocationManager locationManager = null;
    private SharedPreferences settings = null;

    private double latitude = 0;
    private double longitude = 0;
    private float prevDistance = 0;
    private boolean valid = false;

    private double toLatitude = 0;
    private double toLongitude = 0;
    private int points = 0;

    private final static String TOLAT = "tolat";
    private final static String TOLONG = "tolong";
    private final static String POINTS = "points";

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get the location manager

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a Criteria object
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);

        Location location;
        String bestAvailable = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(bestAvailable);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Add a marker in Sydney and move the camera
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng selfLoc = new LatLng(latitude, longitude);
        final String query = GET_CAN_URL
                + "?x="
                + Double.toString(latitude)
                + "&y="
                + Double.toString(longitude);
        final stringHolder holder = new stringHolder();
        final CountDownLatch latch = new CountDownLatch(1);
        final String[] value = new String[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String response;
                    while ((response = br.readLine()) != null){
                        sb.append(response);
                    }
                    holder.setS(response);
                    value[0] = sb.toString();
                    latch.countDown();

                } catch (MalformedURLException e) {
                    // Should never happen

                } catch (IOException ex) {

                }
            }
        }).start();
        try{
            latch.await();
        }
        catch(InterruptedException e){

        }
        String test = holder.getS();

        if(value[0] != null){
        List<String> coordList = Arrays.asList(value[0].split("~"));
            for(String s : coordList){
                List<String> coords = Arrays.asList(s.split(","));
                double xC = Double.parseDouble(coords.get(0));
                double yC = Double.parseDouble(coords.get(1));
                LatLng Loc = new LatLng(xC, yC);
                mMap.addMarker(new MarkerOptions().position(Loc).title("Trash"));
            }
        }
        mMap.addMarker(new MarkerOptions().position(selfLoc).title("You"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selfLoc));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 19.0f ) );
        mapFragment.onResume();
    }

    public class stringHolder{
        private String str;
        private void setS(String s){
            str = s;
        }
        private String getS(){
            return str;
        }
    }

   static String memberStr;

}
