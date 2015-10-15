package com.example.mgenty.mrrobot_android_project.communication;


import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mgenty.mrrobot_android_project.R;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.FirebaseRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {
    private static final String TAG = "ListFragment";
    private ListListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mDatas = new ArrayList<>();

    private Firebase mFirebaseRef;
    final String name = "Android User";

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ListListener) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString() + "must implement ListFragment.ListListener");
        }

    }

    @Override
    public void onDestroyView(){
        mListener = null;

        super.onDestroyView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mFirebaseRef = new Firebase("https://androidmrrobot.firebaseio.com/").child("chat");

        final EditText inputText = (EditText) view.findViewById(R.id.messageInput);

        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = new Chat(name, inputText.getText().toString());
                mFirebaseRef.push().setValue(chat, new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        if (firebaseError != null) {
                            Log.e("FirebaseUI.chat", firebaseError.toString());
                        }
                    }
                });
                inputText.setText("");
            }
        });



        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadTexts();

        return view;
    }


    public void loadTexts(){

        FirebaseRecyclerViewAdapter<Chat, ChatHolder> adapter = new FirebaseRecyclerViewAdapter<Chat, ChatHolder>(Chat.class, android.R.layout.two_line_list_item, ChatHolder.class, mFirebaseRef) {
            @Override
            public void populateViewHolder(ChatHolder chatView, Chat chat) {
                chatView.textView.setText(chat.getMessage());
                chatView.textView.setPadding(10, 0, 10, 0);
                chatView.nameView.setText(chat.getAuthor());
                chatView.nameView.setPadding(10, 0, 10, 15);
                if (chat.getAuthor().equals(name)) {
                    chatView.textView.setGravity(Gravity.END);
                    chatView.nameView.setGravity(Gravity.END);
                    chatView.nameView.setTextColor(Color.parseColor("#8BC34A"));
                } else {
                    chatView.nameView.setTextColor(Color.parseColor("#00BCD4"));
                }
            }
        };
        mRecyclerView.setAdapter(adapter);

    }



    public interface ListListener {

    }


}