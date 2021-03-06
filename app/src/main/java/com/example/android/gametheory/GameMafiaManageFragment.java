package com.example.android.gametheory;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
public class GameMafiaManageFragment extends Fragment {
    private static final String TAG = ".GameMafiaManageFragment";

    DatabaseReference gameRef;
    long time_timer;
    long time;

    TextView tvTurn, tvTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_game_mafia_manage, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("마피아 게임");

        tvTurn = v.findViewById(R.id.tv_turn);
        tvTimer = v.findViewById(R.id.tv_turn_time);

        gameRef = FirebaseDatabase.getInstance().getReference("game").child("0");
        gameRef.child("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                time = (long)dataSnapshot.getValue();
                long minute = time/60;
                long second = (time%60);
                if (time==0 && getContext()!=null) {
                    tvTurn.setText(getString(R.string.turn_night));
                    tvTimer.setVisibility(View.INVISIBLE);
                }
                if (time != 0 && tvTimer.getVisibility()==View.INVISIBLE) { tvTimer.setVisibility(View.VISIBLE); }
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

        Button btnCheckVote = v.findViewById(R.id.btn_check_vote);
        btnCheckVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckVoteActivity.class);
                startActivity(intent);
            }
        });
        Button btnSetTurn = v.findViewById(R.id.btn_set_turn);
        btnSetTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // in VoteActivity
                AlertDialog.Builder voteDialog = new AlertDialog.Builder(getActivity());
                voteDialog.setTitle("낮 시작");
                voteDialog.setMessage("낮을 시작하시겠습니까?");
                voteDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                voteDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameRef.child("vote").removeValue();
                        tvTurn.setText(getString(R.string.turn_daytime));
                        tvTimer.setVisibility(View.VISIBLE);

                        Date date = new Date();
                        time_timer = date.getTime();
                        final long endTime = time_timer + 60*1000;

                        new Timer().scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                time_timer += 1000;
                                gameRef.child("time").setValue((endTime-time_timer)/1000);
                                if(time_timer == endTime) { this.cancel(); }
                            }
                        }
                        , 0
                        , 1000);
                    }
                });
                voteDialog.show();
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