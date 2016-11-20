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
import android.widget.Button;
import android.widget.EditText;
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
import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.reimaginebanking.api.nessieandroidsdk.models.Customer;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPage extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private FirebaseUser user;
    private TextView hello;
    private String userName;
    private NessieClient client;
    private Customer customer;
    private Button b;
    private Button bank;
    private Account account;

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
        bank = (Button) findViewById(R.id.check);
        bank.setVisibility(View.GONE);
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = (String) dataSnapshot.child("name").getValue();
                userName = user;
                hello.setText("Hello " + user + "! Welcome to TravelOne!");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        b = (Button) findViewById(R.id.connect);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.child("customerID").getValue();
                if (value != null) {
                    b.setVisibility(View.GONE);
                    bank.setVisibility(View.VISIBLE);
                    client.ENTERPRISE.getAccountAsEnterprise("56c66be6a73e492741507b29", new NessieResultsListener() {
                        @Override
                        public void onSuccess(Object result) {
                            account = (Account) result;
                            Toast.makeText(MainPage.this, "Successfully connected to the bank!",
                                    Toast.LENGTH_SHORT).show();
                            Map<String, Object> childUpdates = new HashMap<>();
                            DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("users");
                            childUpdates.put(user.getUid() + "/customerID/", account.getId());
                            d.updateChildren(childUpdates);
                            b.setVisibility(View.GONE);
                            bank.setVisibility(View.VISIBLE);
                            bank.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(MainPage.this);
                                    alert.setMessage("Account Number: " + account.getAccountNumber() + "\nBalance: " + Integer.toString(account.getBalance()) + "\nType: " + account.getType().toString()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    AlertDialog d = alert.create();
                                    d.setTitle("Account Information");
                                    d.show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(NessieError error) {
                            Toast.makeText(MainPage.this, "Failed to connected to the bank!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.addValueEventListener(userListener);
        client = NessieClient.getInstance("b7a6d1efb6182f70d21efb33ec5e1afa");

        hello = (TextView) findViewById(R.id.hello);

        findViewById(R.id.create).setOnClickListener(this);
        findViewById(R.id.join).setOnClickListener(this);
        findViewById(R.id.connect).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.create) {
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
                    intent.putExtra("name", groupString);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Insert Group Name");
            alert.show();


        } else if (i == R.id.join) {
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
                    intent.putExtra("key", groupString);
                    intent.putExtra("username", userName);

                    startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Insert Room Key");
            alert.show();
        } else if (i == R.id.connect) {
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
                    client.ENTERPRISE.getAccountAsEnterprise("56c66be6a73e492741507b29", new NessieResultsListener() {
                        @Override
                        public void onSuccess(Object result) {
                            account = (Account) result;
                            Toast.makeText(MainPage.this, "Successfully connected to the bank!",
                                    Toast.LENGTH_SHORT).show();
                            Map<String, Object> childUpdates = new HashMap<>();
                            DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("users");
                            childUpdates.put(user.getUid() + "/customerID/", account.getId());
                            d.updateChildren(childUpdates);
                            b.setVisibility(View.GONE);
                            bank.setVisibility(View.VISIBLE);
                            bank.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(MainPage.this);
                                    alert.setMessage("Account Number: " + account.getAccountNumber() + "\nBalance: " + Integer.toString(account.getBalance()) + "\nType: " + account.getType().toString()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    AlertDialog d = alert.create();
                                    d.setTitle("Account Information");
                                    d.show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(NessieError error) {
                            Toast.makeText(MainPage.this, "Failed to connected to the bank!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
//                    client.ENTERPRISE.getCustomerAsEnterprise("56c66be5a73e492741507272", new NessieResultsListener() {
//                        @Override
//                        public void onSuccess(Object result) {
//                            customer = (Customer) result;
//                            System.out.println(customer.getFirstName());
//                            Toast.makeText(MainPage.this, "Successfully connected to the bank!",
//                                    Toast.LENGTH_SHORT).show();
//                            Map<String, Object> childUpdates = new HashMap<>();
//                            DatabaseReference d = FirebaseDatabase.getInstance().getReference().child("users");
//                            childUpdates.put(user.getUid() + "/customerID/", "56c66be6a73e492741507b29");
//                            d.updateChildren(childUpdates);
//                            b.setVisibility(View.GONE);
//                            bank.setVisibility(View.VISIBLE);
//
//                        }
//
//                        @Override
//                        public void onFailure(NessieError error) {
//                            Toast.makeText(MainPage.this, "Failed to connected to the bank!",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle("Insert customer ID");
            alert.show();
        } else if (i == R.id.check) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(account.getBalance()).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog d = alert.create();
            d.setTitle("Insert Room Key");
            d.show();

        }


    }
}
