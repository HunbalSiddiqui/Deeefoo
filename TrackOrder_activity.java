package com.deeefoo.myappl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class TrackOrder_activity extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap map;
    Location CurrentLocation;
    public double riderLat,riderLng=0;
    Marker currentLocationMarker;
    public float bearing;
    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseUser fbuser=mAuth.getCurrentUser();
    private static String rider_name;
    DatabaseReference Rider_Name = FirebaseDatabase.getInstance().getReference("Placed Orders").child(mAuth.getCurrentUser().getPhoneNumber()+" ping").child("Rider Name");
    DatabaseReference Rider_Location_Latitude;
    DatabaseReference Rider_Location_Longitude;
    private DatabaseReference user_location;
    private DatabaseReference orderno=FirebaseDatabase.getInstance().getReference("Placed Orders").child(fbuser.getPhoneNumber()+" ping").child("orderno");
    private TextView order_number;
    private DatabaseReference userpingdb;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_activity);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.maptrack);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        mMapView.getMapAsync(this);
        getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT>=21){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        user=FirebaseAuth.getInstance().getCurrentUser();
        userpingdb=FirebaseDatabase.getInstance().getReference("Placed Orders").child(user.getPhoneNumber()+" ping");
        order_number=findViewById(R.id.order_number);
        orderno.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ordernoSnapshot) {
                order_number.setText("ORDER NUMBER : "+ordernoSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userpingdb.child("delievered").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equalsIgnoreCase("1"))
                {
                    Intent intent = new Intent(TrackOrder_activity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    //MAP INITIALIZE
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //Setting All Defaults as false
        Rider_Name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot RiderNameSnapshot) {
                rider_name=RiderNameSnapshot.getValue().toString();
        map.getUiSettings().setTiltGesturesEnabled(false);
        map.getUiSettings().setCompassEnabled(false);
        map.setBuildingsEnabled(false);
        final Double[] lat = new Double[2];
        final Double[] lng = new Double[2];
        user_location=FirebaseDatabase.getInstance().getReference(rider_name).child("AcceptedOrders").child(fbuser.getPhoneNumber());
        user_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot  userlocationSnapshot) {
                try {
                    final LatLng latLng = new LatLng(Double.parseDouble(userlocationSnapshot.child("lat").getValue().toString()),Double.parseDouble(userlocationSnapshot.child("lng").getValue().toString()));
                    map.addMarker(new MarkerOptions().position(latLng).title("Order Delivery Here"));

                    //Current LatLng



                    Rider_Location_Latitude =FirebaseDatabase.getInstance().getReference(rider_name).child("lat");
                    Rider_Location_Longitude =FirebaseDatabase.getInstance().getReference(rider_name).child("lng");






                    Rider_Location_Latitude.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot RiderLocationLatSnapshot) {
                            riderLat = RiderLocationLatSnapshot.getValue(Double.class);
//                        Toast.makeText(TrackOrder_activity.this, "Lat", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Rider_Location_Longitude.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot RiderLocationLngSnapshot) {
                            riderLng=RiderLocationLngSnapshot.getValue(Double.class);
                            LatLng rider = new LatLng(riderLat,riderLng);
//                        Toast.makeText(TrackOrder_activity.this, "Lng", Toast.LENGTH_SHORT).show();


                            // For the first time to assign 0 to both lat and lng
                            if(currentLocationMarker==null){

                                lat[0]=0.0;
                                lng[0]=0.0;
                            }


                            // Setting Marker For New Location of Rider read from DataBase
                            if (currentLocationMarker == null)
                                currentLocationMarker = map.addMarker(new MarkerOptions().position(rider).flat(true).title("Rider").icon(BitmapDescriptorFactory.fromResource(R.drawable.bikeheight200)));
                            else

                                //Making marker move with Animation when New Location of rider received
                                MarkerAnimation.animateMarkerToGB(currentLocationMarker, rider, new LatLngInterpolator.Spherical());
                            currentLocationMarker.setAnchor(0.5f, 0.5f);
                            currentLocationMarker.setRotation(bearing);

                            // putting both points in bound builder to always show both points on screen
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(rider);
                            builder.include(latLng);

                            LatLngBounds boundsOrder = builder.build();

                            //adding some padding to avoid sticking points with screen border
                            int width = getResources().getDisplayMetrics().widthPixels;
                            int padding = (int) (width*0.25);

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsOrder,padding);
                            try {
                                map.moveCamera(cameraUpdate);
                            }catch (Exception e){}


                            //Measuring Distance Between rider and customer
                            Location user = new Location("");
                            user.setLatitude(Double.parseDouble(userlocationSnapshot.child("lat").getValue().toString()));
                            user.setLongitude(Double.parseDouble(userlocationSnapshot.child("lng").getValue().toString()));

                            Location riderLocation = new Location("");
                            riderLocation.setLatitude(riderLat);
                            riderLocation.setLongitude(riderLng);

                            double distance = riderLocation.distanceTo(user);


                            if(distance<100){
                                // Toast.makeText(MainActivity.this, "Your Order has Arrived", Toast.LENGTH_SHORT).show();

                            }

                            //To get bearing for rotation of rider marker saving older value and then we will
                            //use it for .bearingTo method of starting and ending position
                            Location saveRiderOlderLatLng = new Location("");
                            try {
                                saveRiderOlderLatLng.setLatitude(lat[0]);
                                saveRiderOlderLatLng.setLongitude(lng[0]);
                            }catch (Exception e){}

                            lat[0] = riderLat;
                            lng[0] = riderLng;

                            bearing = saveRiderOlderLatLng.bearingTo(riderLocation);



                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }catch (Exception e)
                {
                    if(getBaseContext().equals(TrackOrder_activity.class))
                    {
                        Intent intent=new Intent(TrackOrder_activity.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Exit",true);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        // Read from the database
        // Current Lat

        // Styling Map
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));

        if (!success) {
            Toast.makeText(this, "map styling failed", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();


    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();

        map = null;
    }


    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
