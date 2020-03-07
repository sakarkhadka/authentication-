package com.example.fingerprintauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

//
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//

public class homepage extends AppCompatActivity {

    public double longitude;
    public double latitude;
    private String s1, s2;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_homepage);

        //g7 edit here
        requestPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(homepage.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //funtion to get lastlocation of user
        fusedLocationClient.getLastLocation().addOnSuccessListener(homepage.this, new OnSuccessListener<Location>() {
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                }
                s1 = Double.toString(longitude);
                s2 = Double.toString(latitude);
                Log.d("long --> ",s1);
                Log.d("lati ---> ", s2);
                TextView t1;
                t1 = (TextView)findViewById(R.id.location);
                String show = "Lon-" +s1 + ", Lat-" + s2 + " " + "sent";
                t1.setText(show);
            }
        });

        setContentView(R.layout.activity_homepage);
        //g7 edit here
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    //g7 edit here
    public void newpage(View view) {

            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(this, BluetoothActivity.class);
            startActivity(intent1);

    }
}
