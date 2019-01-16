package com.example.android.gametheory;


import android.content.Intent;
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

import com.bumptech.glide.Glide;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerDialog extends DialogFragment {
    private static final String TAG = ".PlayerDialog";

    public PlayerDialog() {
        // Required empty public constructor
    }

    public static PlayerDialog newInstance(Player player, boolean isManager) {
        PlayerDialog dialog = new PlayerDialog();

        // Supply player data as an argument.
        Bundle args = new Bundle();
        args.putSerializable("player",player);
        args.putBoolean("type", isManager);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_player, container, false);

        final Player player = (Player)getArguments().getSerializable("player");

        ImageView ivPlayerProfile = v.findViewById(R.id.iv_profile);
        if (player.getProfile()!=null) {
            Glide.with(this).load(player.getProfile()).into(ivPlayerProfile);
        }

        TextView tvPlayerName = v.findViewById(R.id.tv_player_name);
        tvPlayerName.setText(player.getName());

        TextView tvPlayerNote = v.findViewById(R.id.tv_special_note_value);
        tvPlayerNote.setText(player.getNote());

        TextView tvStatus = v.findViewById(R.id.tv_status_value);
        String status = player.isStatus() ? getString(R.string.default_value_status) : getString(R.string.failed_value_status);
        tvStatus.setText(status);

        if (getArguments().getBoolean("type")) {
            Button btnMessage = v.findViewById(R.id.btn_message);
            btnMessage.setVisibility(View.VISIBLE);
            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MessageActivity.class);
                    intent.putExtra("uid", player.getUid());
                    startActivity(intent);
                }
            });
        }

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
