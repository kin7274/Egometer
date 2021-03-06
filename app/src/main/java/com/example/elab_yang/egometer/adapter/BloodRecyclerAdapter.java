package com.example.elab_yang.egometer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.activity.navi.ViewChartActivity;
import com.example.elab_yang.egometer.model.Blood;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class BloodRecyclerAdapter extends RecyclerView.Adapter<BloodRecyclerAdapter.ViewHolder> implements OnLongClickListener {
    Context context;
    private final List<Blood> mDataList;

    public BloodRecyclerAdapter(Context context, List<Blood> dataList) {
        this.context = context;
        this.mDataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Blood item = mDataList.get(position);
        CardView cardview = holder.cardview;
        TextView date = holder.date;
        TextView bloodsugar = holder.bloodsugar;
        ImageView image = holder.image;

        holder.date.setText(item.getDate());
        holder.bloodsugar.setText(item.getBloodsugar());

        // 이미지 클릭 -> 차트 보기
        holder.image.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewChartActivity.class);
            intent.putExtra("DATE", item.getDate());
            Log.d(TAG, "DATE = " + item.getDate());
            intent.putExtra("bloodsugar", item.getBloodsugar());
            Log.d(TAG, "bloodsugar = " + item.getBloodsugar());
            context.startActivity(intent);
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

    public void setOnClickListener() {
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        TextView date;
        TextView bloodsugar;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            date = (TextView) itemView.findViewById(R.id.date);
            bloodsugar = (TextView) itemView.findViewById(R.id.txt_bloodsugar);
            image = (ImageView) itemView.findViewById(R.id.image);

            cardview.setOnClickListener(v -> {

            });
            itemView.setOnClickListener(v -> {
//                    Toast.makeText(mContext, "머요ㅡㅡ", Toast.LENGTH_LONG).show();
            });
        }
    }
}