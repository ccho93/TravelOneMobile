package com.app.gillime.travelone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Plan.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Plan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Plan extends Fragment implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "location";
    private Spinner spinner;
    private Spinner hotelSpin;
    private TextView price;
    private EditText priceEdit;
    private Button request;


    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1;
    private ArrayAdapter<String> dA;
    private ArrayAdapter<String> hotelAd;
    private ArrayList<String> hotel;

    private OnFragmentInteractionListener mListener;
    private String selectedLocation;
    private String selectedHotel;
    private String globalKey;

    public Plan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1    Parameter 1.
     * @param globalKey
     * @return A new instance of fragment Plan.
     */
    // TODO: Rename and change types and number of parameters
    public static Plan newInstance(ArrayList<String> param1, String globalKey) {
        Plan fragment = new Plan();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putString("globalKey", globalKey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            globalKey = getArguments().getString("globalKey");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        hotel = new ArrayList<>();

        DatabaseReference asdf = FirebaseDatabase.getInstance().getReference().child("hotel");
        System.out.println(mParam1.get(0).toString());

//        asdf.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot namesnap : dataSnapshot.getChildren()) {
//
//                    System.out.println((String) namesnap.getValue());
//
//                    hotel.add((String) namesnap.getValue());
//                    //     list.add(name);
//                    //aA.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        System.out.println(hotel.get(0).toString());
        ValueEventListener list1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot namesnap : dataSnapshot.getChildren()) {

                    System.out.println((String) namesnap.getValue());

                    hotel.add((String) namesnap.getValue());
                    //     list.add(name);
                    //aA.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        asdf.addValueEventListener(list1);
        spinner = (Spinner) view.findViewById(R.id.location);
        hotelSpin = (Spinner) view.findViewById(R.id.hotel);
        price = (TextView) view.findViewById(R.id.price);
        priceEdit = (EditText) view.findViewById(R.id.priceEdit);
        request = (Button) view.findViewById(R.id.request);
        request.setOnClickListener(this);
        hotelAd = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, hotel);
        hotelAd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hotelSpin.setAdapter(hotelAd);
        hotelSpin.setOnItemSelectedListener(this);

        dA = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mParam1);
        dA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dA);
        spinner.setOnItemSelectedListener(this);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spin = (Spinner) adapterView;
        if (spin.getId() == R.id.location) {
            selectedLocation = (String) adapterView.getItemAtPosition(i);

        } else if (spin.getId() == R.id.hotel) {
            selectedHotel = (String) adapterView.getItemAtPosition(i);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.request) {
            Request r = new Request(selectedHotel, selectedLocation, Integer.parseInt(priceEdit.getText().toString()));
            Map<String, Object> childUpdates = new HashMap<>();

            DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("group").child(globalKey);
            childUpdates.put("/request/", r);
            d.updateChildren(childUpdates);

        }
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
