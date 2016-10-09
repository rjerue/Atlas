package com.example.liam.atlas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.ImageView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.liam.atlas.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationProvider.LocationCallback, PinLocationProvider.PinCallback {

    private GoogleMap mMap;
    private LocationProvider mLocationProvider;
    //private Button cameraButton;
    private ImageView mImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Location currentLocation = null;

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SetupPermissions();


        super.onCreate(savedInstanceState);

        mLocationProvider = new LocationProvider(this, this);

        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        SetupDrawer();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);

    }

    private void SetupPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    /*
    TODO: Add Documentation and make this contain menu items
     */
    private void addDrawerItems() {
        String[] osArray = {"Local", "Friends Markers", "Attractions", "Restaurants", "Your Markers", "Settings",};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }

    private void SetupDrawer() {
       // Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

     //   mDrawerArrow = new DrawerArrowDrawable(this);


        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout ,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
              //  getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
           //     getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

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
        GetPinsFromServer();
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
    public void handleNewPin(Location location, Bitmap bitmap){
        currentLocation = location;
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.getLatitude() , currentLocation.getLongitude()))
        );
        marker.setTag(bitmap);
        mMap.setInfoWindowAdapter(new MapItemAdapter(this));
        try {
            CoordinateServerClient.SendMessage(new Coordinate(currentLocation.getLatitude(), currentLocation.getLongitude(),bitmap));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("UGH");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
          //  mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("You are here"));
        }
    }

    public void refreshMapClick(View view){
        GetPinsFromServer();
    }

    public void cameraButtonClick(View view){
        mLocationProvider.connect();
        if (this.hasPermissionInManifest(this, "android.permission.CAMERA"))
            dispatchTakePictureIntent();
        else{
            dispatchTakePictureIntent();
        }
    }

    public void GetPinsFromServer(){
        ArrayList<Coordinate> coordinates = null;
        try {
            coordinates = CoordinateServerClient.ReceiveData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Coordinate coordinate : coordinates) {
            Marker m;
            m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(coordinate.getX(), coordinate.getY()))
            );
            m.setTag(coordinate.getImage());
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
            new PinLocationProvider(this, this, imageBitmap).connect();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
