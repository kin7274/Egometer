package com.example.elab_yang.egometer;

import android.view.View;

public interface DeviceItemClickListener {
    void onItemClick(View v, int position);
    void onItemLongClick(View v, int position);
}