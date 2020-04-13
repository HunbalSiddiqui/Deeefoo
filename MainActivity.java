package com.deeefoo.myappl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Time;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, OnCameraIdleListener {
    GoogleMap map;

    private MapView mMapView;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private DatabaseReference number_ping= FirebaseDatabase.getInstance().getReference("Placed Orders");
    private DatabaseReference earningdbref;
    private FirebaseAnalytics mFirebaseAnalytics;
    private TextView timestart,timeend;
    Location CurrentLocation;
    private Marker currentLocationMarker;
    private Location currentLocation;
    private boolean firstTimeFlag = true;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase fdata;
    DatabaseReference nav_name_ref;
    NavigationView navigationView;
    View view;
    TextView  nav_name,nav_mob_num,user_points,GiftPoints;
    FirebaseUser fbuser;
    private static final int Request_Code = 101;
    LatLng MassCom = new LatLng(24.944539, 67.114556);
    LatLng KFC = new LatLng(24.944548, 67.120879);
    LatLng SufiCanteen = new LatLng(24.936852, 67.127518);
    LatLng ChemistryCanteen = new LatLng(24.942656, 67.120637);
    LatLng StudentStarCanteen=new LatLng(24.939883, 67.119904);
    LatLng QasimSamosa = new LatLng(24.939725, 67.119915);
    LatLng NazirChat = new LatLng(24.939516, 67.119995);
    LatLng TahirGalaxy = new LatLng(24.943335, 67.121879);
    LatLng SFF = new LatLng(24.936708,67.115656);
    static LatLngBounds KU = new LatLngBounds(
            new LatLng(24.917801, 67.103111)
            , new LatLng(24.967556, 67.132138)
    );
    protected static  DrawerLayout dlayout;
    private LinearLayout time_check;
    private Button time_check_close;
    private DatabaseReference testdfb=FirebaseDatabase.getInstance().getReference();


    static boolean check=false;
    static int checkMassCom = 0,checkKFC=0,checkSufi=0,checkChemisty=0,checkStudentStar=0,checkQasimSamosa=0,checkNazirChat=0,checkTahir=0,checkSFF=0;


    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && map != null) {
                firstTimeFlag = false;
            }
            // send latitude and longitude of user from here
        testdfb.child("lng").setValue(currentLocation.getLongitude());
            testdfb.child("lat").setValue(currentLocation.getLatitude());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        new FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "K6HD4Z763F8Z6Z8P4BXG");
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        earningdbref =FirebaseDatabase.getInstance().getReference("RidersEarningDatabase");
        setContentView(R.layout.activity_main);
        dlayout=findViewById(R.id.drawer_side);
        time_check=findViewById(R.id.time_check);
        time_check_close=findViewById(R.id.time_check_close);
        timestart=findViewById(R.id.timestart);
        timeend=findViewById(R.id.timeend);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
            }


        navigationView= findViewById(R.id.nav_view);
        view =navigationView.getHeaderView(0);
        nav_name=(TextView) view.findViewById(R.id.side_menu_namebar);
        user_points=(TextView) view.findViewById(R.id.user_points);
        nav_mob_num=(TextView) view.findViewById(R.id.nav_mob_number);
//        GiftPoints=view.findViewById(R.id.giftcode);
//        FirebaseDatabase.getInstance().getReference("Promo Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                   try {
//                       GiftPoints.setText("Gift Code : " + dataSnapshot.child(fbuser.getPhoneNumber()).getValue().toString());
//                   }
//                   catch (Exception e)
//                   { }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });


        fdata=FirebaseDatabase.getInstance();
        nav_name_ref=fdata.getReference("Users");
        fbuser = FirebaseAuth.getInstance().getCurrentUser();
        nav_name_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    nav_name.setText(dataSnapshot.child(fbuser.getPhoneNumber().toString()).child("name").getValue().toString());
                    user_points.setText("Points : "+dataSnapshot.child(fbuser.getPhoneNumber().toString()).child("points").getValue().toString());
                    nav_mob_num.setText(fbuser.getPhoneNumber());
                }catch (Exception e){}

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        nav_name.setText(name);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()==R.id.nav_rating)
                {
                    number_ping.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot delievercheckSnapshot) {
                            try {
                                if((delievercheckSnapshot.child(fbuser.getPhoneNumber()+" ping").child("delievered").getValue().toString()).equals("1"))
                                {
                                    Intent intent=new Intent(MainActivity.this,food_rating.class);
                                    startActivity(intent);
                                }
                                else {Toast.makeText(MainActivity.this, "You order has not been delievered yet", Toast.LENGTH_SHORT).show(); }

                            }catch (Exception e)
                            {
                                Intent intent=new Intent(MainActivity.this,food_rating.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                if(menuItem.getItemId()==R.id.nav_tracking)
                {
                    number_ping.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot delievercheckSnapshot) {
                            try {
                                if((delievercheckSnapshot.child(fbuser.getPhoneNumber()+" ping").child("delievered").getValue().toString()).equals("0"))
                                {
                                    Intent intent=new Intent(MainActivity.this,TrackOrder_activity.class);
                                    startActivity(intent);
                                }
                                else {Toast.makeText(MainActivity.this, "You order has already been delivered", Toast.LENGTH_SHORT).show(); }

                            }catch (Exception e)
                            {
                                Toast.makeText(MainActivity.this, "You have not ordered anything yet. Please order first", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                if(menuItem.getItemId()==R.id.nav_faq)
                {
                    Intent intent = new Intent(MainActivity.this,faq_screen.class);
                    startActivity(intent);

                }
                if(menuItem.getItemId()==R.id.nav_terms)
                {
                    Intent intent = new Intent(MainActivity.this,privacy_policy_screen.class);
                    startActivity(intent);
                }
//                if(menuItem.getItemId()==R.id.invite)
//                {
//                    FirebaseDatabase.getInstance().getReference("Invites").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            double r1 =Math.random();
//                            double r2 =Math.random();
//                            String x1 =String.valueOf(r1).substring(3,4);
//                            String x2 =String.valueOf(r2).substring(2,3);
//                            String s = fbuser.getPhoneNumber();
//                            String subs1=s.substring(4,6);
//                            String subs2=s.substring(7,8);
//                            String receiving_code="get"+subs1+x1;
//                            String sender_code="get"+subs2+x2;
//                            Intent sendIntent = new Intent();
//                            sendIntent.setAction(Intent.ACTION_SEND);
//                            sendIntent.putExtra(Intent.EXTRA_TEXT,dataSnapshot.child("Text").getValue().toString()+"\n"+
//                                    dataSnapshot.child("Link").getValue().toString()+"\n"+dataSnapshot.child("Promo").getValue().toString()+"("+receiving_code+")");
//                            sendIntent.setType("text/plain");
//                            try {
//                                Intent shareIntent = Intent.createChooser(sendIntent, null);
//                                startActivity(shareIntent);
//                                FirebaseDatabase.getInstance().getReference("Promo Users").child(receiving_code).setValue(receiving_code);
//                                FirebaseDatabase.getInstance().getReference("Promo Users").child(fbuser.getPhoneNumber()).setValue(sender_code);
//                            }
//                            catch(Exception e)
//                            {
//                                Toast.makeText(MainActivity.this, "Internet Connectivity issue.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                        }
//                    });

//                }
                if(menuItem.getItemId()==R.id.nav_sign_out) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
        time_check_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

        //CHECK ON EVERY CAMERA POSITION CHANGE
        @Override
        public void onCameraIdle () {
            //Toast.makeText(MainActivity.this, "The camera has stopped moving.",
            // Toast.LENGTH_SHORT).show();
            LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
            fetchData(bounds);
            searc_frag.spinner.setSelection(0);

        }

        //GET THE SCREEN BOUNDS
        private void fetchData (LatLngBounds bounds){

            LatLng newbounds = bounds.northeast;
            LatLng newboundss = bounds.southwest;
            LatLngBounds cameraBounds = new LatLngBounds(
                    new LatLng(newboundss.latitude, newboundss.longitude),
                    new LatLng(newbounds.latitude, newbounds.longitude)
            );
            // CHECKS IF THE SCREEN HAS CANTEEN
            if (cameraBounds.contains(MassCom)) {
                checkMassCom = 1;
            } else checkMassCom = 0;
            if (cameraBounds.contains(TahirGalaxy)) {
                checkTahir = 1;
            } else checkTahir = 0;
            if (cameraBounds.contains(SufiCanteen)) {
                checkSufi = 1;
            } else checkSufi = 0;
            if (cameraBounds.contains(KFC)) {
                checkKFC = 1;
            } else checkKFC = 0;
            if (cameraBounds.contains(ChemistryCanteen)) {
                checkChemisty = 1;
            } else checkChemisty = 0;
            if (cameraBounds.contains(StudentStarCanteen)) {
                checkStudentStar = 1;
            } else checkStudentStar = 0;
            if (cameraBounds.contains(QasimSamosa)) {
                checkQasimSamosa = 1;
            } else checkQasimSamosa = 0;
            if (cameraBounds.contains(NazirChat)) {
                checkNazirChat = 1;
            } else checkNazirChat = 0;
            if (cameraBounds.contains(SFF)) {
                checkSFF = 1;
            } else checkSFF = 0;
        }
        //FETCHING USER LOCATION
        private void startCurrentLocationUpdates() {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(2000);


            //Ask For Allowing Location Sharing
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                    return;
                }

            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
        }


    // After Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {

            //If permission NOT granted then ask for it Again and Again
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Press Allow to move forward", Toast.LENGTH_SHORT).show();

            }
            //If Permission granted Then Start Calling StartCurrentLocationUpdate Method
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();

        }
    }
        //MAP INITIALIZE
        @Override
        public void onMapReady (GoogleMap googleMap){
            map = googleMap;
            map.setOnCameraIdleListener(this);
            map.getUiSettings().setTiltGesturesEnabled(false);
            map.getUiSettings().setCompassEnabled(false);
            map.setBuildingsEnabled(false);

	     // Styling Map
        	boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));


            //SETTING BOUNDS OF UNIVERSITY
            LatLngBounds KU = new LatLngBounds(
                    new LatLng(24.927701, 67.109745)
                    , new LatLng(24.958559, 67.127427)
            );

            googleMap.setLatLngBoundsForCameraTarget(KU);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(KU.getCenter()));
            //FIXING ZOOM
            map.setMaxZoomPreference(25.0f);
            map.setMinZoomPreference(15.0f);
            map.addMarker(new MarkerOptions().position(NazirChat).title("Nazir Chat House").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(MassCom).title("Mass Communication Canteen").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(KFC).title(" Karachi Food Center").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(SufiCanteen).title("Sufi Canteen").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(ChemistryCanteen).title("Nafees Canteen").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(StudentStarCanteen).title("Student Star Canteen").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(QasimSamosa).title("Qasim Samosa").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(TahirGalaxy).title("Tahir Galaxy Juice").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));
            map.addMarker(new MarkerOptions().position(SFF).title("Shaheen Fast Food").icon(BitmapDescriptorFactory.fromResource((R.drawable.marker250))));




        }


    long backtime;
    @Override
    public void onBackPressed() {
        if(dlayout.isDrawerOpen(GravityCompat.START))
        {
            dlayout.closeDrawer(GravityCompat.START);
        }
        else
        super.onBackPressed();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        startCurrentLocationUpdates();
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
        fusedLocationProviderClient = null;
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


