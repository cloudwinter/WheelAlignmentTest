package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.Constants;
import com.inprintech.wheelalignmenttest.Utils.SocketUitls;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.inprintech.wheelalignmenttest.common.CommonConstants;

/**
 * 诊断
 */
public class DiagnosisActivity extends BaseActivity implements View.OnClickListener, SocketUitls.OnDataReceiveListener {
    private static final String TAG = "DiagnosisActivity";

    private ImageView imgBack;
    private LinearLayout llNotnet, llData;
    private TextView tv_ccd_data0_a, tv_ccd_data1_a, tv_roll_a, tv_pitch_a, tv_yaw_a;
    private TextView tv_ccd_data0_b, tv_ccd_data1_b, tv_roll_b, tv_pitch_b, tv_yaw_b;

    private DiagmosisThread thread;
    private SocketUitls socketUitls = new SocketUitls();
    private boolean socket;
    private byte[] data = new byte[26];

    private static int timeInterVal = CommonConstants.TIME_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        initView();

        SharedPreferences sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
        //时间间隔
        timeInterVal = sharedStore.getInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, CommonConstants.TIME_INTERVAL);
    }

    private void initView() {
        llData = findViewById(R.id.ll_data);
        llNotnet = findViewById(R.id.ll_no_net);
        imgBack = findViewById(R.id.img_back_main);
        tv_ccd_data0_a = findViewById(R.id.tv_ccd_data0_a);
        tv_ccd_data0_b = findViewById(R.id.tv_ccd_data0_b);
        tv_ccd_data1_a = findViewById(R.id.tv_ccd_data1_a);
        tv_ccd_data1_b = findViewById(R.id.tv_ccd_data1_b);
        tv_roll_a = findViewById(R.id.tv_roll_a);
        tv_roll_b = findViewById(R.id.tv_roll_b);
        tv_pitch_a = findViewById(R.id.tv_pitch_a);
        tv_pitch_b = findViewById(R.id.tv_pitch_b);
        tv_yaw_a = findViewById(R.id.tv_yaw_a);
        tv_yaw_b = findViewById(R.id.tv_yaw_b);

        imgBack.setOnClickListener(this);

        socket = socketUitls.openSocket();
        if (socket) {
            llNotnet.setVisibility(View.GONE);
            llData.setVisibility(View.VISIBLE);
            getDiagmosis();
            socketUitls.setOnDataReceiveListener(this);
        } else {
            llNotnet.setVisibility(View.VISIBLE);
            llData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(DiagnosisActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (socket) {
            socketUitls.closeSocket();
        }
        if (thread != null) {
            thread.close();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(DiagnosisActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDataReceive(byte[] buffer, int size) {
        data = buffer;

        final int ccd_data0 = UITools.getUnsignedByte(data[2]);
        final int ccd_data1 = UITools.getUnsignedByte(data[3]);
        final int roll_pitch_yaw = UITools.getUnsignedByte(data[4]);
        final int msgType = UITools.getUnsignedByte(data[6]);
        Log.i(TAG, "run: 消息类型-" + msgType);
        if (msgType == 60) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int channel = UITools.getUnsignedByte(data[8]);
                    Log.i(TAG, "run: 通道-" + channel);
                    if (channel == 1) {
                        Log.i(TAG, "run: -通道1-" + UITools.bytesToHexString(data));
                        if (ccd_data0 == 0) {
                            tv_ccd_data0_a.setText("无数据");
                        } else if (ccd_data0 == 1) {
                            tv_ccd_data0_a.setText(UITools.Ccd_data(UITools.M(data[14], data[15])) + "");
                        }
                        if (ccd_data1 == 0) {
                            tv_ccd_data1_a.setText("无数据");
                        } else if (ccd_data1 == 1) {
                            tv_ccd_data1_a.setText(UITools.Ccd_data(UITools.M(data[16], data[17])) + "");
                        }
                        if (roll_pitch_yaw == 0) {
                            tv_roll_a.setText("无数据");
                            tv_pitch_a.setText("无数据");
                            tv_yaw_a.setText("无数据");
                        } else if (roll_pitch_yaw == 1) {
                            tv_roll_a.setText(UITools.Roll(data[18], data[19]) + "|" + UITools.M2(data[18],data[19]));
                            tv_pitch_a.setText(UITools.Pitch(data[20], data[21]) + "|" + UITools.M2(data[20],data[21]));
                            tv_yaw_a.setText(UITools.Yaw(data[22], data[23]) + "|" + UITools.M2(data[22],data[23]));
                        }
                        //socketUitls.sendOrder(Constants.measure2());
                    } else if (channel == 2) {
                        Log.i(TAG, "run: -通道2-" + UITools.bytesToHexString(data));
                        if (ccd_data0 == 0) {
                            tv_ccd_data0_b.setText("无数据");
                        } else if (ccd_data0 == 1) {
                            tv_ccd_data0_b.setText(UITools.Ccd_data(UITools.M(data[14], data[15])) + "");
                        }
                        if (ccd_data1 == 0) {
                            tv_ccd_data1_b.setText("无数据");
                        } else if (ccd_data1 == 1) {
                            tv_ccd_data1_b.setText(UITools.Ccd_data(UITools.M(data[16], data[17])) + "");
                        }
                        if (roll_pitch_yaw == 0) {
                            tv_roll_b.setText("无数据");
                            tv_pitch_b.setText("无数据");
                            tv_yaw_b.setText("无数据");
                        } else if (roll_pitch_yaw == 1) {
                            tv_roll_b.setText(UITools.Roll(data[18], data[19]) + "|" + UITools.M2(data[18],data[19]));
                            tv_pitch_b.setText(UITools.Pitch(data[20], data[21]) + "|" + UITools.M2(data[20],data[21]));
                            tv_yaw_b.setText(UITools.Yaw(data[22], data[23]) + "|" + UITools.M2(data[22],data[23]));
                        }
                        //socketUitls.sendOrder(Constants.measure());
                    }
                }
            });
        }


    }

    private void getDiagmosis() {
        thread = new DiagmosisThread();
        thread.start();
    }

    private class DiagmosisThread extends Thread {
        private boolean mRunning = false;

        int msgWhat = 1;

        @Override
        public void run() {
            mRunning = true;
            super.run();
            do {
                Message msg = new Message();
                msg.what = msgWhat;
                mHandler.sendMessage(msg);
                try {
                    Thread.sleep(timeInterVal);
                    if (msgWhat == 1) {
                        msgWhat = 2;
                    } else {
                        msgWhat = 1;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (mRunning);
        }

        public void close() {
            mRunning = false;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Log.d(TAG, "handleMessage: 发送设备1信号");
                    socketUitls.sendOrder(Constants.measure());
                    break;
                case 2:
                    Log.d(TAG, "handleMessage: 发送设备2信号");
                    socketUitls.sendOrder(Constants.measure2());
                    break;
            }
        }
    };

}
