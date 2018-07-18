package com.dave.checkin.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dave.checkin.R;
import com.dave.checkin.beans.Topic;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter <TopicAdapter.ViewHolder>{
    private List<Topic> list;
    public TopicAdapter(List<Topic> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.topic_user.setText(list.get(position).getName());
        holder.topic_comment.setText(list.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView topic_user;
        TextView topic_comment;
        public ViewHolder(View itemView) {
            super(itemView);
            topic_user=itemView.findViewById(R.id.topic_user);
            topic_comment=itemView.findViewById(R.id.topic_comment);
        }
    }
}
