package com.deeefoo.myappl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;

import java.util.ArrayList;
import java.util.Calendar;


public class searc_frag extends Fragment {
    private String[] items={"SELECT FOOD ITEM","Burger","Juice","Roll","Fries","Desi","Chinese","Sandwich","One Bite Samosa"};

    private Button canteen1,canteen2,canteen3,gobtn,opne_canteen_list,close_canteen_list;
    private ListView canteen_list;
    private ConstraintLayout canteen_list_layout;
    private TextView rank1,rank2,rank3;
    private DatabaseReference NafeesRef,KfcRef,MCRef,NCHRef,OBSRef,SFFRef,SRef,SSFRef,TJCRef,Canteens;
    protected static  Spinner spinner;
    private ArrayList<String> CanteenNames;
    private ArrayList<Integer> raw;
    private ArrayList<String> all_canteens;
    static  String Clicked_Button_TextHolder="null";
    MainActivity ma =new MainActivity();
    FragmentManager frag = getFragmentManager();
    // fraggg =  frag.findFragmentById(R.id.maps);
    private LinearLayout map_canteen;
    private Button close_canteen;
    private ImageView hamburger;
    private TextView cateen1points,cateen2points,cateen3points;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_searc_frag, container, false);
        //        ZonedDateTime.now //latest method //
//        Toast.makeText(getContext(), time_check, Toast.LENGTH_SHORT).show();
        cateen1points=view.findViewById(R.id.canteen1points);
        cateen2points=view.findViewById(R.id.canteen2points);
        cateen3points=view.findViewById(R.id.canteen3points);
        canteen1=(Button) view.findViewById(R.id.canteen2);
        canteen2=(Button) view.findViewById(R.id.canteen1);
        canteen3=(Button) view.findViewById(R.id.canteen3);
        close_canteen=view.findViewById(R.id.close_canteen);
        close_canteen_list=view.findViewById(R.id.close_canteen_list);
        opne_canteen_list=view.findViewById(R.id.canteen_list_open);
        canteen_list=view.findViewById(R.id.canteen_list);
        canteen_list_layout=view.findViewById(R.id.canteen_list_layout);
        hamburger=view.findViewById(R.id.hamburger);
        rank1=view.findViewById(R.id.rank1);
        rank2=view.findViewById(R.id.rank2);
        rank3=view.findViewById(R.id.rank3);
        rank1.setVisibility(View.GONE);rank2.setVisibility(View.GONE);rank3.setVisibility(View.GONE);
        canteen1.setVisibility(View.GONE);
        canteen2.setVisibility(View.GONE);
        canteen3.setVisibility(View.GONE);
        spinner= (Spinner) view.findViewById(R.id.search);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_dropdown_item_1line,items);
        spinner.setAdapter(adapter);
        raw=new ArrayList<Integer>();
        CanteenNames=new ArrayList<String>();
        try{
            NafeesRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Nafees Canteen");
            KfcRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Karachi Food Center");
            MCRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Mass Communication Canteen");
            NCHRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Nazir Chat House");
            OBSRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Qasim Samosa Shop");
            SFFRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Shaheen Fast Food");
            SRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Sufi Canteen");
            SSFRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Student Star Food");
            TJCRef= FirebaseDatabase.getInstance().getReference().child("Canteens").child("Tahir Juice Center");

        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }


//From Here
        spinnermethod();
        final ArrayList<String> result= new ArrayList();
        gobtn = (Button) view.findViewById(R.id.gobtn);
        map_canteen=view.findViewById(R.id.map_canteen);
        gobtn.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> result= new ArrayList<>(10);
            ArrayList<Integer> points = new ArrayList<>();
            @Override
            public void onClick(View view) {
                if(Internet_Connection.instance(getActivity()).isonline()) {

                }
                else{
                    Toast.makeText(getActivity(), "Internet not connected", Toast.LENGTH_SHORT).show();
                }

                    result.clear();
                    points.clear();
                    if(raw.isEmpty())
                    {
                        Toast.makeText(getContext(), "Make sure your intent is connected and suitable canteens are present in your radius.", Toast.LENGTH_SHORT).show();

                    }
                    else if (!raw.isEmpty()) {
                        Sorting sort = new Sorting(raw, CanteenNames);
                        result.addAll(sort.getCanlist());
                        points.addAll(sort.getpoints());
                       try {
                           if (result.get(0) != null) {
                               canteen1.setText(result.get(0));
                               canteen1.setVisibility(View.VISIBLE);
                               rank1.setVisibility(View.VISIBLE);
                               cateen1points.setText(String.valueOf(points.get(1)+" pts"));
                           }//+"    "+points.get(0));

                           if (result.get(1) != null) {
//                               Toast.makeText(getActivity(), "2nd button", Toast.LENGTH_SHORT).show();
                               canteen2.setText(result.get(1));
                               canteen2.setVisibility(View.VISIBLE);
                               rank2.setVisibility(View.VISIBLE);
                               cateen2points.setText(String.valueOf(points.get(0)+" pts"));
                           }//+"    "+points.get(1));
                           if (result.get(2) != null) {
                               canteen3.setText(result.get(2));
                               canteen3.setVisibility(View.VISIBLE);
                               rank3.setVisibility(View.VISIBLE);
                               cateen3points.setText(String.valueOf(points.get(2)+" pts"));
                           }    //+"    "+points.get(2));
                           result.clear();
                           points.clear();
                           CanteenNames.clear();
                           raw.clear();
                       }

                       catch(IndexOutOfBoundsException e)
                       {
//                           Toast.makeText(getActivity(), "You have selected less than 3 canteens", Toast.LENGTH_SHORT).show();
                       }
                        map_canteen.setVisibility(View.VISIBLE);


                    }
            }
        });
        close_canteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map_canteen.setVisibility(View.INVISIBLE);
            }
        });
        canteen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clicked_Button_TextHolder=canteen2.getText().toString();
                String top=canteen2.getText().toString();
                Intent intent = new Intent(getContext(),MenuActivity.class);
                intent.putExtra("CanName",top);
                startActivity(intent);
                getActivity().finish();
            }
        });
        canteen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clicked_Button_TextHolder=canteen1.getText().toString();
                String top=canteen1.getText().toString();
                Intent intent = new Intent(getContext(),MenuActivity.class);
                intent.putExtra("CanName",top);
                startActivity(intent);
                getActivity().finish();
            }
        });
        canteen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Clicked_Button_TextHolder=canteen3.getText().toString();
                String top=canteen3.getText().toString();
                Intent intent = new Intent(getContext(),MenuActivity.class);
                intent.putExtra("CanName",top);
                startActivity(intent);
                getActivity().finish();
            }
        });
        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.dlayout.isDrawerOpen(GravityCompat.START)){MainActivity.dlayout.openDrawer(GravityCompat.START);}
                else MainActivity.dlayout.closeDrawer(GravityCompat.START);
            }
        });
        //CANTEEN LIST
        Canteens=FirebaseDatabase.getInstance().getReference("Canteens");
        Canteens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                all_canteens=new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    all_canteens.add(postSnapshot.getKey().toString());
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getContext(),
                            android.R.layout.simple_list_item_1,
                            all_canteens );
                    canteen_list.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        canteen_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Clicked_Button_TextHolder=all_canteens.get(i).toString();
                String top=all_canteens.get(i).toString();
                Intent intent = new Intent(getContext(),MenuActivity.class);
                intent.putExtra("CanName",top);
                startActivity(intent);
                getActivity().finish();
            }
        });
        opne_canteen_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canteen_list_layout.setVisibility(View.VISIBLE);
            }
        });
        close_canteen_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canteen_list_layout.setVisibility(View.GONE);
            }
        });
        return view;
    }


    public void spinnermethod()
    {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // ma.onCameraIdle();
                   canteen1.setVisibility(View.GONE);
                   canteen2.setVisibility(View.GONE);
                   canteen3.setVisibility(View.GONE);
                rank1.setVisibility(View.GONE);rank2.setVisibility(View.GONE);rank3.setVisibility(View.GONE);
                   canteen1.setText("");
                   canteen2.setText("");
                   canteen3.setText("");
                       raw.clear();
                   CanteenNames.clear();
                    if(spinner.getSelectedItem().toString().equals("SELECT FOOD ITEM"))
                    {
                        //Toast.makeText(getActivity(), "First select the food item.", Toast.LENGTH_SHORT).show();
                        canteen1.setVisibility(View.GONE);
                        canteen2.setVisibility(View.GONE);
                        canteen3.setVisibility(View.GONE);
                        rank1.setVisibility(View.GONE);rank2.setVisibility(View.GONE);rank3.setVisibility(View.GONE);
                    }

                   if (spinner.getSelectedItem().toString().equals("Burger")) {
                       if(MainActivity.checkKFC==1){
                       KfcRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Burger Points").getValue().toString()));
                               CanteenNames.add("Karachi Food Center");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                               Toast.makeText(getActivity(), "canceled", Toast.LENGTH_SHORT).show();
                           }
                       });}
                       if(MainActivity.checkChemisty==1){
                       NafeesRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Burger Points").getValue().toString()));
                               CanteenNames.add("Nafees Canteen");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                       if(MainActivity.checkNazirChat==1){
                       NCHRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Burger Points").getValue().toString()));
                               CanteenNames.add("Nazir Chat House");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                       if(MainActivity.checkStudentStar==1){

                           SSFRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Burger Points").getValue().toString()));
                                   CanteenNames.add("Student Star Food");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });}
                       if(MainActivity.checkSFF==1){
                       SFFRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Burger Points").getValue().toString()));
                               CanteenNames.add("Shaheen Fast Food");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                   }
                   if (spinner.getSelectedItem().toString().equals("Juice")) {
                       if (MainActivity.checkKFC == 1) {
                           KfcRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Juice Points").getValue().toString()));
                                   CanteenNames.add("Karachi Food Center");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                       if (MainActivity.checkMassCom == 1) {
                           MCRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Juice Points").getValue().toString()));
                                   CanteenNames.add("Mass Communication Canteen");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                       if (MainActivity.checkChemisty == 1) {
                           NafeesRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Juice Points").getValue().toString()));
                                   CanteenNames.add("Nafees Canteen");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                       if (MainActivity.checkSFF == 1) {
                           SFFRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Juice Points").getValue().toString()));
                                   CanteenNames.add("Shaheen Fast Food");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                       if (MainActivity.checkTahir == 1) {

                           TJCRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Juice Points").getValue().toString()));
                                   CanteenNames.add("Tahir Juice Center");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                   }
                   if (spinner.getSelectedItem().toString().equals("Roll")) {
                       if (MainActivity.checkKFC == 1) {
                           KfcRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Roll Points").getValue().toString()));
                                   CanteenNames.add("Karachi Food Center");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                       if (MainActivity.checkMassCom == 1) {
                           MCRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Roll Points").getValue().toString()));
                                   CanteenNames.add("Mass Communication Canteen");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                       if (MainActivity.checkChemisty == 1) {
                           NafeesRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Roll Points").getValue().toString()));
                                   CanteenNames.add("Nafees Canteen");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                       }
                       if (MainActivity.checkNazirChat == 1) {
                           NCHRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Roll Points").getValue().toString()));
                                   CanteenNames.add("Nazir Chat House");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                           });
                           if (MainActivity.checkSFF == 1) {

                               SSFRef.addValueEventListener(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       raw.add(Integer.parseInt(dataSnapshot.child("Roll Points").getValue().toString()));
                                       CanteenNames.add("Student Star Food");
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError databaseError) {
                                   }
                               });
                           }
                       }
                   }
                   if (spinner.getSelectedItem().toString().equals("Fries")) {
                       if(MainActivity.checkMassCom==1){
                       MCRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Fries Points").getValue().toString()));
                               CanteenNames.add("Mass Communication Canteen");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                       if(MainActivity.checkNazirChat==1){
                       NCHRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Fries Points").getValue().toString()));
                               CanteenNames.add("Nazir Chat House");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                       if(MainActivity.checkStudentStar==1){

                           SSFRef.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                   raw.add(Integer.parseInt(dataSnapshot.child("Fries Points").getValue().toString()));
                                   CanteenNames.add("Student Star Food");
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                               }
                       });}
                       if(MainActivity.checkSFF==1){
                       SFFRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Fries Points").getValue().toString()));
                               CanteenNames.add("Shaheen Fast Food");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                   }
                   if (spinner.getSelectedItem().toString().equals("Desi")) {
                      if(MainActivity.checkKFC==1){
                       KfcRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Desi Points").getValue().toString()));
                               CanteenNames.add("Karachi Food Center");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                      if(MainActivity.checkChemisty==1){
                        NafeesRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Desi Points").getValue().toString()));
                               CanteenNames.add("Nafees Canteen");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                      if(MainActivity.checkSFF==1){
                        SFFRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Desi Points").getValue().toString()));
                               CanteenNames.add("Shaheen Fast Food");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                      if(MainActivity.checkStudentStar==1){
                        SSFRef.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                               raw.add(Integer.parseInt(dataSnapshot.child("Desi Points").getValue().toString()));
                               CanteenNames.add("Student Star Food");
                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {
                           }
                       });}
                      if(MainActivity.checkSufi==1)
                      {
                          SRef.addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                  raw.add(Integer.parseInt((dataSnapshot.child("Desi Points").getValue().toString())));
                                  CanteenNames.add("Sufi Canteen");
                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError) {

                              }
                          });
                      }
                   }
                if (spinner.getSelectedItem().toString().equals("Chinese")) {
                    if(MainActivity.checkKFC==1){
                    KfcRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            raw.add(Integer.parseInt(dataSnapshot.child("Chinese Points").getValue().toString()));
                            CanteenNames.add("Karachi Food Center");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });}
                    if(MainActivity.checkSFF==1){
                    SFFRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            raw.add(Integer.parseInt(dataSnapshot.child("Chinese Points").getValue().toString()));
                            CanteenNames.add("Shaheen Fast Food");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });}
                }
                if (spinner.getSelectedItem().toString().equals("Sandwich")) {
                    if(MainActivity.checkKFC==1){
                    KfcRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            raw.add(Integer.parseInt(dataSnapshot.child("Sandwich Points").getValue().toString()));
                            CanteenNames.add("Karachi Food Center");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });}
                    if(MainActivity.checkSFF==1){
                    SFFRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            raw.add(Integer.parseInt(dataSnapshot.child("Sandwich Points").getValue().toString()));
                            CanteenNames.add("Shaheen Fast Food");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });}
                    if(MainActivity.checkStudentStar==1){
                    SSFRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            raw.add(Integer.parseInt(dataSnapshot.child("Sandwich Points").getValue().toString()));
                            CanteenNames.add("Student Star Food");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });}
                }
                if (spinner.getSelectedItem().toString().equals("One Bite Samosa")) {
                    if(MainActivity.checkQasimSamosa==1){
                    OBSRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            raw.add(Integer.parseInt(dataSnapshot.child("Samosa Points").getValue().toString()));
                            CanteenNames.add("Qasim Samosa Shop");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });}
                }
               }




            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }
    }

