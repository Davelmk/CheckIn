package com.dave.checkin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dave.checkin.R;
import com.dave.checkin.beans.User;

import java.util.List;

public class GroupMemberAdapter extends RecyclerView.Adapter <GroupMemberAdapter.ViewHolder>{
    public Context mContext;
    private List<User> list;
    private MyItemClickListener mItemClickListener;
    public GroupMemberAdapter(Context context,List<User> list) {
        this.list = list;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_created_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.memberName.setText(list.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView memberIcon;
        TextView memberName;
        MyItemClickListener mListener;
        public ViewHolder(View itemView) {
            super(itemView);
            memberIcon=itemView.findViewById(R.id.memberIcon);
            memberName=itemView.findViewById(R.id.memberName);
            this.mListener=mItemClickListener;
            memberIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
