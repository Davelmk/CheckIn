package com.dave.checkin.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dave.checkin.R;
import com.dave.checkin.beans.User;

import java.util.List;

public class SignMemberAdapter extends RecyclerView.Adapter<SignMemberAdapter.ViewHolder> {
    private List<User> users;

    public SignMemberAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sign_member,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.sign_member_name.setText(users.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sign_member_name;
        public ViewHolder(View itemView) {
            super(itemView);
            sign_member_name=itemView.findViewById(R.id.sign_member_name);
        }
    }
}
