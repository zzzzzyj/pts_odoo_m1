package com.pts_odoo_m1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;

import com.android.RfidControll;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by zyj on 01/03/2018.
 */

public class ScanAndRfidModule extends ReactContextBaseJavaModule {

    RfidControll rfidControll = new RfidControll();

    private BroadcastReceiver iDataScanReceiver;
    private IntentFilter iDataIntentFilter;
    //IntentFilter
    private static final String RES_ACTION = "android.intent.action.SCANRESULT";
    //打开与关闭扫描头
    private static final String KEY_BARCODE_ENABLESCANNER_ACTION = "android.intent.action.BARCODESCAN";
    //是否广播模式
    public static final String KEY_OUTPUT_ACTION = "android.intent.action.BARCODEOUTPUT";

    public ScanAndRfidModule(ReactApplicationContext reactContext) {
        super(reactContext);
        rfidControll.OpenComm();
        initScan();
    }

    @Override
    public String getName() {
        return "ScanAndRfid";
    }

    @ReactMethod
    public void closeComm() {
        rfidControll.CloseComm();
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    //rfid
    int res = 0;

    @ReactMethod
    public void readCardUid() {
        byte[] uid = new byte[4];
        byte[] pdata = new byte[1];
        pdata[0] = 0x00;
        byte buffer[] = new byte[256];
        res = rfidControll.API_MF_Request(0, 0x26, buffer);
        if (res == 0) {
            res = rfidControll.API_MF_Anticoll(0, pdata, buffer);
            if (res == 0) {
                System.arraycopy(buffer, 0, uid, 0, 4);
                WritableMap params = Arguments.createMap();
                params.putString("RfidResult", getDec(uid));
                sendEvent(getReactApplicationContext(), "iDataRfid", params);
            }
        }
    }

    private String getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;

        for (int i = 0; i < bytes.length; i++) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return String.valueOf(result);
    }

    //scan
    private void initScan(){
        //扫描结果的意图过滤器的动作一定要使用"android.intent.action.SCANRESULT"
        iDataIntentFilter = new IntentFilter(RES_ACTION);
        //注册广播接收者
        iDataScanReceiver = new ScannerResultReceiver();
        getReactApplicationContext().registerReceiver(iDataScanReceiver, iDataIntentFilter);
        open();
        enablePlayBeep(false);
        setOutputMode(1);
    }

    private class ScannerResultReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RES_ACTION)){
                final String scanResult = intent.getStringExtra("value");

                WritableMap params = Arguments.createMap();
                params.putString("ScanResult", scanResult);
                sendEvent(getReactApplicationContext(), "iDataScan", params);
            }
        }
    }

    @ReactMethod
    public void openScanner(){
        open();
    }

    @ReactMethod
    public void colseScanner(){
        close();
    }

    //扫码头供电
    private void open(){
        Intent intent = new Intent(KEY_BARCODE_ENABLESCANNER_ACTION);
        intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, true);
        getReactApplicationContext().sendBroadcast(intent);
    }

    //扫码头断电
    public void  close(){
        Intent intent = new Intent(KEY_BARCODE_ENABLESCANNER_ACTION);
        intent.putExtra(KEY_BARCODE_ENABLESCANNER_ACTION, false);
        getReactApplicationContext().sendBroadcast(intent);
    }

    private void setOutputMode(int mode){
        Intent intent = new Intent(KEY_OUTPUT_ACTION);
        intent.putExtra(KEY_OUTPUT_ACTION, mode);
        getReactApplicationContext().sendBroadcast(intent);
    }

    private void enablePlayBeep(boolean enable){
        Intent intent = new Intent("android.intent.action.BEEP");
        intent.putExtra("android.intent.action.BEEP", enable);
        getReactApplicationContext().sendBroadcast(intent);
    }
}
