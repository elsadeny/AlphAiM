package com.sadeny.alphaim;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ChildEventListener;
import com.firebase.client.FirebaseError;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    //LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    DataClass dataClass;
    Firebase reference1,reference2;
    MessageAdapter messageAdapter;
    ListView messageView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //layout =(LinearLayout)findViewById(R.id.layout1);

        //getting string from prev act
        String value =getIntent().getStringExtra("CHAT_WITH");
        getSupportActionBar().setTitle(value);

        sendButton=(ImageView) findViewById(R.id.sendButton);
        messageArea=(EditText)findViewById(R.id.messageArea);
        //scrollView=(ScrollView)findViewById(R.id.scrollView);
        messageView = (ListView) findViewById(R.id.messages_view);
        messageAdapter = new MessageAdapter(this);
        messageView.setAdapter(messageAdapter);
        dataClass = new DataClass(this);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://alphaim-d0e00.firebaseio.com/messages/"+UserDetails.username+"_"+UserDetails.chatWith);
        reference2 = new Firebase("https://alphaim-d0e00.firebaseio.com/messages/"+UserDetails.chatWith+"_"+UserDetails.username);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String messageText = messageArea.getText().toString();
                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
             if(!messageText.equals(" "))
             {
                 Map<String,String> map = new HashMap<String,String>();
                 map.put("message",messageText);
                 map.put("user",UserDetails.username);
                 map.put("date",mydate);
                 reference1.push().setValue(map);
                 reference2.push().setValue(map);
                 messageArea.getText().clear();
             }

            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map =dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String mytime =map.get("date").toString();


                boolean currentUser = userName.equals(UserDetails.username);
                Message msg = new Message(message,userName,currentUser,mytime);
                messageAdapter.add(msg);
                messageView.setSelection(messageView.getCount()-1);
                //

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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



}
