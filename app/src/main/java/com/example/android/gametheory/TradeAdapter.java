package com.example.android.gametheory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.ViewHolder> {
    private List<Trade> mTrade;

    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tvText;

        ViewHolder(View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TradeAdapter(List<Trade> aTrade, Context context) {
        this.mTrade = aTrade;
        this.context = context;
    }

    //View 생성 시
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trade Trade = mTrade.get(position);

        String stText = Trade.getName() + "님이 " + Trade.getAuction_id() + "를 " + Trade.getBid() + "$에 낙찰";
        holder.tvText.setText(stText);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mTrade.size();
    }
}
