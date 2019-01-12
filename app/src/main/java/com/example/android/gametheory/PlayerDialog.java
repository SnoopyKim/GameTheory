package com.example.android.gametheory;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerDialog extends DialogFragment {
    private static final String TAG = ".PlayerDialog";

    public PlayerDialog() {
        // Required empty public constructor
    }

    public static PlayerDialog newInstance(Player player) {
        PlayerDialog dialog = new PlayerDialog();

        // Supply player data as an argument.
        Bundle args = new Bundle();
        args.putString("name", player.getName());
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_player, container, false);

        ImageView ivPlayerProfile = v.findViewById(R.id.iv_profile);
        TextView tvPlayerName = v.findViewById(R.id.tv_player_name);
        tvPlayerName.setText(getArguments().getString("name"));

        Button btnDetail = v.findViewById(R.id.btn_detail);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomUtils.displayToast(getContext(), getString(R.string.detail));
                dismiss();
            }
        });
        Button btnClose = v.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

}
