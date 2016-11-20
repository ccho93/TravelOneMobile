package com.app.gillime.travelone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Room extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private ArrayList<String> list;
    private ListView lv;

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

        Bundle b = getIntent().getExtras();
        String name = b.getString("name");
        String userName = b.getString("username");
        String keyReceived = b.getString("key");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<String>();


        if(keyReceived == null){
            String key = mDatabase.child("group").push().getKey();
            Group group = new Group(user.getUid(), name, userName);
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/group/" + key, group);
            mDatabase.updateChildren(childUpdates);
            list.add(userName);

        }
        else{
            mDatabase.child("group").child(keyReceived).push();
            Join join = new Join(userName);
            DatabaseReference newD = mDatabase.child(keyReceived).push();
            newD.setValue(join);
//            Map<String, Object> childUpdates = new HashMap<>();
//            childUpdates.put("/group/"+keyReceived,join);
//            mDatabase.updateChildren(childUpdates);
            list.add(userName);
        }



        ValueEventListener listadd = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot namesnap : dataSnapshot.getChildren()) {
                    String name = (String) namesnap.child("name").getValue();
                    list.add(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(listadd);

        lv = (ListView) findViewById(R.id.grouplist);
        ArrayAdapter<String> aA = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        lv.setAdapter(aA);




    }

}
