package com.example.android.gametheory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AuctionActivity extends AppCompatActivity {
    private static final String TAG = ".AuctionActivity";

    FirebaseUser player;
    DatabaseReference auctionRef, playerRef, timeRef;

    TextView tvCurrentBidder, tvCurrentBid;
    EditText etBid;

    String item;
    int player_money, current_bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        player = FirebaseAuth.getInstance().getCurrentUser();

        auctionRef = FirebaseDatabase.getInstance().getReference("game/1/auction");
        playerRef = FirebaseDatabase.getInstance().getReference("game/1/players").child(player.getUid());
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                player_money = Integer.valueOf(dataSnapshot.child("money").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        timeRef = FirebaseDatabase.getInstance().getReference("game/1/time");
        timeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue().toString().equals("0")) {
                        CustomUtils.displayToast(getApplicationContext(), "경매가 종료되었습니다.");
                        finish();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        ImageView ivItem = findViewById(R.id.iv_item);

        tvCurrentBidder = findViewById(R.id.tv_current_player_value);
        tvCurrentBid = findViewById(R.id.tv_current_bid_value);
        auctionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    item = dataSnapshot.child("item").getValue().toString();

                    String stBidder = dataSnapshot.child("player_name").getValue().toString();
                    tvCurrentBidder.setText(stBidder);

                    current_bid = Integer.valueOf(dataSnapshot.child("current_bid").getValue().toString());
                    tvCurrentBid.setText(String.valueOf(current_bid));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        etBid = findViewById(R.id.et_bid);

    }

    // onClick for btn_bid
    public void bid(View view) {
        int bid = Integer.valueOf(etBid.getText().toString());
        if (bid > current_bid ) {
            HashMap<String, String> data = new HashMap<>();
            data.put("player_uid", player.getUid());
            data.put("player_name", player.getDisplayName());
            data.put("current_bid", String.valueOf(bid));
            data.put("item", item);
            auctionRef.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CustomUtils.displayToast(getApplicationContext(), "입찰되었습니다.");
                    etBid.setText("");
                }
            });
        } else {
            CustomUtils.displayToast(this, "현재 입찰가보다 높은 입찰가를 제시해주세요.");
            etBid.setText("");
            etBid.setFocusable(true);
        }
    }
}
