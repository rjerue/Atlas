package com.example.liam.atlas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.view.View;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import static com.example.liam.atlas.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationProvider.LocationCallback {

    private GoogleMap mMap;
    private LocationProvider mLocationProvider;
    //private Button cameraButton;
    private ImageView mImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Location currentLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        super.onCreate(savedInstanceState);
        mLocationProvider = new LocationProvider(this, this);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        mImageView = (ImageView) findViewById(R.id.imageView);

        ImageButton imgbtnZoom = (ImageButton) findViewById(R.id.zoomButton);
        imgbtnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        ImageButton imgbtnUnzoom = (ImageButton) findViewById(R.id.unzoomButton);
        imgbtnUnzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
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
        mMap.setInfoWindowAdapter(new MapItemAdapter(this));
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();

    }

    public void handleNewLocation(Location location) {
        if (location != null)
        {
            currentLocation = location;
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(20)                   // Sets the zoom
                   // .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("You are here"));
        }
    }

    public void cameraButtonClick(View view){
        if (this.hasPermissionInManifest(this, "android.permission.CAMERA"))
            dispatchTakePictureIntent();
        else{
            System.out.println("poop");
            dispatchTakePictureIntent();
        }
    }

    public boolean hasPermissionInManifest(Context context, String permissionName) {
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            final String[] declaredPermissions = packageInfo.requestedPermissions;
            if (declaredPermissions != null && declaredPermissions.length > 0) {
                for (String p : declaredPermissions) {
                    System.out.println("Permission " + p);
                    if (p.equals(permissionName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
        return false;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Coordinate c = new Coordinate(currentLocation.getLatitude(), currentLocation.getLongitude(), imageBitmap);
            Marker m;
            m = mMap.setInfoWindowAdapter(new MapItemAdapter(this)).addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.getLatitude() +0.001 , currentLocation.getLongitude()))
            );
            m.setTag(imageBitmap);
            //MarkerOptions m = new MarkerOptions().position(new LatLng(currentLocation.getLatitude()+2,currentLocation.getLongitude())).title("HELLO IMAGE");
            //mMap.addMarker(m);
            //m.visible(true);
            //mImageView.setImageBitmap(imageBitmap);
         }
    }
}
