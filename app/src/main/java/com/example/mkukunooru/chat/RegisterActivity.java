package com.example.mkukunooru.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email, pw,mName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText edit =  (EditText) findViewById(R.id.username);
        final EditText edit1 =  (EditText) findViewById(R.id.password);
        final EditText edit2 =  (EditText) findViewById(R.id.displayname);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("MainLoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("MainLoginActivity", "onAuthStateChanged:signed_out");
                }
                // ...
            }


        };

        Button go= (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=edit.getText().toString();
                pw=edit1.getText().toString();
                mName=edit2.getText().toString();
                createuser(email,pw);
                app.UID=mName;
                Intent intentMain = new Intent(RegisterActivity.this ,
                        MainLoginActivity.class);
                RegisterActivity.this.startActivity(intentMain);
            }
        });

        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(RegisterActivity.this ,
                        MainLoginActivity.class);
                RegisterActivity.this.startActivity(intentMain);
            }

        });

    }
    public void createuser(String email,String pw) {
        mAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d( "MainLoginActivity" , "createUserWithEmail:onComplete:" + task.isSuccessful());
                        System.out.println( "createUserWithEmail:onComplete:");
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            FirebaseAuthException e = (FirebaseAuthException)task.getException();
                            Log.d( "MainLoginActivity" , "createUserWithEmail:notComplete:" );
                            System.out.println( "createUserWithEmail:notComplete:"+e.getMessage());
                        }
                        // ...
                    }
                });

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!=null){
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(mName).build();
                    user.updateProfile(profileUpdates);
                }

            }
        };


    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
