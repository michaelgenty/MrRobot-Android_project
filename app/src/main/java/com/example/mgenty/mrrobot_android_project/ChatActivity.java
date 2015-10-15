package com.example.mgenty.mrrobot_android_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mgenty.mrrobot_android_project.chat.ChatFragment;
import com.example.mgenty.mrrobot_android_project.chat.ReceivedMessageFragment;
import com.example.mgenty.mrrobot_android_project.chat.SendMessageFragment;
import com.example.mgenty.mrrobot_android_project.communication.CommunicationActivity;
import com.example.mgenty.mrrobot_android_project.user.User;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements ChatFragment.ChatListener, SendMessageFragment.SendListener, ReceivedMessageFragment.ReceivedListener{
    private static final String TAG = "ChatActivity";
    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase("https://androidmrrobot.firebaseio.com/");

        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.chatContainer, new ChatFragment())
                    .commit();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuHomeConnectedUserItem) {
            //login clicked
            Log.d(TAG, "onUserScreenClicked in Activity");



            return true;
        } else if (item.getItemId() == R.id.menuHomeConnectedChatItem) {
            //register clicked
            Log.d(TAG, "onChatClicked in Activity");


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_connected, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void onAccessSendMessageClicked() {
        Log.d(TAG, "onAccessMessageClicked in Activity");

        //launch the chatActivity
        Intent intent = new Intent(ChatActivity.this, CommunicationActivity.class);
        startActivity(intent);

        //old chat method
     /*   getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.chatContainer, new SendMessageFragment())
                .commit();*/

    }

    public void onSendMessageClicked(String message) {
        Log.d(TAG, "onSendMessageClicked in Activity");

        Map<String, String> messages = new HashMap<String, String>();

        //retrieve the current User
        User user = HomeActivity.getUser();

        messages.put("sender", user.getName());
        messages.put("receiver", "lui");
        messages.put("message", message);
        mFirebaseRef.child("messages").push().setValue(messages);

    }

}
