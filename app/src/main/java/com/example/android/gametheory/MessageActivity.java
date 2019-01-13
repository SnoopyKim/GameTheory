package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = ".MessageActivity";

    FirebaseUser user;
    DatabaseReference msgRef;

    boolean isManager;
    String roomKey;

    RecyclerView chatView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Chat> chatList = new ArrayList<>();
    ChatAdapter chatAdapter;

    EditText etMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        isManager = user.getDisplayName().equals("manager");

        msgRef = FirebaseDatabase.getInstance().getReference("message");
        roomKey = isManager ? getIntent().getStringExtra("uid") : user.getUid();
        msgRef.child(roomKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // A new comment has been added, add it to the displayed list
                String uid = dataSnapshot.child("uid").getValue().toString();
                String text = dataSnapshot.child("text").getValue().toString();

                Chat chat = new Chat(uid, text);

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

        chatView = findViewById(R.id.rv_player);
        chatView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        chatView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter(chatList, user.getUid(), this);
        chatView.setAdapter(chatAdapter);

        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etMessage.getText().toString();
                if (text.equals("")) {
                    CustomUtils.displayToast(getApplicationContext(), getString(R.string.hint_message));
                } else {
                    etMessage.setText("");
                    sendMessage(user.getUid(), text);
                }
            }
        });

        String toorbar_title = isManager ? getIntent().getStringExtra("name") : "관리자";
        toolbar.setTitle(toorbar_title);
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
