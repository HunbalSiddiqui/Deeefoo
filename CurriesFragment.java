package com.deeefoo.myappl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CurriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CurriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CurriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CurriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurriesFragment newInstance(String param1, String param2) {
        CurriesFragment fragment = new CurriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    DatabaseReference earningdbref;

    ListView currieslist;
    DatabaseReference dref1;
    searc_frag sf=new searc_frag();
    ArrayList<Integer> selected_item_price;
    ArrayList<String> result;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_curries, container, false);
        currieslist=(ListView) view.findViewById(R.id.currieslist);
        earningdbref=FirebaseDatabase.getInstance().getReference("RidersEarningDatabase");
        selected_item_price=new ArrayList<Integer>();
        if(searc_frag.Clicked_Button_TextHolder.equalsIgnoreCase("Sufi Canteen"))
        {
            try {
                dref1 = FirebaseDatabase.getInstance().getReference().child("Sufi Order").child("Curries");
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(), "Curries Frag Sufi Error", Toast.LENGTH_SHORT).show();
            }
            dref1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    result = new ArrayList<>();
                    selected_item_price.clear();
                    for(final DataSnapshot postSnapshot : dataSnapshot.getChildren())
                    {
                        earningdbref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                result.add(postSnapshot.getKey());
                                selected_item_price.add(Integer.parseInt(postSnapshot.getValue().toString())+Integer.parseInt(dataSnapshot.child("increase").getValue().toString()));
                                currieslist.setAdapter(new Pagal_List_Adapter(getContext(),result,selected_item_price));
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        currieslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(!selected_item_price.get(i).equals(0)) {
                    Chart_Static_Variable.chart_count++;
                    MenuActivity.menu.setText(String.valueOf("Number Of Selected Items" + "  " + Chart_Static_Variable.chart_count));
                    Chart_Static_Variable.selected_menu_list.add(result.get(i));//YHAN SE CANTEEN NAME OR PRICE HATA DI HE
                    Chart_Static_Variable.canteen_list.add(MenuActivity.Actionbar_Canteen_Name); //CANTEEN KI LIST BNAYI HE CHART STATIC CLASS MEIN
                    Chart_Static_Variable.total_price_list.add(selected_item_price.get(i)); //YE TUMHARA KAM KOI CHANGE NHI HE
                    MenuActivity.price_check = MenuActivity.price_check + selected_item_price.get(i);
                    earningdbref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Chart_Static_Variable.add_discount_func(selected_item_price.get(i)-Integer.parseInt(dataSnapshot.child("Canteen Discount").getValue().toString()));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(), "This item is not currently available.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
