package com.example.feriteja.projectpasmartgarbage;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    Marker marker1;
    Marker marker2;
    Marker marker3;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference tempat1Ref = database.getReference("tempats1");
    DatabaseReference tempat2Ref = database.getReference("tempats2");
    DatabaseReference tempat3Ref = database.getReference("tempats3");





    int nomber=0;
    int urutan=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
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


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }











///////////////////////////////////////////////////////////////////////////////////////////////
        /*

        SharedPreferences prefs = getSharedPreferences("datanya", MODE_PRIVATE);


        int posisi =0;
        int urutan=1;
        while(posisi<=200&&urutan<=200) {

            final int urutannya= urutan;
            DatabaseReference bagianRef = database.getReference("tempats" + urutan);

            String namanyaa = prefs.getString("nilaiTempat"+urutan, "waiting");
            //  int kapasitas= prefs.getInt("nilaiKapasitas"+urutan, 0);



                bagianRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {
                            try{
                                String kapasitasnya = dataSnapshot.child("kapasitas").getValue(String.class);
                                int kapasitasInteger = Integer.parseInt(kapasitasnya);




                                String latitudeString = dataSnapshot.child("latitude").getValue(String.class);
                                String longitudeString = dataSnapshot.child("longitude").getValue(String.class);

                                final Double latitude = Double.parseDouble(latitudeString);
                                final Double longitude = Double.parseDouble(longitudeString);



                            } catch (Exception e){

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            if (namanyaa.equals("kosong")) {


            } else {

                posisi++;
            }

            urutan++;
        }

*/
/////////////////////////////////////////////////////////////////////

        SharedPreferences prefsLetak = getSharedPreferences("letaknya", MODE_PRIVATE);
        SharedPreferences prefs = getSharedPreferences("kaka", MODE_PRIVATE);
        int jumlah =prefs.getInt("nilai",6);



        // LatLng[] point_new = new LatLng[jumlah];
        final ArrayList<LatLng> latLngs=new ArrayList<LatLng>();
        final  ArrayList<Integer> kapasitasArray = new ArrayList<Integer>();
        final ArrayList<String>judulArray=new ArrayList<String>();
        final ArrayList<String>statusArray=new ArrayList<String>();
        ArrayList<Category> catList;

        int urutan=1;

        while(urutan<=200){
            final int urutanmark=urutan;
            final  DatabaseReference bagianRef = database.getReference("tempats"+urutanmark);

          bagianRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                    String latitudeString = dataSnapshot.child("latitude").getValue(String.class);
                    String longitudeString = dataSnapshot.child("longitude").getValue(String.class);

                    Double latitude = Double.parseDouble(latitudeString);
                    Double longitude = Double.parseDouble(longitudeString);

                        LatLng tempat2 = new LatLng(latitude, longitude);

                        if(dataSnapshot.hasChild("kapasitas")) {
                            String kapasitasString = dataSnapshot.child("kapasitas").getValue(String.class);

                            int kapasitas = Integer.parseInt(kapasitasString);
                            final String nama = dataSnapshot.child("namaTempat").getValue(String.class);




                            if(dataSnapshot.hasChild("batas")){


                                Calendar cel = Calendar.getInstance();
                                Date sekarang = cel.getTime();
                                Date Batasnya = dataSnapshot.child("batas").getValue(Date.class);



                                if (kapasitas >= 0 && kapasitas <= 50) {
                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_green);
                                    Bitmap b=bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 110, 110, false);

                                    MarkerOptions sampah1 = new MarkerOptions()
                                            .position(tempat2)
                                            .title(nama)
                                            .snippet(kapasitasString + " %")
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));


                                    marker2 = mMap.addMarker(sampah1);

                                    if(sekarang.after(Batasnya)){
                                        BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_brown);
                                        Bitmap bs=bitmapdraws.getBitmap();
                                        Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 110, 110, false);

                                        MarkerOptions sampah1s = new MarkerOptions()
                                                .position(tempat2)
                                                .title(nama)
                                                .snippet(kapasitasString + " %")
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers));


                                        marker2 = mMap.addMarker(sampah1s);
                                    }

                                }
                                else if (kapasitas >= 51 && kapasitas <= 75) {
                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_yellow);
                                    Bitmap b=bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 110, 110, false);

                                    MarkerOptions sampah2 = new MarkerOptions()
                                            .position(tempat2)
                                            .title(nama)
                                            .title(nama)
                                            .snippet(kapasitasString + " %")
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));


                                    marker2 = mMap.addMarker(sampah2);

                                    if(sekarang.after(Batasnya)){
                                        BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_brown);
                                        Bitmap bs=bitmapdraws.getBitmap();
                                        Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 110, 110, false);

                                        MarkerOptions sampah1s = new MarkerOptions()
                                                .position(tempat2)
                                                .title(nama)
                                                .snippet(kapasitasString + " %")
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers));


                                        marker2 = mMap.addMarker(sampah1s);
                                    }

                                }
                                else if (kapasitas >= 76 && kapasitas <= 100) {
                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_red);
                                    Bitmap b=bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 110, 110, false);

                                    MarkerOptions sampah2 = new MarkerOptions()
                                            .position(tempat2)
                                            .title(nama)
                                            .snippet(kapasitasString + " %")
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));


                                    marker2 = mMap.addMarker(sampah2);

                                    if(sekarang.after(Batasnya)){
                                        BitmapDrawable bitmapdraws=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_brown);
                                        Bitmap bs=bitmapdraws.getBitmap();
                                        Bitmap smallMarkers = Bitmap.createScaledBitmap(bs, 110, 110, false);

                                        MarkerOptions sampah1s = new MarkerOptions()
                                                .position(tempat2)
                                                .title(nama)
                                                .snippet(kapasitasString + " %")
                                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarkers));


                                        marker2 = mMap.addMarker(sampah1s);
                                    }



                                }
                            }

                            else {




                                if (kapasitas >= 0 && kapasitas <= 50) {
                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_green);
                                    Bitmap b=bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 110, 110, false);

                                    MarkerOptions sampah1 = new MarkerOptions()
                                            .position(tempat2)
                                            .title(nama)
                                            .snippet(kapasitasString + " %")
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));




                                    marker2 = mMap.addMarker(sampah1);


                                }
                                else if (kapasitas >= 51 && kapasitas <= 75) {
                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_yellow);
                                    Bitmap b=bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 110, 110, false);

                                    MarkerOptions sampah2 = new MarkerOptions()
                                            .position(tempat2)
                                            .title(nama)
                                            .snippet(kapasitasString + " %")
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));




                                    marker2 = mMap.addMarker(sampah2);

                                }
                                else if (kapasitas >= 76 && kapasitas <= 100) {
                                    BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.trash_red);
                                    Bitmap b=bitmapdraw.getBitmap();
                                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 110, 110, false);

                                    MarkerOptions sampah2 = new MarkerOptions()
                                            .position(tempat2)
                                            .title(nama)
                                            .snippet(kapasitasString + " %")
                                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));



                                    marker2 = mMap.addMarker(sampah2);



                                }

                            }

                            }




                   }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            urutan++;
        }




/*
        while(nomber<200&&urutan<=200){
            final int urutanfinal=urutan;
            final int nomberfinal=nomber+1;


        String latitudeString = prefsLetak.getString("latitude"+urutan,"-6.992180");
        String longitudeString = prefsLetak.getString("longitude"+urutan,"107.679387");
        String title = prefsLetak.getString("title"+urutan,"try");
        String snipet = prefsLetak.getString("snipet"+urutan,"try");
        int kapasitas = prefsLetak.getInt("kapasitas"+urutan,0);
        int kondisi = prefsLetak.getInt("kondisi"+urutan,0);



        Double latitude = Double.parseDouble(latitudeString);
        Double longitude = Double.parseDouble(longitudeString);


            latLngs.add(nomber, new LatLng(latitude,longitude));
            latLngs.set(nomber, new LatLng(latitude,longitude));

            kapasitasArray.add(nomber, kapasitas);
            kapasitasArray.set(nomber, kapasitas);
            judulArray.add(nomber, title);
            judulArray.set(nomber, title);
            statusArray.add(nomber,snipet);
            statusArray.set(nomber,snipet);


      //  point_new[nomber] = new LatLng(latitude,longitude);



            if(title.equals("mencoba"))
            {


            }
            else if(title.equals("try")) {

            }

            else {


                if (kondisi == 0) {
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.trash_green);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 115, false);


                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLngs.get(nomber))
                            .title(judulArray.get(nomber))
                            .snippet(String.valueOf(kapasitasArray.get(nomber)) + "%")
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );


                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                            final EditText input = new EditText(MapsActivity.this);
                            input.setSingleLine();
                            input.setPadding(50, 0, 50, 0);

                            alert.setTitle("a");
                            alert.setMessage("Masukan nama baru:");
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String value = input.getText().toString().trim();
                                  //  DatabaseReference tempatRef = database.getReference("tempats" + urutan);
                                  //  tempatRef.child("namaTempat").setValue(value);


                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    });

                }

                if (kondisi == 1) {
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.trash_yellow);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 115, false);


                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLngs.get(nomber))
                            .title(judulArray.get(nomber))
                            .snippet(String.valueOf(kapasitasArray.get(nomber)) + "%")
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );


                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                            final EditText input = new EditText(MapsActivity.this);
                            input.setSingleLine();
                            input.setPadding(50, 0, 50, 0);

                            alert.setTitle("s");
                            alert.setMessage("Masukan nama baru:");
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String value = input.getText().toString().trim();
                                 //   DatabaseReference tempatRef = database.getReference("tempats" + urutan);
                                 //   tempatRef.child("namaTempat").setValue(value);


                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    });

                }

                if (kondisi == 2) {
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.trash_red);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 115, false);

                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLngs.get(nomber))
                            .title(judulArray.get(nomber))
                            .snippet(String.valueOf(kapasitasArray.get(nomber)) + "%")
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );


                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                            final EditText input = new EditText(MapsActivity.this);
                            input.setSingleLine();
                            input.setPadding(50, 0, 50, 0);

                            alert.setTitle("s");
                            alert.setMessage("Masukan nama baru:");
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String value = input.getText().toString().trim();
                                    DatabaseReference tempatRef = database.getReference("tempats" +urutan);
                              tempatRef.child("namaTempat").setValue(value);


                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    });

                }

                if (kondisi == 3) {
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.trash_brown);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 115, false);

                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLngs.get(nomber))
                            .title(judulArray.get(nomber))
                            .snippet(String.valueOf(kapasitasArray.get(nomber)) + "%")
                            .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
                    );


                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            final AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                            final EditText input = new EditText(MapsActivity.this);
                            input.setSingleLine();
                            input.setPadding(50, 0, 50, 0);

                            alert.setTitle("c");
                            alert.setMessage("Masukan nama baru:");
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String value = input.getText().toString().trim();
                              //      DatabaseReference tempatRef = database.getReference("tempats" + urutan);
                             //       tempatRef.child("namaTempat").setValue(value);


                                }
                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                            alert.show();
                        }
                    });

                }
            }


                nomber++;
            urutan++;
        }*/


        }










    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }





    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }


        Intent intent= getIntent();

        //Place current location marker
        Double latitude = intent.getDoubleExtra("latitude",location.getLatitude());
        Double longitude = intent.getDoubleExtra("longitude",location.getLongitude());
        int zoom= intent.getIntExtra("zoom",11);
        LatLng latLng = new LatLng(latitude, longitude);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }



}
