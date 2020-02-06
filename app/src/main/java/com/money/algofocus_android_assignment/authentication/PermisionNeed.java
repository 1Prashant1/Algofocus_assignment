package com.money.algofocus_android_assignment.authentication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.money.algofocus_android_assignment.R;

import java.util.List;


public class PermisionNeed extends AppCompatActivity  {
    private GoogleMap mMap;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    EditText et_names, et_com_mobiles, et_citys;
    TextView et_emails, tv_select_location;
    Button submit;
    ImageView civ_user;

    private Marker marker;
    Button btnButton;
    private FirebaseAuth userd;

    private String aAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        userd = FirebaseAuth.getInstance();
        // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //  .findFragmentById(R.id.map);
        //  mapFragment.getMapAsync(this);


        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            // do you work now
                            Intent intent = new Intent(PermisionNeed.this, UserDetail.class);
                            startActivity(intent);
                            finish();

                        } else {
                            signout();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();





    }

    private void signout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(PermisionNeed.this, Login.class);
        startActivity(intent);
        finish();

    }
    }