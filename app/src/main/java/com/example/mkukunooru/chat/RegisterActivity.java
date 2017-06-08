package com.example.mkukunooru.chat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email, pw,mName;
    UserAccount user;

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

                app.loginemail=email;
                if(isValidEmail(email)) {
                    if (pw.length() >= 6) {
                        new Callapi().execute();
                        if (user == null) {
                            app.UID = mName;
                            UserAccount newacc = new UserAccount();
                            newacc.setDisplayname(mName);
                            newacc.setPassword(pw);
                            newacc.setUsername(email);
                            FirebaseDatabase.getInstance().getReference().child("accounts/").push().setValue(newacc);
                            // app.acc.add(newacc);
                            Toast.makeText(getApplicationContext(), "Account created ", Toast.LENGTH_LONG).show();

                            Intent intentMain = new Intent(RegisterActivity.this,
                                    MainLoginActivity.class);
                            RegisterActivity.this.startActivity(intentMain);
                        } else {
                            Toast.makeText(getApplicationContext(), "Account already exists ", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Enter a bigger password", Toast.LENGTH_LONG).show();
                    }
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "Invalid email id", Toast.LENGTH_LONG).show();
                }
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

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
    private class Callapi extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... data) {
            String s= null;
            try {
                s=  api.startService(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return s;
        }

        protected void onPostExecute(String result) {
            System.out.println("Downloaded " + result + " bytes");
            try {
                JSONObject obj = new JSONObject(result);
                Iterator key=obj.keys();
                while(key.hasNext())
                {
                    String currentDynamicKey = (String)key.next();
                    JSONObject acc = obj.getJSONObject(currentDynamicKey);
                    String un=acc.getString("username");
                    String pw=acc.getString("password");
                    if((un.equals(email))&&(pw.equals(pw)))
                    {
                       user.setUsername(email);
                        user.setPassword(pw);
                    }

                }

            }
            catch(Exception e)
            {
                System.out.println("gone");
                e.printStackTrace();
            }
            System.out.println(result);

        }
    }

}
