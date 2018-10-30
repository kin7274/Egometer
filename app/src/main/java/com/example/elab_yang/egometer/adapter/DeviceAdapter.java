/*  Copyright (C) 2015-2018 Andreas Shimokawa, Carsten Pfeiffer, Daniele
    Gobbetti, Lem Dulfo

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package com.example.elab_yang.egometer.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elab_yang.egometer.IndoorBikeRealTimeActivity;
import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.activity.MainActivity;
import com.example.elab_yang.egometer.activity.treadmill.DeviceControlActivity;
import com.example.elab_yang.egometer.activity.treadmill.TimelineActivity;
import com.example.elab_yang.egometer.etc.EGZeroConst;
import com.example.elab_yang.egometer.model.Device;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.paperdb.Paper;

import static com.example.elab_yang.egometer.etc.IntentConst.REAL_TIME_INDOOR_BIKE_DEVICE;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private Context context;
    private List<Device> deviceList;
    private int expandedDevicePosition = RecyclerView.NO_POSITION;
    private ViewGroup parent;
    private static final String TAG = "DeviceAdapter";
    boolean deviceFlag;
    HashSet<Device> deviceDatabase = new HashSet<>();
    ArrayList<Device> deviceArrayList;

    public DeviceAdapter(Context context, List<Device> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
    }

    public DeviceAdapter(Context context, List<Device> deviceList, boolean deviceFlag) {
        this.context = context;
        this.deviceList = deviceList;
        this.deviceFlag = deviceFlag;
    }

    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Paper.init(context);
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String deviceName = deviceList.get(position).getDeviceName();
        String deviceAddress = deviceList.get(position).getDeviceAddress();
//        holder.deviceNameLabel.setText(deviceList.get(position).getDeviceName());
        if(deviceName.equals("KNU EG0")){
            // 에르고미터
            Log.d(TAG, "onBindViewHolder: 이건 에르고미터");
            holder.deviceNameLabel.setText("에르고미터");
        } else if(deviceName.equals("HMSoft")){
            // 트레드밀
            Log.d(TAG, "onBindViewHolder: 이건 트레드밀");
            holder.deviceNameLabel.setText("트레드밀");
        }
        holder.deviceStatusLabel.setText(deviceList.get(position).getDeviceAddress());
        if (deviceName.equals(EGZeroConst.DEVICE_NAME)) {
            // deviceFlag true <- ergometer
            holder.fetchActivityData.setVisibility(View.GONE);
            holder.showActivityTracks.setImageResource(R.drawable.ic_activity_tracks);
        } else {// 동기화
            holder.fetchActivityData.setOnClickListener((View v) -> {
                Log.e("클릭됨", "onClick: 클릭툄" + EGZeroConst.DEVICE_NAME);
                Intent bsmIntent1 = new Intent(context, TimelineActivity.class);
                bsmIntent1.putExtra(REAL_TIME_INDOOR_BIKE_DEVICE, deviceAddress);
                context.startActivity(bsmIntent1);
            });
        }

        // 운동하러ㄱ
        holder.showActivityTracks.setOnClickListener(v -> {
            if (deviceName.equals(EGZeroConst.DEVICE_NAME)) {
                Intent bsmIntent = new Intent(context, IndoorBikeRealTimeActivity.class);
                bsmIntent.putExtra(REAL_TIME_INDOOR_BIKE_DEVICE, deviceAddress);
                context.startActivity(bsmIntent);
            } else {
                Intent bsmIntent = new Intent(context, DeviceControlActivity.class);
                bsmIntent.putExtra(REAL_TIME_INDOOR_BIKE_DEVICE, deviceAddress);
                context.startActivity(bsmIntent);
            }
        });

        // 설명
        holder.deviceInfoView.setOnClickListener(v -> {
            if (deviceName.equals(EGZeroConst.DEVICE_NAME)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(EGZeroConst.DEVICE_NAME);
                builder.setMessage("에르고미터입니다");
                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("트레드밀입니다");
                builder.setMessage("트레드밀입니다");
                builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });

        // 연결장치 제거
        holder.deviceRemove.setOnClickListener(v -> {
            Log.e(TAG, "deviceAddress: --> " + deviceAddress);
            Log.e(TAG, "deviceName: --> " + deviceName);
            deviceDatabase = Paper.book("device").read("user_device");
            deviceArrayList = new ArrayList<>(deviceDatabase);
            for (Device d : deviceArrayList) {
                Log.e(TAG, "deviceArrayList: --> " + d.getDeviceAddress());
                Log.e(TAG, "deviceArrayList: --> " + d.getDeviceName());
            }
            Log.e(TAG, "onBindViewHolder: " + deviceList.get(position).getDeviceName());
            Log.e(TAG, "onBindViewHolder: " + deviceList.indexOf(deviceList.get(position)));
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("장비 삭제하기");
            builder.setMessage("등록하신 장비를 삭제하시겠어요?");
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                Log.e(TAG, "onClick: " + deviceAddress);
                deviceList.remove(deviceList.get(position));
                HashSet<Device> tmpSet = new HashSet<>(deviceList);
                for (Device d : tmpSet) {
                    Log.e(TAG, "tmpSet: --> " + d.getDeviceAddress());
                    Log.e(TAG, "tmpSet: --> " + d.getDeviceName());
                }
                Paper.book("device").delete("user_device");
                Paper.book("device").write("user_device", tmpSet);
                notifyDataSetChanged();
            });
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;

        ImageView deviceImageView;
        TextView deviceNameLabel;
        TextView deviceStatusLabel;

        ImageView fetchActivityData;
        ImageView showActivityTracks;

        ImageView deviceInfoView;
        ImageView deviceRemove;

        ViewHolder(View view) {
            super(view);
            cardview = view.findViewById(R.id.card_view);

            deviceImageView = view.findViewById(R.id.device_image);
            deviceNameLabel = view.findViewById(R.id.device_name);
            deviceStatusLabel = view.findViewById(R.id.device_status);
            fetchActivityData = view.findViewById(R.id.device_action_fetch_activity);
            showActivityTracks = view.findViewById(R.id.device_action_show_activity_tracks);
            deviceInfoView = view.findViewById(R.id.device_info_image);
            deviceRemove = view.findViewById(R.id.device_info_trashcan);
        }
    }
}