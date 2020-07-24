package com.example.fresh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    public static final String EXTRA_TEXT = "com.example.fresh.Extra_TEXT";
    public static final String EXTRA_NUMBER = "com.example.fresh.Extra_NUMBER";

    private GoogleMap mMap;
    private Button button;
    BottomNavigationView navView;
    TextView zipCode;
    double latDouble, lonDouble;
    Location gps_loc=null, network_loc = null, final_loc=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        zipCode = findViewById(R.id.zipCode);

//map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //mpa


        //Jsoupe
        String url = "https://www.google.com/get/sunroof/building/33.399931/-112.02455880000002/#?f=buy";
        Document doc= Jsoup.parse(url);
        Elements words = doc.select("li");
        String wordsOK = words.toString();
        //JSOUPE


        //MAP
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_NETWORK_STATE) !=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Not Granted", Toast.LENGTH_SHORT).show();
        }
        try{
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e){
            e.printStackTrace();
        }

        if (gps_loc !=null){
            final_loc = gps_loc;
            latDouble=final_loc.getLatitude();
            lonDouble=final_loc.getLongitude();
        } else if (network_loc !=null) {
            final_loc = network_loc;
            latDouble = final_loc.getLatitude();
            lonDouble = final_loc.getLongitude();
        } else {
            latDouble=0;
            lonDouble=0;
        }

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latDouble, lonDouble, 1);
            String zipString = addresses.get(0).getPostalCode();
            zipCode.setText("Current Zip Code: "+zipString+" ");


        } catch (IOException e) {
            e.printStackTrace();
        }
//MAP
        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            break;
                        case R.id.report:
                            break;
                        case R.id.info:
                            Intent info = new Intent(MainActivity.this, Info.class);
                            startActivity(info);
                            break;

                    }
                    return false;
                }
            };

    public void openActivity2() {
        EditText editText1 =(EditText) findViewById(R.id.editText);
        String text = editText1.getText().toString();

        EditText editText2= (EditText)findViewById(R.id.editText2);
        String number = editText2.getText().toString();


        Intent intent = new Intent(this, Report.class);
        intent.putExtra(EXTRA_TEXT,text);
        intent.putExtra(EXTRA_NUMBER,number);
        startActivity(intent);
    }



    //Map Start
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.getFocusedBuilding();
        //Store these lat lng values somewhere. These should be constant.

        // Add a marker in Sydney and move the camera
        LatLng current = new LatLng(latDouble, lonDouble);
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(current, 20);
        mMap.animateCamera(location);
    }
    //Map END


}