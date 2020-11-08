package com.inprintech.wheelalignmenttest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.ReportDataManager;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteBurdenWheelHelpter;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteSteeringWheelHelpter;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.Constants;
import com.inprintech.wheelalignmenttest.Utils.NumberUtils;
import com.inprintech.wheelalignmenttest.Utils.SocketUitls;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.inprintech.wheelalignmenttest.common.CommonConstants;
import com.inprintech.wheelalignmenttest.core.RunningContext;

import java.util.Random;

/**
 * 前束测量
 */
public class ToeinMeasureActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ToeinMeasureActivity";


    private double defaultInterval = 0.3;
    private double defaultPitch = 0.5;

    private ImageView imgBack;
    private Button btnSave;
    private TextView tvTips;
    private TextView tvRoll1, tvRoll2, tv_direction_angle, tv_total_toe_in;
    private View roll_bg1, roll_bg2, direction_angle_bg, total_toe_in_bg;
    private TextView tvT1,tvT3;

    private SocketUitls socketUitls = new SocketUitls();
    private boolean socket;
    private SendThread sendThread;

    private byte[] data = new byte[26];
    private int channel = 0;
    private double roll1 = 0, Ccd_data0_1 = 0;
    private double roll2 = 0, Ccd_data0_2 = 0;
    private double directionAngle = 0, totalToeIn = 0;

    private int check = 0;
    private String strRegisterNum;
    private String sel1, sel2, sel3, sel4;
    private SharedPreferences sharedDefining;
    private SharedPreferences sharedVehicle;
    private SharedPreferences sharedMeasure;
    SQLiteBurdenWheelHelpter dbHelper1, dbHelper2, dbHelper3, dbHelper4;
    SQLiteDatabase db1, db2, db3, db4;
    SQLiteSteeringWheelHelpter dbHelpers1, dbHelpers2, dbHelpers3, dbHelpers4;
    SQLiteDatabase dbs1, dbs2, dbs3, dbs4;
    private boolean isSave;

    private static int timeInterVal = CommonConstants.TIME_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toein_measure);
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
                alertDialog2.dismiss();
                Intent intent = new Intent(ToeinMeasureActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.setCanceledOnTouchOutside(false);
        alertDialog2.show();
    }


    private void initView() {
        imgBack = findViewById(R.id.img_back_main);
        btnSave = findViewById(R.id.btn_keep);
        tvTips = findViewById(R.id.tv_tips);
        tvRoll1 = findViewById(R.id.tv1_roll);
        tvRoll2 = findViewById(R.id.tv2_roll);
        tv_direction_angle = findViewById(R.id.tv_direction_angle);
        tv_total_toe_in = findViewById(R.id.tv_total_toe_in);
        roll_bg1 = findViewById(R.id.roll_bg1);
        roll_bg2 = findViewById(R.id.roll_bg2);
        direction_angle_bg = findViewById(R.id.direction_angle_bg);
        total_toe_in_bg = findViewById(R.id.total_toe_in_bg);
        tvT1 = findViewById(R.id.text_t1);
        tvT3 = findViewById(R.id.text_t3);

        imgBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        SharedPreferences sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
        float sharedStoreFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.TOE_IN_KEY, 0L);
        if (sharedStoreFloat != 0) {
            defaultInterval = sharedStoreFloat;
        }
        float sharedStorePitchFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.TOE_IN_PITCH_KEY, 0L);
        if (sharedStorePitchFloat != 0) {
            defaultPitch = sharedStorePitchFloat;
        }

        timeInterVal = sharedStore.getInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, CommonConstants.TIME_INTERVAL);

        openSocket();
        debug();
    }

    private void debug() {
        if (RunningContext.debug) {
            btnSave.setEnabled(true);
            roll1 = new Random().nextFloat();
            roll2 = new Random().nextFloat();
            totalToeIn = new Random().nextFloat();
        }
    }

    private void openSocket() {
        socket = socketUitls.openSocket();
        if (socket) {
            startSendThread();
            socketUitls.setOnDataReceiveListener(new SocketUitls.OnDataReceiveListener() {
                @Override
                public void onDataReceive(byte[] buffer, int size) {
                    data = buffer;
                    Log.i(TAG, "onDataReceive: " + UITools.bytesToHexString(data));
                    int msgType = UITools.getUnsignedByte(data[6]);
                    Log.i(TAG, "run: 消息类型-" + msgType);
                    if (msgType == 60) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isSave) {
                                    // 如果已经保存数据不再改变
                                    return;
                                }
                                final int ccd_data0_flag = UITools.getUnsignedByte(data[2]);
                                final int ccd_data1_flag = UITools.getUnsignedByte(data[3]);
                                final int roll_pitch_yaw_flag = UITools.getUnsignedByte(data[4]);
                                channel = UITools.getUnsignedByte(data[8]);
                                if (channel == 1) {
                                    if (ccd_data0_flag == 1) {
                                        Ccd_data0_1 = UITools.Ccd_data(UITools.M(data[14], data[15]));
                                        Log.i(TAG, "run: Ccd_data0_1-" + Ccd_data0_1);
                                        // 设置T1
                                        tvT1.setText(NumberUtils.double2Str(Ccd_data0_1));
                                    }
                                    if (roll_pitch_yaw_flag == 1) {
                                        roll1 = UITools.Roll(data[18], data[19]);
                                        tvRoll1.setText(NumberUtils.double2Str(roll1));
                                    }
                                } else if (channel == 2) {
                                    if (ccd_data0_flag == 1) {
                                        Ccd_data0_2 = UITools.Ccd_data(UITools.M(data[14], data[15]));
                                        Log.i(TAG, "run: Ccd_data0_2-" + Ccd_data0_2);
                                        // 设置T1
                                        tvT3.setText(NumberUtils.double2Str(Ccd_data0_2));
                                    }
                                    if (roll_pitch_yaw_flag == 1) {
                                        roll2 = UITools.Roll(data[18], data[19]);
                                        tvRoll2.setText(NumberUtils.double2Str(roll2));
                                    }
                                }

                                // 只有左右水平倾角在这个范围内0.3才能进行页面的计算
                                if (Math.abs(roll1) > defaultInterval || Math.abs(roll2) > defaultInterval) {
                                    // 不满足条件后设置
                                    tvTips.setText(getResources().getString(R.string.str_adjus_horizontal));
                                    btnSave.setEnabled(false);
                                    return;
                                }
                                // 方向角度
                                directionAngle = Ccd_data0_1 - Ccd_data0_2;
                                tv_direction_angle.setText(NumberUtils.double2Str(directionAngle));
                                // 总前束
                                totalToeIn = Ccd_data0_1 + Ccd_data0_2;
                                tv_total_toe_in.setText(NumberUtils.double2Str(totalToeIn));
                                if (Math.abs(directionAngle) <= defaultPitch) {
                                    direction_angle_bg.setBackgroundColor(getResources().getColor(R.color.coaxial_green));
                                } else {
                                    direction_angle_bg.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                }
                                if (Math.abs(totalToeIn) <= defaultPitch) {
                                    total_toe_in_bg.setBackgroundColor(getResources().getColor(R.color.coaxial_green));
                                } else {
                                    total_toe_in_bg.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                }

                                if (Math.abs(directionAngle) <= defaultPitch && Math.abs(totalToeIn) <= defaultPitch) {
                                    // 都满足条件后
                                    tvTips.setText(getResources().getString(R.string.str_save_data));
                                    btnSave.setEnabled(true);
                                } else {
                                    tvTips.setText(getResources().getString(R.string.str_adjus_horizontal));
                                    btnSave.setEnabled(false);
                                }
                            }
                        });
                    }
                }
            });
        } else {
            return;
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(ToeinMeasureActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_keep:
                isSave = true;
                UITools.showToast(ToeinMeasureActivity.this, R.string.str_save_successfully);
                saveData();
                break;
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
        String leftAngle = NumberUtils.double2Str(roll1);
        String rightAngle = NumberUtils.double2Str(roll2);
        String totalAngle = NumberUtils.double2Str(totalToeIn);
        ReportDataManager.setToeiAngle(key, leftAngle,rightAngle,totalAngle,
                adjustBeforeAfter.equals("before") ? true : false);
    }


    private class SendThread extends Thread {
        private boolean mRunning = false;

        private int msgWhat = 1;

        @Override
        public void run() {
            mRunning = true;
            Log.i(TAG, "run: 进入线程");
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

    private void startSendThread() {
        if (sendThread != null) {
            sendThread.close();
            sendThread = null;
        }
        sendThread = new SendThread();
        sendThread.start();
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
        socketUitls.closeSocket();
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
            Intent intent = new Intent(ToeinMeasureActivity.this, MeasureAdjustActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
