package com.example.android.gametheory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


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

        current_game = ((MainActivity)getActivity()).currentGame;

        ImageButton btnRuleBook = v.findViewById(R.id.btn_rule_book);
        btnRuleBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                RuleBookDialog dialog = RuleBookDialog.newInstance(current_game);
                dialog.show(fm, "test");
            }
        });

        return v;
    }

}
