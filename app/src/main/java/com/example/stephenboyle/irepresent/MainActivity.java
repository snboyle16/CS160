package com.example.stephenboyle.irepresent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.stephenboyle.irepresent.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity {

    TextView warningTextView;
    Button currLocation;
    Button randLocation;
    ImageButton goButton;
    EditText zipEditText;
    FusedLocationProviderClient currentLocation;

    LocationManager locationManager;
    LocationListener locationListener;


    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currLocation = findViewById(R.id.currLocation);
        goButton = findViewById(R.id.goButton);
        randLocation = findViewById(R.id.randLocation);
        zipEditText = findViewById(R.id.zipEditText);
        warningTextView = findViewById(R.id.warningtextView);
        currentLocation = LocationServices.getFusedLocationProviderClient(this);

        requestQueue = Volley.newRequestQueue(this);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Location: ", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    public void goClicked(View view) {
        if (this.zipEditText.getText().length() < 5) {
            warningTextView.clearComposingText();
            warningTextView.setText("Please Enter a Valid Zipcode!");
        } else {
            warningTextView.setText("");
            Intent intent = new Intent(this, com.example.stephenboyle.irepresent.FetchActivity.class);
            Bundle zip = new Bundle();
            zip.putInt("zipcode", Integer.parseInt(this.zipEditText.getText().toString()));
            intent.putExtras(zip);

            startActivity(intent);
        }

    }

    public void setToFetch(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println("hit");
            System.out.println(location.getLatitude());
            System.out.println("1");
            System.out.println(location.getLongitude());
            Bundle latlon = new Bundle();
            latlon.putDouble("lat", location.getLatitude());
            latlon.putDouble("lon", location.getLongitude());
            Intent intent = new Intent (this, com.example.stephenboyle.irepresent.FetchActivity.class);
            intent.putExtras(latlon);
            startActivity(intent);
        }


    }

    public void randClicked(View view) {
        Intent intent = new Intent (this, com.example.stephenboyle.irepresent.FetchActivity.class);
        startActivity(intent);
    }


}