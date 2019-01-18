package com.example.android.gametheory;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private ArrayList<Player> playerList;
    private Context context;
    FirebaseUser user;

    boolean vote;

    PlayerAdapter(Context context, ArrayList<Player> list, boolean vote) {
        this.context = context;
        this.playerList = list;
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.vote = vote;
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
                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                if (vote) {
                    // in VoteActivity
                    AlertDialog.Builder voteDialog = new AlertDialog.Builder(context);
                    voteDialog.setTitle("투표");
                    voteDialog.setMessage(player.getName()+"에게 투표하시겠습니까?");
                    voteDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatabaseReference voteRef = FirebaseDatabase.getInstance().getReference("game").child("0").child("vote").child(user.getUid());
                            voteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        CustomUtils.displayToast(context, "이미 투표하셨습니다.");
                                    } else {
                                        dataSnapshot.getRef().child("from").setValue(user.getDisplayName());
                                        dataSnapshot.getRef().child("to").setValue(player.getName());
                                        Calendar time = Calendar.getInstance();
                                        String stTime = time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE);
                                        dataSnapshot.getRef().child("time").setValue(stTime);

                                        CustomUtils.displayToast(context, player.getName()+"에게 투표하셨습니다.");
                                        ((AppCompatActivity) context).finish();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }
                            });
                        }
                    });
                    voteDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    });
                    voteDialog.show();
                } else {
                    // in Another Activity or Fragment
                    PlayerDialog dialog = PlayerDialog.newInstance(player, isManager);
                    dialog.show(fm, "test");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
