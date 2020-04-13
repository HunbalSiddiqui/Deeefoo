package com.deeefoo.myappl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

import static com.google.android.gms.tasks.Tasks.await;

public class
Final_Activity extends AppCompatActivity {

    static   ListView final_list;
    static   Button total_price;
    private Button PlaceOrderButton,trackbtn;
    private  FirebaseDatabase fbdata;
    private  DatabaseReference orderref,rec_confirmationdbref;
    private  final_list_adapter adapter;
    private   FirebaseUser fbuser;
    private   place_order_db pod;
    LatLng UserLatLng;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private   FusedLocationProviderClient fusedLocationProviderClient;
    private long already_rated_check=0;
    private Button back_canteen,button_ok,cancel_order;
    private TextView dilivery;
    private LinearLayout place_order,long_press_lay,finding_rider_layout;
    private DatabaseReference number_ping=FirebaseDatabase.getInstance().getReference("Placed Orders");
    private DatabaseReference pendingdbref;
    private DatabaseReference pendingdbref2=FirebaseDatabase.getInstance().getReference("PendingOrder");
    private DatabaseReference earningdbref;
    protected static double userLat,userLng;
    private ProgressBar progressBar;
    protected static boolean check_long_press=false;
    private boolean firstTimeFlag = true;

    //    private    GoogleMap map;
    private Location currentLocation;
    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag) {
                firstTimeFlag = false;
            }

            userLat = currentLocation.getLatitude();
            userLng = currentLocation.getLongitude();
            UserLatLng = new LatLng(userLat,userLng);
            pendingdbref2.child("lat").setValue(currentLocation.getLatitude());
            pendingdbref2.child("lng").setValue(currentLocation.getLongitude());
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        setContentView(R.layout.activity_final_);
        total_price=(Button) findViewById(R.id.total_price);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getSupportActionBar().hide();
        trackbtn=findViewById(R.id.trackbtn);
        dilivery=findViewById(R.id.delivery);
        place_order=findViewById(R.id.place_order);
        long_press_lay=findViewById(R.id.long_press_lay);
        back_canteen= findViewById(R.id.back_canteen);
        button_ok=findViewById(R.id.button_ok);
        cancel_order=findViewById(R.id.cancel_order);
        fbuser= FirebaseAuth.getInstance().getCurrentUser();
        fbdata=FirebaseDatabase.getInstance();
        orderref=fbdata.getReference("Placed Orders");
        rec_confirmationdbref=fbdata.getReference("Placed Orders").child(fbuser.getPhoneNumber()+" ping");
        rec_confirmationdbref.child("ping").setValue("0");
        pendingdbref=FirebaseDatabase.getInstance().getReference().child("PendingOrder");
        earningdbref =FirebaseDatabase.getInstance().getReference("RidersEarningDatabase");
        finding_rider_layout=findViewById(R.id.layout_findingRider);
        progressBar=findViewById(R.id.findingprogressbar);
        if(check_long_press)
        {
            long_press_lay.setVisibility(View.GONE);
        }
        if(Chart_Static_Variable.total>0){Chart_Static_Variable.total=0;}
        calculatetotal();
        final int hour= Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        final int day=Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        earningdbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot earningSnapshot) {
                dilivery.setText("Delivery Charges = "+earningSnapshot.child("Delivery Charges").getValue().toString());
                Chart_Static_Variable.total+=Integer.parseInt(earningSnapshot.child("Delivery Charges").getValue().toString());//for per order charges.
                if(Chart_Static_Variable.total!=0) {
                    total_price.setText("TOTAL PRICE  ="+"  "+String.valueOf(Chart_Static_Variable.total)+" Rs");
                    total_price.setEnabled(false);
                }
                final_list=(ListView) findViewById(R.id.final_list);
                PlaceOrderButton=(Button) findViewById(R.id.PlaceOrderbtn);

                adapter=new final_list_adapter(Final_Activity.this,Chart_Static_Variable.selected_menu_list,Chart_Static_Variable.total_price_list,Chart_Static_Variable.canteen_list);
                final_list.setAdapter(adapter);
                PlaceOrderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(Chart_Static_Variable.total<100)
                        {
                            Toast.makeText(Final_Activity.this, "Minimum Order Price must be 100rs.", Toast.LENGTH_SHORT).show();
                        }
                    else   if(hour>=Integer.parseInt(earningSnapshot.child("Timestart").getValue().toString())&&hour <Integer.parseInt(earningSnapshot.child("Timeend").getValue().toString())&&day!=Integer.parseInt(earningSnapshot.child("Weekoff1").getValue().toString())&&day!=Integer.parseInt(earningSnapshot.child("Weekoff2").getValue().toString()))
                        {
                            try
                            {
                                if (MainActivity.KU.contains(UserLatLng)) {
                                    if (userLat != 0 || userLng != 0) {
                                        if (MenuActivity.price_check <= 3000 && MenuActivity.price_check >= 100) {
                                            //                    Toast.makeText(Final_Activity.this,"latlng",Toast.LENGTH_LONG);

                                            pendingdbref.child("Number").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.getValue().toString().equalsIgnoreCase("0000")) {
                                                        getValues();
                                                        pendingdbref.child("Number").setValue(fbuser.getPhoneNumber());
                                                        pendingdbref.child("CanteenName").setValue(searc_frag.Clicked_Button_TextHolder);
                                                        pendingdbref.child("OrderList").setValue(pod);
                                                        pendingdbref.child("Discounted Price").setValue(Chart_Static_Variable.discount);
                                                        final_list.setEnabled(false);
                                                        PlaceOrderButton.setEnabled(false);
                                                        back_canteen.setEnabled(false);
                                                        finding_rider_layout.setVisibility(View.VISIBLE);
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        orderref.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                rec_confirmationdbref.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.child("ping").getValue().toString().equals("1")) {
                                                                            progressBar.setVisibility(View.GONE);
                                                                            finding_rider_layout.setVisibility(View.GONE);
                                                                            place_order.setVisibility(View.VISIBLE);
                                                                            orderref.child(fbuser.getPhoneNumber()).setValue(pod);
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

                                                    } else {
                                                        Toast.makeText(Final_Activity.this, "Our all riders are busy at the moment, kindly try again in few minutes.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                        } else {
                                            if (MenuActivity.price_check < 100) {
                                                Toast.makeText(Final_Activity.this, "The minimum limit for order is 100 Rs. Please go back to menu and select more items.", Toast.LENGTH_SHORT).show();

                                            } else if (MenuActivity.price_check > 3000) {
                                                Toast.makeText(Final_Activity.this, "The maximum limit for order is 3000 Rs.Please remove items", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        Toast.makeText(Final_Activity.this, "Please turn on your location", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Final_Activity.this, "You can ONLY place order from University Of Karachi", Toast.LENGTH_LONG).show();
                                }
                            }
                            catch(Exception e)
                            {
                                Toast.makeText(Final_Activity.this, "Please make sure you are in the premises of University of Karachi and your device location is on.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            {
                                Toast.makeText(Final_Activity.this, "You can only place order between university timings (10 : 00 AM to 8 : 00 PM) \n Also we do not operate on Saturday and Sunday", Toast.LENGTH_SHORT).show();
                            }

                    }
                });
                final_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Chart_Static_Variable.total=Chart_Static_Variable.total-Chart_Static_Variable.total_price_list.get(i);
//                        Chart_Static_Variable.discount=Chart_Static_Variable.discount-Chart_Static_Variable.total_discount_list.get(i);
                        MenuActivity.price_check=MenuActivity.price_check-Chart_Static_Variable.total_price_list.get(i);
                        Chart_Static_Variable.total_price_list.remove(i);
                        Chart_Static_Variable.selected_menu_list.remove(i);
                        Chart_Static_Variable.total_discount_list.remove(i);
                        Chart_Static_Variable.chart_count--;
                        Cal_After_Rem();
                        adapter.notifyDataSetChanged();
//                        total_price.setText("TOTAL PRICE    ="+"  "+String.valueOf(Chart_Static_Variable.total));
//                        Toast.makeText(Final_Activity.this, "total price = "+Chart_Static_Variable.total+" and total discount = "+Chart_Static_Variable.discount, Toast.LENGTH_SHORT).show();
                        if((Chart_Static_Variable.total==0 && Chart_Static_Variable.chart_count==0) ||(Chart_Static_Variable.total==Integer.parseInt(earningSnapshot.child("Delivery Charges").getValue().toString())))
                        {
                            PlaceOrderButton.setEnabled(false);
                            PlaceOrderButton.setVisibility(View.GONE);
                            Chart_Static_Variable.total_price_list.clear();
                            Chart_Static_Variable.selected_menu_list.clear();
                            Chart_Static_Variable.total=0;
                            Chart_Static_Variable.discount=0;
                            MenuActivity.price_check=0;
                            Intent intent = new Intent(Final_Activity.this,MenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
            trackbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chart_Static_Variable.selected_menu_list.clear();
                    Chart_Static_Variable.canteen_list.clear();
                    Chart_Static_Variable.chart_count=0;
                    Chart_Static_Variable.total_price_list.clear();
//                    number_ping.child(fbuser.getPhoneNumber()+" ping").child("delievered").setValue("0");
                    Intent intent=new Intent(Final_Activity.this,TrackOrder_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("Exit",true);
                    startActivity(intent);
                    finish();
//                    System.exit(1);

                }
            });
        back_canteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chart_Static_Variable.total=0;
                Chart_Static_Variable.discount=0;
                Intent intent = new Intent(Final_Activity.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long_press_lay.setVisibility(View.GONE);
            }
        });
        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingdbref.child("CanteenName").setValue("Default");
                pendingdbref.child("Number").setValue("0000");
                pendingdbref.child("OrderList").removeValue();
                pendingdbref.child("lat").setValue("0");
                pendingdbref.child("lng").setValue("0");
                Chart_Static_Variable.selected_menu_list.clear();
                Chart_Static_Variable.canteen_list.clear();
                Chart_Static_Variable.chart_count=0;
                Chart_Static_Variable.total_price_list.clear();
                Chart_Static_Variable.total_discount_list.clear();
                Chart_Static_Variable.total=0;
                Chart_Static_Variable.discount=0;
                MenuActivity.price_check=0;
                Intent intent=new Intent(Final_Activity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Exit",true);
                startActivity(intent);
                finish();
            }
        });
        Button applybtn= findViewById(R.id.apply_promo);
       final EditText promotxt=findViewById(R.id.promo_code_text);
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("DiscountPromos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        char check = 'n';
                        for(DataSnapshot postsnapshot : dataSnapshot.getChildren())
                        {
                            if(postsnapshot.getKey().toString().equals(promotxt.getText().toString()))
                            {
                                check='y';
                              percentage(postsnapshot.getValue().toString());
                              Toast.makeText(Final_Activity.this, "PromoCode Applied", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if(check=='n'){
                            Toast.makeText(Final_Activity.this, "Invalid PromoCode", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });

    }

    //calculation function
    private void calculatetotal(){
        Chart_Static_Variable.total=0;
        for(int i=0;i<Chart_Static_Variable.total_price_list.size();i++)
        {
            Chart_Static_Variable.total=Chart_Static_Variable.total+Chart_Static_Variable.total_price_list.get(i);
            Chart_Static_Variable.discount=Chart_Static_Variable.discount+Chart_Static_Variable.total_discount_list.get(i);
            Toast.makeText(this, ""+Chart_Static_Variable.total, Toast.LENGTH_SHORT).show();
        }
    }
    //calculation after removing
    private void Cal_After_Rem(){
        earningdbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Chart_Static_Variable.total=0;
//                promotxt.setText(0);
                for(int i=0;i<Chart_Static_Variable.total_price_list.size();i++)
                {
                    Chart_Static_Variable.total=Chart_Static_Variable.total+Chart_Static_Variable.total_price_list.get(i);
                    Chart_Static_Variable.discount=Chart_Static_Variable.discount+Chart_Static_Variable.total_discount_list.get(i);
                }
                Chart_Static_Variable.total+=Integer.parseInt(dataSnapshot.child("Delivery Charges").getValue().toString());//for per order charges.
                total_price.setText("TOTAL PRICE    ="+"  "+String.valueOf(Chart_Static_Variable.total));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void percentage(String getval){
//        Toast.makeText(Final_Activity.this, getval+" ", Toast.LENGTH_SHORT).show();
        int temp= (int) (Chart_Static_Variable.total *
                        (Double.parseDouble(getval) / 100));
        total_price.setText("TOTAL PRICE  ="+"  "+(Chart_Static_Variable.total-temp)+" Rs");
    }
    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(2000);


        //Ask For Allowing Location Sharing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Final_Activity.this,
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


    private void getValues(){
        pod=new place_order_db(Chart_Static_Variable.selected_menu_list,String.valueOf(Chart_Static_Variable.total),searc_frag.Clicked_Button_TextHolder,already_rated_check);
    }



    long backtime;
    @Override
    public void onBackPressed() {

        Toast backtoast=Toast.makeText(this,"",Toast.LENGTH_SHORT);
        if(backtime+2000>System.currentTimeMillis())
        {
            backtoast.cancel();
            Chart_Static_Variable.selected_menu_list.clear();
            Chart_Static_Variable.canteen_list.clear();
            Chart_Static_Variable.chart_count=0;
            Chart_Static_Variable.total_price_list.clear();
            Chart_Static_Variable.total_discount_list.clear();
            Intent intent=new Intent(Final_Activity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MenuActivity.price_check=0;
            intent.putExtra("Exit",true);
            startActivity(intent);
            finish();
            System.exit(1);
            return;
        }
        else {
            backtoast=Toast.makeText(this, "Press again to go on main screen.", Toast.LENGTH_SHORT);
            backtoast.show();
        }
        backtime=System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        startCurrentLocationUpdates();
    }
}
