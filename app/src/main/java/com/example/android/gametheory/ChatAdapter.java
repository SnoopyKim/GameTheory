package com.example.android.gametheory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Chat> mChat;

    private String stUid;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tvChat;

        ViewHolder(View itemView) {
            super(itemView);
            tvChat = itemView.findViewById(R.id.tv_chat);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(List<Chat> aChat, String uid, Context context) {
        this.mChat = aChat;
        this.stUid = uid;
        this.context = context;
    }

    //말풍선이 자기꺼면 1, 다른사람꺼면 2를 반환
    @Override
    public int getItemViewType(int position) {
        if (mChat.get(position).getUid().equals(stUid)) {
            Log.d("ChatAdapter", "getItemViewType: 1");
            return 1;
        } else {
            Log.d("ChatAdapter", "getItemViewType: 2");
            return 2;
        }
    }

    //View 생성 시
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //만약 자신의 말풍선이면 오른쪽으로 보이는 layout, 아니면 왼쪽으로 보이는 layout로 설정
        View v;
        if (viewType == 1) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_text_view, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.left_text_view, parent, false);
        }

        return new ViewHolder(v);
    }

    //각 말풍선 View의 데이터 및 이벤트 관리
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Chat chat = mChat.get(position);

        holder.tvChat.setText(chat.getText());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mChat.size();
    }
}
