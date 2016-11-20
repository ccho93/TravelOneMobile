package com.app.gillime.travelone;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Room extends AppCompatActivity implements Plan.OnFragmentInteractionListener {
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ListView lv;
    private ArrayAdapter<String> aA;
    private String globalKey;
    private Button send;
    private ArrayList<String> list;
    private Button plan;
    private ArrayList<String> hotel;
    private ArrayList<String> location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        location = new ArrayList<>();

        Bundle b = getIntent().getExtras();
        final String name = b.getString("name");
        String userName = b.getString("username");
        String keyReceived = b.getString("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<String>();

        aA = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);


        if (keyReceived == null) {
            System.out.println("in null");
            String key = mDatabase.child("group").child("invitee").push().getKey();
            Group group = new Group(user.getUid(), name, userName);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/group/" + key, group);
            mDatabase.updateChildren(childUpdates);
            globalKey = key;
            list.add(userName);

        } else {
            System.out.println("nont in null");

            String key = mDatabase.child("group").child(keyReceived).child("invitee").push().getKey();
            Join join = new Join(userName);

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/group/" + keyReceived + "/invitee/" + key, join);
            mDatabase.updateChildren(childUpdates);
            globalKey = key;
            list.add(userName);
        }


//        ChildEventListener cE = new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Join join = dataSnapshot.getValue(Join.class);
//                System.out.println(join.getInvitee());
//                if (join.getInvitee() != null) {
//                    list.add(join.getInvitee());
//                    aA.notifyDataSetChanged();
//
//                }
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        mDatabase.addChildEventListener(cE);
        DatabaseReference newref = FirebaseDatabase.getInstance().getReference().child("location");
        newref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot namesnap : dataSnapshot.getChildren()) {

                    System.out.println((String) namesnap.getValue());
                    location.add((String) namesnap.getValue());
                    //     list.add(name);
                    //aA.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        ValueEventListener listadd = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        newref.addValueEventListener(listadd);


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


        lv = (ListView) findViewById(R.id.grouplist);
        lv.setAdapter(aA);
        plan = (Button) findViewById(R.id.plan);
        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment frg = Plan.newInstance(location, globalKey);
                fragmentTransaction.replace(R.id.fragPlace, frg);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Room.this);
                builder.setMessage("Room Key: " + globalKey).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog d = builder.create();
                d.setTitle("Room Key");
                d.show();

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
