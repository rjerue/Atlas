package com.example.liam.atlas;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.view.View;
import android.provider.MediaStore;
import android.widget.ImageView;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //private Button cameraButton;
    private ImageView mImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mImageView = (ImageView) findViewById(R.id.imageView);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
            //ActivityCompat.requestPermissions(this,
                    //new String[]{"android.permission.CAMERA"}, REQUEST_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            //mImageView.setVisibility(View.VISIBLE);

        }
    }




}
