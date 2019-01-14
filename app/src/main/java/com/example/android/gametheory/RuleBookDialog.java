package com.example.android.gametheory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RuleBookDialog extends DialogFragment {
    private static final String TAG = ".RuleBookDialog";

    TextView tvSubTitle, tvContext, tvIndicator;

    int page_num = 1;

    public RuleBookDialog() {
        // Required empty public constructor

    }

    public static RuleBookDialog newInstance(int game_id) {
        RuleBookDialog dialog = new RuleBookDialog();

        // Supply player data as an argument.
        Bundle args = new Bundle();
        args.putInt("game_id", game_id);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_rule_book, container, false);

        TextView tvGameTitle = v.findViewById(R.id.tv_game_title);

        tvSubTitle = v.findViewById(R.id.tv_game_rule_subtitle);
        tvContext = v.findViewById(R.id.tv_game_rule_content);
        tvIndicator = v.findViewById(R.id.tv_indicator);

        showRule();

        ImageButton btnPrev = v.findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page_num != 1) {
                    page_num--;
                    showRule();
                }
            }
        });
        ImageButton btnNext = v.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page_num != 3) {
                    page_num++;
                    showRule();
                }
            }
        });

        return v;
    }

    public void showRule() {
        switch (page_num) {
            case 1:
                tvSubTitle.setText(getString(R.string.game_rule_subtitle_1));
                tvContext.setText(getString(R.string.game_rule_content_1));
                tvIndicator.setText("1 / 3");
                break;
            case 2:
                tvSubTitle.setText(getString(R.string.game_rule_subtitle_2));
                tvContext.setText(getString(R.string.game_rule_content_2));
                tvIndicator.setText("2 / 3");
                break;
            case 3:
                tvSubTitle.setText(getString(R.string.game_rule_subtitle_3));
                tvContext.setText(getString(R.string.game_rule_content_3));
                tvIndicator.setText("3 / 3");
                break;
        }
    }

}
