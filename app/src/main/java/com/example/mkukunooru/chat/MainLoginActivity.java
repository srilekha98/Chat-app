package com.example.mkukunooru.chat;

import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.Iterator;

public class MainLoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String email, password;
    int signinflag=0;
    UserAccount user;



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
                app.loginemail=email;

                System.out.println("clicked, signinflag= "+signinflag);

                Callapi task=new Callapi();
                task.execute();
                System.out.println("clicked, signinflag= "+signinflag);

            }
        });

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
                    app.UID=acc.getString("displayname");
                    System.out.println(un+pw);
                    if((un.equals(email))&&(pw.equals(password)))
                    {
                        signinflag=1;
                    }

                }
                if(signinflag==1)
                {
                    Intent intentMain = new Intent(MainLoginActivity.this ,
                            ChatScreen.class);
                    MainLoginActivity.this.startActivity(intentMain);


                }
                else
                {
                    Toast.makeText(getApplicationContext(), "invalid username or password ", Toast.LENGTH_LONG).show();
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
