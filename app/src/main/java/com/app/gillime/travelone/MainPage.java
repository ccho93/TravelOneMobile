package com.app.gillime.travelone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainPage extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private TextView hello;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getName();
                hello.setText("Hello " + user.getName() + "! Welcome to TravelOne!");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(userListener);
        hello = (TextView)findViewById(R.id.hello);
        findViewById(R.id.create).setOnClickListener(this);
        findViewById(R.id.join).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if(i == R.id.create){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText group = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            group.setLayoutParams(lp);
            builder.setView(group);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String groupString = group.getText().toString();
                    System.out.println(groupString);
                    Intent intent = new Intent(MainPage.this, Room.class);
//                    Bundle b = new Bundle();
//                    b.putString("name",groupString);
                    intent.putExtra("name",groupString);
                    intent.putExtra("username",userName);
                    startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Insert Group Name");
            alert.show();



        }else if(i == R.id.join){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText group = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            group.setLayoutParams(lp);
            builder.setView(group);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String groupString = group.getText().toString();
                    System.out.println(groupString);
                    Intent intent = new Intent(MainPage.this, Room.class);
//                    Bundle b = new Bundle();
//                    b.putString("name",groupString);
                    intent.putExtra("key",groupString);
                    intent.putExtra("username",userName);

                    startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Insert Room Key");
            alert.show();
        }

    }
}
