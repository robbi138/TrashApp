package com.example.austin.trashapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.CheckBox;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TrashActivity extends AppCompatActivity {


    private static final String CREATE_CAN_URL = "http://webdev.cse.msu.edu/~robbi138/trashApp/insertCan.php";

    private LocationManager locationManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
    }


    public void onClick(View view)
    {
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
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        CheckBox c = (CheckBox) findViewById(R.id.checkBox);
        boolean checked = c.isChecked();
        String rec;
        if(checked){
            rec = "1";
        }
        else{
            rec = "0";        }

        final String query = CREATE_CAN_URL
                + "?x="
                + Double.toString(latitude)
                + "&y="
                + Double.toString(longitude)
                +"&rec="
                + rec;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(query);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int responseCode = conn.getResponseCode();



                } catch (MalformedURLException e) {
                    // Should never happen

                } catch (IOException ex) {

                }
            }
        }).start();


        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


}

