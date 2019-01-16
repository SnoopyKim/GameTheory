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


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {
    private static final String TAG = ".GameFragment";

    int current_game;

    public GameFragment() {

    }
    public static GameFragment newInstance(int id) {
        GameFragment fragment = new GameFragment();

        // Supply player data as an argument.
        Bundle args = new Bundle();
        args.putInt("now", id);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        current_game = getArguments().getInt("now");

        init(v);


        Button btnRuleBook = v.findViewById(R.id.btn_rule_book);
        btnRuleBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                RuleBookDialog dialog = RuleBookDialog.newInstance(0);
                dialog.show(fm, "test");
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
