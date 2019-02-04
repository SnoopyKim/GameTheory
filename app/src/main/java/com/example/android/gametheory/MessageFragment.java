package com.example.android.gametheory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    FirebaseUser user;
    DatabaseReference msgRef;

    ArrayList<Chat> chatList;
    ChatAdapter chatAdapter;
    RecyclerView chatView;

    EditText etMessage;
    Button btnSend;

    String roomKey;
    boolean isManager;

    public MessageFragment() {
        // Required empty public constructor
        user = FirebaseAuth.getInstance().getCurrentUser();
        isManager = user.getDisplayName().equals("manager");
        msgRef = FirebaseDatabase.getInstance().getReference("message");
        roomKey = isManager ? getArguments().getString("uid") : user.getUid();
        chatList = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        msgRef.child(roomKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // A new comment has been added, add it to the displayed list
                String uid = dataSnapshot.child("uid").getValue().toString();
                String text = dataSnapshot.child("text").getValue().toString();
                String time = dataSnapshot.getKey();

                Chat chat = new Chat(uid, text, time);

                chatList.add(chat);
                chatView.scrollToPosition(chatList.size() - 1);
                chatAdapter.notifyItemInserted(chatList.size() - 1);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        chatView = v.findViewById(R.id.rv_chat);
        chatView.hasFixedSize();
        chatView.setLayoutManager(new LinearLayoutManager(getContext()));

        chatAdapter = new ChatAdapter(chatList, user.getUid(), getContext());
        chatView.setAdapter(chatAdapter);

        etMessage = v.findViewById(R.id.et_message);
        etMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etMessage.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = etMessage.getText().toString();
                    if (text.equals("")) {
                        CustomUtils.displayToast(getContext(), getString(R.string.hint_message));
                    } else {
                        etMessage.setText("");
                        sendMessage(user.getUid(), text);
                    }
                }
                return false;
            }
        });
        /*
        btnSend = v.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                if (text.equals("")) {
                    CustomUtils.displayToast(getContext(), getString(R.string.hint_message));
                } else {
                    etMessage.setText("");
                    sendMessage(user.getUid(), text);
                }
            }
        });
        */
        return v;
    }


    public void sendMessage(String uid, String text) {
        // Time to use by key
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.KOREA);
        String formattedDate = df.format(c.getTime());

        Hashtable<String, String> chat = new Hashtable<String, String>();
        chat.put("uid", user.getUid());
        chat.put("text", text);

        msgRef.child(roomKey).child(formattedDate).setValue(chat);
    }

}
