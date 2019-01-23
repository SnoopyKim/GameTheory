package com.example.android.gametheory;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameBlockFragment extends Fragment {
    private static final String TAG = ".GameBlockFragment";

    DatabaseReference timerRef;
    long time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_block, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("블록체인 게임");

        Button btnRuleBook = v.findViewById(R.id.btn_rule_book);
        btnRuleBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                RuleBookDialog dialog = RuleBookDialog.newInstance(1);
                dialog.show(fm, "test");
            }
        });

        timerRef = FirebaseDatabase.getInstance().getReference("game").child("1").child("time");
        final TextView tvTurn = v.findViewById(R.id.tv_turn);
        final TextView tvTimer = v.findViewById(R.id.tv_turn_time);
        timerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                time = (long)dataSnapshot.getValue();
                long minute = time/60;
                long second = (time%60);
                tvTimer.setText(String.format("%d : %d", minute,second));
                if (time == 0) { tvTurn.setText("경매 종료"); }
                else if (time == 59) { tvTurn.setText("경매중"); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        Button btnTradeBook = v.findViewById(R.id.btn_trade_book);
        btnTradeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TradeBookActivity.class);
                startActivity(intent);
            }
        });

        Button btnCheckRank = v.findViewById(R.id.btn_check_rank);
        btnCheckRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckRankActivity.class);
                startActivity(intent);
            }
        });

        Button btnAuction = v.findViewById(R.id.btn_auction);
        btnAuction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuctionActivity.class);
                startActivity(intent);
            }
        });

        Button btnLogout = v.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return v;
    }

}