package com.deeefoo.myappl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class food_rating extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser fbuser;
    private DatabaseReference orderlist;
    private DatabaseReference canteen_name_ref,set_rating_ref,already_rated_check_ref,user_points;
    private final ArrayList<String> orders = new ArrayList<>();
    private ImageView star1,star2,star3,star4,star5;
    private TextView food_name,canteen_name_text;
    private long rating;
    private Button next,food_ok_button;
    private int index=0,points=5;
    private LinearLayout thanks_layout;
    private TextView food_text;
    private long already_rated=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        rating=0;
        setContentView(R.layout.food_rating);
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        mAuth= FirebaseAuth.getInstance();
        fbuser=mAuth.getCurrentUser();
        food_name=findViewById(R.id.food_name);
        canteen_name_text=findViewById(R.id.canteen_name_text);
        next=findViewById(R.id.next_food);
        star1=findViewById(R.id.button_rate_1);
        star2=findViewById(R.id.button_rate_2);
        star3=findViewById(R.id.button_rate_3);
        star4=findViewById(R.id.button_rate_4);
        star5=findViewById(R.id.button_rate_5);
        thanks_layout=findViewById(R.id.thanks_layout);
        food_ok_button=findViewById(R.id.food_ok_button);
        food_text=findViewById(R.id.food_text);
        orderlist= FirebaseDatabase.getInstance().getReference("Placed Orders").child(mAuth.getCurrentUser().getPhoneNumber()).child("list");
        already_rated_check_ref= FirebaseDatabase.getInstance().getReference("Placed Orders").child(mAuth.getCurrentUser().getPhoneNumber()).child("already_rated_check");
        canteen_name_ref= FirebaseDatabase.getInstance().getReference("Placed Orders").child(mAuth.getCurrentUser().getPhoneNumber()).child("canteen_name");
        set_rating_ref=FirebaseDatabase.getInstance().getReference("Canteens");
        user_points=FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getPhoneNumber());
        orderlist.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    orders.addAll((ArrayList<String>)dataSnapshot.getValue());
                    for(int i=0;i<orders.size();i++)
                    {
                        if(orders.get(i).contains("Plates")||orders.get(i).contains("Glasses")||orders.get(i).contains("7 UP")||orders.get(i).contains("DEW")||orders.get(i).contains("PEPSI")||orders.get(i).contains("COCA COLA")||orders.get(i).contains("MIRINDA")||orders.get(i).contains("STING"))
                        {
                            orders.remove(i);
                        }
                    }
                    already_rated_check_ref.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if((dataSnapshot.getValue().toString()).equals("0")){
                                food_name.setText(orders.get(index));
                                next.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        for(int i=0;i<orders.size();i++)
                                        {
                                            if(orders.get(i).contains("Plates")||orders.get(i).contains("Glasses")||orders.get(i).contains("7 UP")||orders.get(i).contains("DEW")||orders.get(i).contains("PEPSI")||orders.get(i).contains("COCA COLA")||orders.get(i).contains("MIRINDA")||orders.get(i).contains("STING"))
                                            {
                                                orders.remove(i);
                                            }
                                        }
                                        star1.setImageResource(R.drawable.star);
                                        star2.setImageResource(R.drawable.star);
                                        star3.setImageResource(R.drawable.star);
                                        star4.setImageResource(R.drawable.star);
                                        star5.setImageResource(R.drawable.star);
                                        if(index < orders.size())  {
                                            canteen_name_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    final String cname= dataSnapshot.getValue().toString();
                                                    canteen_name_text.setText(cname);
                                                    set_rating_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            String food=food_name.getText().toString();
                                                            if(food.contains("Burger")||food.contains("Bun"))
                                                            {
                                                                long final_rating=((long)dataSnapshot.child(cname).child("Burger Points").getValue()) +rating;
                                                                set_rating_ref.child(cname).child("Burger Points").setValue(final_rating);
                                                            }
                                                            else if(food.contains("Shake") || food.contains("Juice"))
                                                            {
                                                                long final_rating= (long)dataSnapshot.child(cname).child("Juice Points").getValue()+rating;
                                                                set_rating_ref.child(cname).child("Juice Points").setValue(final_rating);
                                                            }
                                                            else if(food.contains("Roll"))
                                                            {
                                                                long final_rating= (long)dataSnapshot.child(cname).child("Roll Points").getValue()+rating;
                                                                set_rating_ref.child(cname).child("Roll Points").setValue(final_rating);
                                                            }
                                                            else if(food.contains("Sandwich"))
                                                            {
                                                                long final_rating= (long)dataSnapshot.child(cname).child("Sandwich Points").getValue()+rating;
                                                                set_rating_ref.child(cname).child("Sandwich Points").setValue(final_rating);
                                                            }
                                                            else if(food.contains("Rice")||food.contains("Chilli")||food.contains("Chowmein")||food.contains("Garlic")||food.contains("Jalferezi")||food.contains("Manchurian")||food.contains("Shashlik")||food.contains("Pasta"))
                                                            {
                                                                long final_rating= (long)dataSnapshot.child(cname).child("Chinese Points").getValue()+rating;
                                                                set_rating_ref.child(cname).child("Chinese Points").setValue(final_rating);
                                                            }
                                                            else if(food.contains("Biryani")||food.contains("Paratha")||food.contains("Pulao")||food.contains("Chola")||food.contains("Karhayi")||food.contains("Kaleji")||food.contains("Keema")||food.contains("Nihari")||food.contains("Korma")||food.contains("Sabzi")||food.contains("Chapati")||food.contains("Tandoori"))
                                                            {
                                                                long final_rating= (long)dataSnapshot.child(cname).child("Desi Points").getValue()+rating;
                                                                set_rating_ref.child(cname).child("Desi Points").setValue(final_rating);
                                                            }
                                                            else if(food.contains("Fries"))
                                                            {
                                                                long final_rating= (long)dataSnapshot.child(cname).child("Fries Points").getValue()+rating;
                                                                set_rating_ref.child(cname).child("Fries Points").setValue(final_rating);
                                                            }
                                                            else if(food.contains("Samosa"))
                                                            {
                                                                long final_rating= (long)dataSnapshot.child(cname).child("Samosa Points").getValue()+rating;
                                                                set_rating_ref.child(cname).child("Samosa Points").setValue(final_rating);
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
                                            index++;
                                            if(index<orders.size()){food_name.setText(orders.get(index));}
                                            else {
                                                already_rated_check_ref.setValue(already_rated);
                                                user_points.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        int user_rat=Integer.parseInt(dataSnapshot.child("points").getValue().toString())+points;
                                                        user_points.child("points").setValue(user_rat);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                                thanks_layout.setVisibility(View.VISIBLE);
                                                food_name.setVisibility(View.GONE);
                                                next.setVisibility(View.GONE);
                                                next.setEnabled(false);}
                                        }

                                    }
                                });

                            }
                            else { thanks_layout.setVisibility(View.VISIBLE);
                                    food_text.setText("You have already rated.");
                                    food_name.setVisibility(View.GONE);
                                    next.setVisibility(View.GONE);
                                    next.setEnabled(false);}


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                catch (Exception e){
                    thanks_layout.setVisibility(View.VISIBLE);
                    food_text.setText("You have not ordered anything yet.Please order first");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=1;
                star1.setImageResource(R.drawable.star_onclick);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=2;
                star1.setImageResource(R.drawable.star_onclick);
                star2.setImageResource(R.drawable.star_onclick);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=3;
                star1.setImageResource(R.drawable.star_onclick);
                star2.setImageResource(R.drawable.star_onclick);
                star3.setImageResource(R.drawable.star_onclick);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=4;
                star1.setImageResource(R.drawable.star_onclick);
                star2.setImageResource(R.drawable.star_onclick);
                star3.setImageResource(R.drawable.star_onclick);
                star4.setImageResource(R.drawable.star_onclick);
                star5.setImageResource(R.drawable.star);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rating=5;
                star1.setImageResource(R.drawable.star_onclick);
                star2.setImageResource(R.drawable.star_onclick);
                star3.setImageResource(R.drawable.star_onclick);
                star4.setImageResource(R.drawable.star_onclick);
                star5.setImageResource(R.drawable.star_onclick);
            }
        });
        food_ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(food_rating.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
