package com.example.mkukunooru.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatScreen extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener {
     EditText input;
    EmojiconTextView txt;
    ArrayList<ChatMessage> results = new ArrayList<>();
    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        results.clear();

        System.out.println("entered chat screen");
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fabcs);
        input = (EditText) findViewById(R.id.input);
//        txt=(EmojiconTextView)findViewById(R.id.txt);
//        input.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            txt.setText(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        setEmojiconFragment(true);

        /*
        xml emojis
               <com.rockerhieu.emojicon.EmojiconTextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_toLeftOf="@id/fabcs"
            android:id="@+id/txt"
            android:layout_alignParentBottom="true"
            android:layout_alignParentStart="true"
            android:layout_alignParentLeft="true"/>

        <com.rockerhieu.emojicon.EmojiconEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Input"
            android:id="@+id/input"
            android:layout_below="@+id/txt"
            android:layout_alignParentRight="true"
            android:layout_alignParentEnd="true"/>

        <FrameLayout
            android:id="@+id/emojicons"
            android:layout_width="match_parent"
            android:layout_height="fill_parent"
            android:layout_below="@+id/input"
            android:layout_alignParentRight="true"
            android:layout_alignParentEnd="true"
            android:layout_marginTop="38dp"/>

         */


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content = input.getText().toString().trim();
                ChatMessage newMessage = new ChatMessage();
                newMessage.setMessageText(content);
                newMessage.setMessageUser(app.UID) ;
                newMessage.setMessageTime(String.valueOf(System.currentTimeMillis()));
                FirebaseDatabase.getInstance().getReference().child("message/" ).push().setValue(newMessage);
//                FirebaseDatabase.getInstance()
//                        .getReference()
//                        .push()
//                        .setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
//                        );

                // Clear the input
                input.setText("");
            }
        });


        displayChatMessages();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
//        menu.findItem(R.menu.main_menu).setTitle("Chat");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ChatScreen.this,
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

    private void displayChatMessages() {
        final ListView lv1 = (ListView)findViewById(R.id.list_of_messages);
      // System.out.println("long thing "+ FirebaseDatabase.getInstance().getReference());
        String url="https://chat-4ab21.firebaseio.com";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("message/" );
        System.out.println("reference is"+ FirebaseDatabase.getInstance().getReference());

        ref.addChildEventListener(new ChildEventListener() {
                                      @Override
                                      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                         // ChatMessage msg= dataSnapshot.getValue(ChatMessage.class);
                                          if (dataSnapshot.getValue() != null) {

                                              HashMap mapMessage = (HashMap) dataSnapshot.getValue();
                                              ChatMessage newMessage = new ChatMessage();
                                             // newMessage.idSender = (String) mapMessage.get("idSender");
                                              newMessage.setMessageUser((String) mapMessage.get("messageUser")) ;
                                              newMessage.setMessageText((String) mapMessage.get("messageText"));
                                              newMessage.setMessageTime((String) mapMessage.get("messageTime"));
                                              results.add(newMessage);
                                              System.out.println((String) mapMessage.get("messageUser")+"         "+mapMessage.get("messageText")+"      "+mapMessage.get("messageTime"));
                                             if(results!=null) {
                                                 adapter = new CustomListAdapter(getApplicationContext(), results);
                                                 lv1.setAdapter(adapter);
                                                 adapter.notifyDataSetChanged();
                                                 lv1.setSelection(lv1.getCount()-1);
                                             }
                                              //linearLayoutManager.scrollToPosition(consersation.getListMessageData().size() - 1);
                                          }
                                          System.out.println("msg"+dataSnapshot);

                                      }

                                      @Override
                                      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                      }

                                      @Override
                                      public void onChildRemoved(DataSnapshot dataSnapshot) {

                                      }

                                      @Override
                                      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                      }

                                      @Override
                                      public void onCancelled(DatabaseError databaseError) {

                                      }
                                  });

    }


    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(input, emojicon);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {

//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
//                .commit();
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(input);
    }
}
