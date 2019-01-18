package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CheckVoteActivity extends AppCompatActivity {

    DatabaseReference voteRef;

    ListView voteView;
    ArrayList<String> voteList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_vote);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("투표 확인");

        voteRef = FirebaseDatabase.getInstance().getReference("game").child("0").child("vote");
        voteRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot voteData : dataSnapshot.getChildren()) {
                    String stFrom = voteData.child("from").getValue().toString();
                    String stTo = voteData.child("to").getValue().toString();
                    String stTime = voteData.child("time").getValue().toString();
                    voteList.add(stFrom+"님이 "+stTo+"에게 투표하였습니다. ("+stTime+")");
                }
                Collections.sort(voteList, new Comparator<String>() {
                    @Override
                    public int compare(String text1, String text2) {
                        String time1 = text1.substring(text1.indexOf("("),text1.indexOf(")"));
                        String time2 = text2.substring(text2.indexOf("("),text2.indexOf(")"));
                        return time1.compareTo(time2);
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, voteList);

        voteView = findViewById(R.id.lv_vote);
        voteView.setAdapter(adapter);
    }
}
