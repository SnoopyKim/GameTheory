package com.example.android.gametheory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private List<Rank> mRank;

    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tvRank, tvText, tvScore;

        ViewHolder(View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            tvText = itemView.findViewById(R.id.tv_text);
            tvScore = itemView.findViewById(R.id.tv_score);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RankAdapter(List<Rank> aRank, Context context) {
        this.mRank = aRank;
        this.context = context;
    }

    //View 생성 시
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Rank rank = mRank.get(position);

        holder.tvRank.setText(String.valueOf(position+1));
        holder.tvText.setText(rank.getName());
        holder.tvScore.setText(rank.getScore()+"점");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRank.size();
    }
}
