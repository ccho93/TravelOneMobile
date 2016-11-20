package com.app.gillime.travelone;

import android.content.Intent;
import android.net.Uri;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;

public class signin extends AppCompatActivity implements View.OnClickListener, SignUp.OnFragmentInteractionListener {
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView mEmail;
    private TextView mPass;
    private EditText mEmailEdit;
    private EditText mPassEdit;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }

            }
        };
        mEmail = (TextView) findViewById(R.id.email);
        mPass = (TextView) findViewById(R.id.password);
        mEmailEdit = (EditText) findViewById(R.id.emailEdit);
        mPassEdit = (EditText) findViewById(R.id.passEdit);

        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                if (!task.isSuccessful()) {
                    Toast.makeText(signin.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(signin.this, R.string.auth_failed,
                            Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(signin.this, MainPage.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.register) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment frg = SignUp.newInstance();
            fragmentTransaction.replace(R.id.fragment_place,frg);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


//            createAccount(mEmailEdit.getText().toString(), mPassEdit.getText().toString());
        } else {
            signIn(mEmailEdit.getText().toString(), mPassEdit.getText().toString());
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
