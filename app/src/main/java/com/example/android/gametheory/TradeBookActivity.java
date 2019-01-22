package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TradeBookActivity extends AppCompatActivity {

    DatabaseReference tradeRef;

    RecyclerView tradeView;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Trade> tradeList = new ArrayList<>();
    TradeAdapter tradeAdapter;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_book);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("거래 장부");

        tradeRef = FirebaseDatabase.getInstance().getReference("game").child("1").child("trades");
        tradeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userData : dataSnapshot.getChildren()) {
                    String stUid = userData.child("uid").getValue().toString();
                    String stName = userData.child("name").getValue().toString();
                    int aid = Integer.valueOf(userData.child("aid").getValue().toString());
                    int bid = Integer.valueOf(userData.child("bid").getValue().toString());

                    tradeList.add(new Trade(stUid, stName, aid, bid));
                }
                tradeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        tradeView = findViewById(R.id.rv_trade);
        tradeView.hasFixedSize();
        layoutManager = new LinearLayoutManager(this);
        tradeView.setLayoutManager(layoutManager);

        tradeAdapter = new TradeAdapter(tradeList,this);
        tradeView.setAdapter(tradeAdapter);
    }
}
