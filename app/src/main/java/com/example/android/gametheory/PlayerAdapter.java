package com.example.android.gametheory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private ArrayList<Player> playerList;
    private Context context;

    PlayerAdapter(Context context, ArrayList<Player> list) {
        this.context = context;
        this.playerList = list;
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

        //Glide.with(this).load(player.image);
        holder.tvName.setText(player.name);
        holder.playerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
                PlayerDialog dialog = PlayerDialog.newInstance(player);
                dialog.show(fm, "test");
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
