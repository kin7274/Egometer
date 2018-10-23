package com.example.elab_yang.egometer.etc;

public class EGDataConverter {
    public EGDataConverter() {
    }
    public static String parseBluetoothData(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        float floatVal;

        final int intVal = (data[3] << 8) & 0xff00 | (data[2] & 0xff);
        floatVal = (float) intVal / 100;
        return String.format("%.2f", floatVal);
    }

    public static String parseTreadmillData(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        float floatVal;


        final int intVal = (data[4] << 16) & 0xff0000 | (data[3] << 8) & 0x00ff00 | (data[2] & 0xff);
        return String.format("%d m", intVal);
    }
}
