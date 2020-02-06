package com.money.algofocus_android_assignment.authentication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.money.algofocus_android_assignment.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;




public class UserDetail extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {
    private GoogleMap mMap;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    EditText et_names, et_com_mobiles, et_citys;
    TextView et_emails, tv_select_location;
    Button submit;
    ImageView civ_user, logout;

    private Marker marker;
    Button btnButton;
    private FirebaseAuth userd;

    private String aAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        userd = FirebaseAuth.getInstance();

        // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //  .findFragmentById(R.id.map);
        //  mapFragment.getMapAsync(this);
        findViewById();
        click();
        locationEnabled() ;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location mCurrentLocation = locationResult.getLastLocation();
                LatLng myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                String cityName = getCityName(myCoordinates);
               // Toast.makeText(UserDetail.this, aAddress, Toast.LENGTH_SHORT).show();
                tv_select_location.setText(aAddress);



            }
        };

        FirebaseUser user = userd.getCurrentUser();

        if (userd != null) {
            assert user != null;
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .into(civ_user);

            et_names.setText(user.getDisplayName());
            et_emails.setText(user.getEmail());

        }




        if (Build.VERSION.SDK_INT >= 23) {
            Log.d("mylog", "Getting Location Permission");
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("mylog", "Not granted");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else
                requestLocation();
        } else
            requestLocation();
    }



    private void locationEnabled() {

        LocationManager lm = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
                     new AlertDialog.Builder(UserDetail.this)
                    .setCancelable(false)
                    .setMessage("Turn On your Location Service")
                    .setPositiveButton("Settings", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                     startActivity(new Intent(UserDetail.this, Login.class));
                                    signout();

                                }
                            })
                    .setNegativeButton( "" , null )
                    .show() ;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if the user is not logged in
        //opening the login activity
        if (userd.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, Login.class));
        }

    }

    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(UserDetail.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            aAddress = address;
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
         //   Toast.makeText(UserDetail.this, addresses.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
    }

    private void requestLocation() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);
        Log.d("mylog", "In Requesting Location");
        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            String cityName = getCityName(myCoordinates);
            Toast.makeText(this, cityName, Toast.LENGTH_SHORT).show();
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last location too old getting new location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }
    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        finish();

    }

    private void findViewById() {
        tv_select_location = findViewById(R.id.tv_select_location);
        et_emails = findViewById(R.id.et_email);
        et_com_mobiles = findViewById(R.id.et_com_mobile);
        et_names = findViewById(R.id.et_name);
        submit = findViewById(R.id.save_profile);
        civ_user = findViewById(R.id.civ_user);
        logout = findViewById(R.id.logout);
    }

    private void click() {
        submit.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_profile:
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("mylog", "Not granted");
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else
                        requestLocation();
                } else
                    requestLocation();
                break;
            case R.id.logout:
                new AlertDialog.Builder(UserDetail.this)
                        .setCancelable(false)
                        .setMessage("Do you want to Logout ?")
                        .setPositiveButton("Yes", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        signout();

                                    }
                                })
                        .setNegativeButton( "No" , null )
                        .show() ;

               

        }
    }
}
