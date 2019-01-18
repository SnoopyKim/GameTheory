package com.example.android.gametheory;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameManageFragment extends Fragment {
    private static final String TAG = ".GameManageFragment";

    int current_game;

    DatabaseReference timerRef;
    long time;

    public GameManageFragment() {
    }
    public static GameManageFragment newInstance(int id) {
        GameManageFragment fragment = new GameManageFragment();

        // Supply player data as an argument.
        Bundle args = new Bundle();
        args.putInt("now", id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game_manage, container, false);

        current_game = getArguments().getInt("now");

        init(v);

        Button btnRuleBook = v.findViewById(R.id.btn_rule_book);
        btnRuleBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                RuleBookDialog dialog = RuleBookDialog.newInstance(current_game);
                dialog.show(fm, "test");
            }
        });

        timerRef = FirebaseDatabase.getInstance().getReference("game").child("0").child("time");

        Button btnCheckTimer = v.findViewById(R.id.btn_check_timer);
        btnCheckTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimerDialog dialog = new TimerDialog();
                dialog.show(fm, "timer");
            }
        });
        Button btnSetTimer = v.findViewById(R.id.btn_set_timer);
        btnSetTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                time = date.getTime();
                final long endTime = time + 10*1000;

                new Timer().scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            time += 1000;
                            timerRef.setValue((endTime-time)/1000);

                            if(time == endTime) { this.cancel(); }
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

    private void init(View v) {
        String [] titles = getResources().getStringArray(R.array.title);
        TextView tvCurrentGame = v.findViewById(R.id.tv_current_game_value);
        tvCurrentGame.setText(titles[current_game]);
    }
}