package com.example.mkukunooru.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email, password;
    int signinflag=1;



    int SIGN_IN_REQUEST_CODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
                                     public void onClick(View v) {
                                         Intent intentMain = new Intent(MainLoginActivity.this,
                                                 RegisterActivity.class);
                                         MainLoginActivity.this.startActivity(intentMain);


                                     }
                                 });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                System.out.println("fire base user"+firebaseAuth.getCurrentUser());
                if (user != null) {
                    // User is signed in
                    app.UID=user.getUid();
                    Log.d("MainLoginActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("MainLoginActivity", "onAuthStateChanged:signed_out");
                }
                // ...
            }


        };

        final EditText edit =  (EditText) findViewById(R.id.usernamer);
        final EditText edit1 =  (EditText) findViewById(R.id.passwordr);

        Button go= (Button) findViewById(R.id.go1);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=edit.getText().toString();
                password=edit1.getText().toString();
                System.out.println("clicked, signinflag= "+signinflag);
                signin();
                System.out.println("clicked, signinflag= "+signinflag);
                if(signinflag==1)
                {
                    Intent intentMain = new Intent(MainLoginActivity.this ,
                            ChatScreen.class);
                    MainLoginActivity.this.startActivity(intentMain);


                }
            }
        });

                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    // Start sign in/sign up activity
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .build(),
                            SIGN_IN_REQUEST_CODE
                    );
                } else {
                    // User is already signed in. Therefore, display
                    // a welcome Toast
                    Toast.makeText(getApplicationContext(), "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                            Toast.LENGTH_LONG)
                            .show();

                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if(app.UID.equals("")) {

                            app.UID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            System.out.println("uid is"+app.UID);
                        }
                    //app.UID=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

                    // Load chat room contents
                    Intent intentMain = new Intent(MainLoginActivity.this ,
                            ChatScreen.class);
                    MainLoginActivity.this.startActivity(intentMain);

                }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainLoginActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }
        return true;
    }

    public void signin() {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("login", "signInWithEmail:onComplete:" + task.isSuccessful());
                        System.out.println( "signInWithEmail:onComplete:");
                        signinflag =1;
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("login", "signInWithEmail:failed", task.getException());
                            // Toast.makeText(this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                            System.out.println( "signInWithEmail:failed");
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
      //  mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            //mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
