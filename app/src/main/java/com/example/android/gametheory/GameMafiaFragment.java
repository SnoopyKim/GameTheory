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
public class GameMafiaFragment extends Fragment {
    private static final String TAG = ".GameMafiaManageFragment";

    DatabaseReference timerRef;
    long time;

    TextView tvTurn, tvTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_mafia, container, false);
        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("마피아 게임");

        tvTurn = v.findViewById(R.id.tv_turn);
        tvTimer = v.findViewById(R.id.tv_turn_time);

        timerRef = FirebaseDatabase.getInstance().getReference("game").child("0").child("time");
        timerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                time = (long)dataSnapshot.getValue();
                long minute = time/60;
                long second = (time%60);
                if (time==0) {
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
        Button btnVote = v.findViewById(R.id.btn_vote);
        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (time==0) {
                    CustomUtils.displayToast(getActivity(), "지금은 밤입니다.");
                } else {
                    Intent intent = new Intent(getActivity(), VoteActivity.class);
                    startActivity(intent);
                }
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