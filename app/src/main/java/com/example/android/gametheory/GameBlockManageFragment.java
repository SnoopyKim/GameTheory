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

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameBlockManageFragment extends Fragment {
    private static final String TAG = ".GameBlockManageFragment";

    DatabaseReference timerRef;
    long time_timer, time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_block_manage, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("블록체인 게임");

        timerRef = FirebaseDatabase.getInstance().getReference("game").child("1").child("time");
        final TextView tvTimer = v.findViewById(R.id.tv_turn_time);
        timerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                time = (long)dataSnapshot.getValue();
                long minute = time/60;
                long second = (time%60);
                tvTimer.setText(String.format("%d : %d", minute,second));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        Button btnRuleBook = v.findViewById(R.id.btn_rule_book);
        btnRuleBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                RuleBookDialog dialog = RuleBookDialog.newInstance(0);
                dialog.show(fm, "test");
            }
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

        Button btnSetTurn = v.findViewById(R.id.btn_set_turn);
        btnSetTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                time_timer = date.getTime();
                final long endTime = time_timer + 60*1000;

                new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            time_timer += 1000;
                            timerRef.setValue((endTime-time_timer)/1000);

                            if(time_timer == endTime) { this.cancel(); }
                        }
                    }
                    , 0
                    , 1000);
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