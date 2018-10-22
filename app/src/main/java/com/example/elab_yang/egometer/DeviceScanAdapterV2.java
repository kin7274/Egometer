package com.example.elab_yang.egometer;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elab_yang.egometer.model.Device;

import java.util.ArrayList;
import java.util.HashSet;

import io.paperdb.Paper;

public class DeviceScanAdapterV2 extends RecyclerView.Adapter<DeviceScanAdapterV2.DeviceScanViewHolder> {
    ArrayList<BluetoothDevice> deviceArrayList;
    ArrayList<ScanResult> scanResultArrayList;
    Context context;
    SharedPreferences preferences;

    HashSet<Device> deviceDatabase = new HashSet<>();

    DeviceItemClickListener deviceItemClickListener;

    public void setDeviceItemClickListener(DeviceItemClickListener deviceItemClickListener) {
        this.deviceItemClickListener = deviceItemClickListener;
    }

    public DeviceScanAdapterV2(ArrayList<BluetoothDevice> deviceArrayList, ArrayList<ScanResult> scanResults, Context context) {
        this.deviceArrayList = deviceArrayList;
        this.scanResultArrayList = scanResults;
        this.context = context;

        preferences = this.context.getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        Paper.init(this.context);
    }

    @NonNull
    @Override
    public DeviceScanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_device_v2, parent, false);
        return new DeviceScanViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceScanViewHolder holder, int position) {

        final String deviceName = deviceArrayList.get(position).getName();
        final String deviceAddress = deviceArrayList.get(position).getAddress();

        if (deviceName != null && deviceName.length() > 0)
            holder.deviceName.setText(deviceName);
        else
            holder.deviceName.setText("unknown_device");
//        holder.deviceName.setText(deviceArrayList.get(position).getName());
        holder.deviceAddress.setText(deviceArrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceArrayList.size();
    }

    class DeviceScanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView deviceName;
        TextView deviceAddress;
        LinearLayout container;

        DeviceScanViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name);
            deviceAddress = itemView.findViewById(R.id.device_address);
            container = itemView.findViewById(R.id.container);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (deviceItemClickListener != null) {
                deviceItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
