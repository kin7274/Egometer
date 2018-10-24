package com.example.elab_yang.egometer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.model.CardItem;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements OnLongClickListener {
    Context mContext;
    private final List<CardItem> mDataList;
    public MyRecyclerAdapter(List<CardItem> dataList) {
        mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardItem item = mDataList.get(position);

        CardView cardview = holder.cardview;
        TextView date = holder.date;
        TextView time = holder.time;
        TextView speed = holder.speed;
        TextView distance = holder.distance;
        TextView bpm = holder.bpm;
        TextView kcal = holder.kcal;

        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.speed.setText(item.getSpeed());
        holder.distance.setText(item.getDistance());
        holder.bpm.setText(item.getBpm());
        holder.kcal.setText(item.getKcal());

        holder.cardview.setOnClickListener(v -> {
            Toast.makeText(mContext, "쌔게...", Toast.LENGTH_SHORT).show();
        });

        holder.cardview.setOnLongClickListener(v -> {
            removeAt(position);
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
    }

    public void removeAt(int position) {
        mDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataList.size());
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardview;
        TextView date, time, speed, distance, bpm, kcal;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            speed = (TextView) itemView.findViewById(R.id.speed);
            distance = (TextView) itemView.findViewById(R.id.distance);
            bpm = (TextView) itemView.findViewById(R.id.bpm);
            kcal = (TextView) itemView.findViewById(R.id.kcal);
            cardview.setOnClickListener(v -> {
            });
            itemView.setOnClickListener(v -> {
//                    Toast.makeText(mContext, "머요ㅡㅡ", Toast.LENGTH_LONG).show();
            });
        }
    }
}