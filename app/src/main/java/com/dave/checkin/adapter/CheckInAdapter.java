package com.dave.checkin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dave.checkin.R;
import com.dave.checkin.beans.CheckIn;

import java.util.List;

public class CheckInAdapter extends RecyclerView.Adapter <CheckInAdapter.ViewHolder>{
    public Context mContext;
    private List<CheckIn> list;
    private MyItemClickListener mItemClickListener;
    public CheckInAdapter(Context context,List<CheckIn> list) {
        this.list = list;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkin_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(list.get(position).getBackgroundColor()));
        holder.item_title.setText(list.get(position).getTitle());
        holder.item_owner.setText(list.get(position).getOwner());
        holder.item_num.setText(list.get(position).getNumOfMember()+"人");
        holder.item_time.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cardView;
        TextView item_title;
        TextView item_owner;
        TextView item_num;
        TextView item_time;
        MyItemClickListener mListener;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardView);
            item_title=itemView.findViewById(R.id.title);
            item_owner=itemView.findViewById(R.id.owner);
            item_num=itemView.findViewById(R.id.num);
            item_time=itemView.findViewById(R.id.time);
            this.mListener=mItemClickListener;
            cardView.setOnClickListener(this);
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
