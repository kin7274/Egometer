package com.example.elab_yang.egometer.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.model.CardItem2;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MyRecyclerAdapter2 extends RecyclerView.Adapter<MyRecyclerAdapter2.ViewHolder> implements OnLongClickListener {
    Context mContext;
    private final List<CardItem2> mDataList;
    EventBus bus;

    // Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public MyRecyclerAdapter2(List<CardItem2> dataList) {
        mDataList = dataList;
        bus = EventBus.getDefault();
    }

    // ViewHolder가 초기화 될 때 혹은 ViewHolder를 초기화 할 때 실행되는 메서드
    // 뷰 홀더를 생성하는 부분. 레이아웃을 만드는 부분
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardItem2 item = mDataList.get(position);

        CardView cardview = holder.cardview;
        TextView user_code = holder.user_code;
        TextView date = holder.date;
        TextView time = holder.time;
        TextView distance = holder.distance;
        TextView speed = holder.speed;
        TextView bpm = holder.bpm;;

        holder.user_code.setText(item.getUser_code());
        holder.date.setText(item.getDate());
        holder.time.setText(item.getTime());
        holder.distance.setText(item.getDistance());
        holder.speed.setText(item.getSpeed());
        holder.bpm.setText(item.getBpm());

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
        TextView user_code, date, time, distance, speed, bpm;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
            user_code = (TextView) itemView.findViewById(R.id.user_code);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            distance = (TextView) itemView.findViewById(R.id.distance);
            speed = (TextView) itemView.findViewById(R.id.speed);
            bpm = (TextView) itemView.findViewById(R.id.bpm);

            cardview.setOnClickListener(v -> {
            });
            itemView.setOnClickListener(v -> {
//                    Toast.makeText(mContext, "머요ㅡㅡ", Toast.LENGTH_LONG).show();
            });
        }
    }
}