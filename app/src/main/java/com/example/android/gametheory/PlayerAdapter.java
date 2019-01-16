package com.example.android.gametheory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private ArrayList<Player> playerList;
    private Context context;
    FirebaseUser user;

    PlayerAdapter(Context context, ArrayList<Player> list) {
        this.context = context;
        this.playerList = list;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout playerLayout;
        ImageView ivProfile;
        TextView tvName;

        PlayerViewHolder(View view) {
            super(view);
            playerLayout = view.findViewById(R.id.ll_player);
            ivProfile = view.findViewById(R.id.iv_profile);
            tvName = view.findViewById(R.id.tv_name);
        }
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_player, viewGroup, false);
        return new PlayerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int i) {
        final Player player = playerList.get(i);
        final boolean isManager = user.getDisplayName().equals("manager");

        if (player.getProfile()!=null) {
            Glide.with(context).load(player.getProfile()).into(holder.ivProfile);
        }

        holder.tvName.setText(player.getName());
        holder.playerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                PlayerDialog dialog = PlayerDialog.newInstance(player, isManager);
                dialog.show(fm, "test");
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
