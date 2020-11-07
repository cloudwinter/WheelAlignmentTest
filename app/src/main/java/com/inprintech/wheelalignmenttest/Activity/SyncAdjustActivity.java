package com.inprintech.wheelalignmenttest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.ReportDataManager;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteBurdenWheelHelpter;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.Constants;
import com.inprintech.wheelalignmenttest.Utils.NumberUtils;
import com.inprintech.wheelalignmenttest.Utils.SocketUitls;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.inprintech.wheelalignmenttest.common.CommonConstants;
import com.inprintech.wheelalignmenttest.core.RunningContext;

import java.util.Random;

import androidx.appcompat.app.AlertDialog;

/**
 * 同步测量
 */
public class SyncAdjustActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "SyncAdjustActivity";


    private double defaultInterval = -5.0;
    private double defaultPitch = 0.5;

    /**
     * 默认的角度有效间距
     */
    private static final double LEFT_DEFAULT_ANGLE_INVALID_INTERVAL = 0.5;
    private static final double RIGHT_DEFAULT_ANGLE_INVALID_INTERVAL = 0.5;

    private ImageView imgBack;
    private TextView tvTips;
    private TextView tvMeasuringorDirectionAngle;
    private TextView tvLeft_measuring_angle, tvRight_measuring_angle, tvSync_valus;
    private Button btnDetermine, btnSubmit;
    private View Sync_valusBg;

    private SocketUitls socketUitls = new SocketUitls();
    private boolean socket;
    private SendThread sendThread;

    private byte[] data = new byte[26];
    private int channel = 0;
    //1方向角  2测量角
    private double Ccd_data1_1 = 0, Ccd_data1_2 = 0;
    // 左侧测量值，右侧测量值
    private double t1 = 0, t2 = 0;
    private double syncValus = 0;

    private int check = 0;
    private String strRegisterNum;
    private String sel1, sel2, sel3, sel4;
    private SharedPreferences sharedDefining;
    private SharedPreferences sharedVehicle;
    private SharedPreferences sharedMeasure;
    SQLiteBurdenWheelHelpter dbHelper1, dbHelper2, dbHelper3, dbHelper4;
    SQLiteDatabase db1, db2, db3, db4;
    // 是否确认左侧，是否确认右侧
    private boolean isDetermineLeft, isDetermineRight;

    private static int timeInterVal = CommonConstants.TIME_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_adjust);
        showSingleAlertDialog();
        initView();
    }

    private AlertDialog alertDialog2; //单选框
    private String adjustBeforeAfter = "before";

    private void showSingleAlertDialog() {
        final String[] items = {getString(R.string.str_before_adjustment), getString(R.string.str_after_adjustment)};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getString(R.string.str_select_device_status));
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    adjustBeforeAfter = "before";
                } else {
                    adjustBeforeAfter = "after";
                }
            }
        });

        alertBuilder.setPositiveButton(getText(R.string.str_determine), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog2.dismiss();
            }
        });

        alertBuilder.setNegativeButton(getText(R.string.str_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(SyncAdjustActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                alertDialog2.dismiss();
            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.setCanceledOnTouchOutside(false);
        alertDialog2.show();
    }


    private void initView() {
        Sync_valusBg = findViewById(R.id.sync_valus_bg);
        tvTips = findViewById(R.id.tv_tips);
        imgBack = findViewById(R.id.img_back_main);
        tvMeasuringorDirectionAngle = findViewById(R.id.tv_measuring_angle);
        tvLeft_measuring_angle = findViewById(R.id.tv_left_measuring_angle);
        tvRight_measuring_angle = findViewById(R.id.tv_right_measuring_angle);
        tvSync_valus = findViewById(R.id.tv_sync_valus);
        btnDetermine = findViewById(R.id.btn_determine);
        btnSubmit = findViewById(R.id.btn_submit);

        imgBack.setOnClickListener(this);
        btnDetermine.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        SharedPreferences sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
        float sharedStoreFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.SYNC_KEY, 0L);
        if (sharedStoreFloat != 0) {
            defaultInterval = sharedStoreFloat;
        }
        float sharedStorePitchFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.SYNC_PITCH_KEY, 0L);
        if (sharedStorePitchFloat != 0) {
            defaultPitch = sharedStorePitchFloat;
        }

        timeInterVal = sharedStore.getInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, CommonConstants.TIME_INTERVAL);

        openSocket();

        debug();
    }

    private void debug() {
        if (RunningContext.debug) {
            btnSubmit.setEnabled(true);
            syncValus = new Random().nextFloat();
        }
    }


    private void openSocket() {
        socket = socketUitls.openSocket();
        if (socket) {
            startSendThread();
            socketUitls.setOnDataReceiveListener(listener);
        } else {
            return;
        }
    }


    private SocketUitls.OnDataReceiveListener listener = new SocketUitls.OnDataReceiveListener() {
        @Override
        public void onDataReceive(byte[] buffer, int size) {
            data = buffer;
            Log.i(TAG, "onDataReceive: " + UITools.bytesToHexString(data));
            int msgType = UITools.getUnsignedByte(data[6]);
            Log.i(TAG, "run: 消息类型-" + msgType);
            if (msgType == 60) {
                channel = UITools.getUnsignedByte(data[8]);
                if (channel != 1 && channel != 2) {
                    return;
                }
                int ccd_data1_flag = UITools.getUnsignedByte(data[3]);
                if (ccd_data1_flag == 0) {
                    // 无效数据
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (channel == 1) {
                            Ccd_data1_1 = UITools.Ccd_data(UITools.M(data[16], data[17]));
                            Log.i(TAG, "run: Ccd_data1_1-" + Ccd_data1_1);
                            // 设置方向角
                            tvMeasuringorDirectionAngle.setText(NumberUtils.double2Str(Ccd_data1_1));
                        } else if (channel == 2) {
                            Ccd_data1_2 = UITools.Ccd_data(UITools.M(data[16], data[17]));
                            Log.i(TAG, "run: Ccd_data1_2-" + Ccd_data1_2);
                        }

                        if (Math.abs(Ccd_data1_1 - defaultInterval) <= defaultPitch) {
                            t1 = Ccd_data1_2;
                            tvLeft_measuring_angle.setText(NumberUtils.double2Str(t1));
                            tvTips.setText(getResources().getText(R.string.str_turn_right_5));
                            playMusic();
                            isDetermineLeft = true;
                        }

                        if (isDetermineLeft) {
                            // 只有左侧测量过，才能测量右侧
                            if (Math.abs(Ccd_data1_1 + defaultInterval) <= defaultPitch) {
                                t2 = Ccd_data1_2;
                                tvRight_measuring_angle.setText(NumberUtils.double2Str(Ccd_data1_2));
                                syncValus = t2 + t1;
                                tvSync_valus.setText(NumberUtils.double2Str(syncValus));
                                btnSubmit.setEnabled(true);
                                playMusic();
                            }

                            if (syncValus != 0) {
                                // FIXME 待定
                                if (syncValus <= defaultInterval) {
                                    Sync_valusBg.setBackgroundColor(getResources().getColor(R.color.coaxial_green));
                                } else {
                                    Sync_valusBg.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                }
                            }
                        }
                    }
                });
            }
        }
    };

    private void startSendThread() {
        if (sendThread != null) {
            sendThread.close();
            sendThread = null;
        }
        sendThread = new SendThread();
        sendThread.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(SyncAdjustActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_submit:
                UITools.showToast(SyncAdjustActivity.this, R.string.str_save_successfully);
                saveData();
                break;
            case R.id.btn_determine:
                determine();
                break;
        }
    }

    private void determine() {
        if (!isDetermineLeft) {
            // 表示左边未设置
            isDetermineLeft = true;
            tvTips.setText(getResources().getText(R.string.str_turn_right_5));
            playMusic();
        } else if (isDetermineLeft && !isDetermineRight) {
            // 表示右边未设置
            isDetermineRight = true;
            btnSubmit.setEnabled(true);
            playMusic();
        }
    }

    /**
     * 保存数据到报表
     */
    private void saveData() {
        sharedDefining = getSharedPreferences("defining_Vehicles", MODE_PRIVATE);
        // select{i}_1 表示转向，select{i}_2表示承重桥
        sel1 = sharedDefining.getString("sel1", "select1_0");
        sel2 = sharedDefining.getString("sel2", "select2_0");
        sel3 = sharedDefining.getString("sel3", "select3_0");
        sel4 = sharedDefining.getString("sel4", "select4_0");
        // 车辆信息
        sharedVehicle = getSharedPreferences("Vehicle_Info", MODE_PRIVATE);
        strRegisterNum = sharedVehicle.getString("registerNum", "");
        // 轮子信息（1，2，3，4）
        sharedMeasure = getSharedPreferences("check_measure", MODE_PRIVATE);
        check = sharedMeasure.getInt("check", 0);
        String key = ReportDataManager.genKey(strRegisterNum, check + "");
        ReportDataManager.setSyncValue(key, NumberUtils.double2Str(syncValus),
                adjustBeforeAfter.equals("before") ? true : false);
    }


    /**
     * 测量角是否在规定的区间之内
     *
     * @param measureAngle
     * @param referAngle
     * @param interval  一定是一个正数
     * @return
     */
    private boolean isInterval(double measureAngle, double referAngle, double interval) {
        if (measureAngle > (referAngle + interval)) {
            return false;
        }
        if (measureAngle < (referAngle - interval)) {
            return false;
        }
        return true;
    }

    private void playMusic() {
        MediaPlayer music = MediaPlayer.create(this, R.raw.kring);
        music.start();
    }

    private class SendThread extends Thread {
        private boolean mRunning = false;

        private int msgWhat = 1;

        @Override
        public void run() {
            mRunning = true;
            super.run();
            while (mRunning) {
                UITools.sleeoTime(timeInterVal);
                sendHandler.sendEmptyMessage(msgWhat);
                if (msgWhat == 1) {
                    msgWhat = 2;
                } else {
                    msgWhat = 1;
                }
            }
        }

        public void close() {
            mRunning = false;
        }
    }



    private Handler sendHandler = new Handler() {
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

    @Override
    protected void onPause() {
        super.onPause();
        if (socketUitls != null) {
            socketUitls.closeSocket();
        }
        if (sendThread != null) {
            sendThread.close();
        }
    }

    @Override
    protected void onRestart() {
        openSocket();
        super.onRestart();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(SyncAdjustActivity.this, MeasureAdjustActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
