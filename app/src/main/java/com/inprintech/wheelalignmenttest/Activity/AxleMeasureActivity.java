package com.inprintech.wheelalignmenttest.Activity;

import android.content.ContentValues;
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
 * 车桥测量
 */
public class AxleMeasureActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AxleMeasureActivity";

    private double defaultInterval = 0.5;

    private ImageView imgBack;
    private Button btnSave;
    private TextView tvAxle;
    private TextView tvAxleBg;

    private SocketUitls socketUitls = new SocketUitls();
    private boolean socket;
    private SendThread sendThread;

    private byte[] data = new byte[26];
    private int channel = 0;
    private double Ccd_data1_1 = 0, Ccd_data1_2 = 0;
    private double axle = 0;

    SharedPreferences shareAxle;
    SharedPreferences.Editor editorAxle;

    private int check = 0;
    private String strRegisterNum;
    private SharedPreferences sharedVehicle;
    private SharedPreferences sharedMeasure;
    SQLiteBurdenWheelHelpter dbHelper1, dbHelper2, dbHelper3, dbHelper4;
    SQLiteDatabase db1, db2, db3, db4;
    private boolean isSave;

    private static int timeInterVal = CommonConstants.TIME_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_axle_measure);
        showSingleAlertDialog();
        initView();
        debug();
    }

    private void debug() {
        if (RunningContext.debug) {
            btnSave.setEnabled(true);
            axle = new Random().nextDouble();
        }
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
                Intent intent = new Intent(AxleMeasureActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.setCanceledOnTouchOutside(false);
        alertDialog2.show();
    }

    private void initView() {
        btnSave = findViewById(R.id.btn_save);
        imgBack = findViewById(R.id.img_back_main);
        tvAxle = findViewById(R.id.tv_axle);
        tvAxleBg = findViewById(R.id.axle_bg);

        imgBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        SharedPreferences sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
        float sharedFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.AXLE_KEY, 0L);
        if (sharedFloat != 0) {
            defaultInterval = sharedFloat;
        }

        timeInterVal = sharedStore.getInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, CommonConstants.TIME_INTERVAL);

        openSocket();
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
                    if (msgType != 60) {
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
                                channel = UITools.getUnsignedByte(data[8]);
                                if (channel == 1) {
                                    Ccd_data1_1 = UITools.Ccd_data(UITools.M(data[16], data[17]));
                                    Log.i(TAG, "run: Ccd_data1_1-" + Ccd_data1_1);

                                } else if (channel == 2) {
                                    Ccd_data1_2 = UITools.Ccd_data(UITools.M(data[16], data[17]));
                                    Log.i(TAG, "run: Ccd_data1_1-" + Ccd_data1_2);

                                }
                                if (isSave) {
                                    // 如果保存后数据不再改变
                                    return;
                                }
                                axle = Ccd_data1_1 - Ccd_data1_2;
                                tvAxle.setText(NumberUtils.double2Str(axle));
                                // FIXME 区间值待定
                                if (Math.abs(axle) <= defaultInterval) {
                                    tvAxleBg.setBackgroundColor(getResources().getColor(R.color.coaxial_green));
                                } else {
                                    tvAxleBg.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                }
                            }
                    });
                }
            });
        } else {
            return;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(AxleMeasureActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_save:
                isSave = true;
                UITools.showToast(AxleMeasureActivity.this, R.string.str_save_successfully);
                saveData();
                break;
        }
    }


    /**
     * 保存数据到报表
     */
    private void saveData() {
        // 车辆信息
        sharedVehicle = getSharedPreferences("Vehicle_Info", MODE_PRIVATE);
        strRegisterNum = sharedVehicle.getString("registerNum", "");
        // 轮子信息（1，2，3，4）
        sharedMeasure = getSharedPreferences("check_measure", MODE_PRIVATE);
        check = sharedMeasure.getInt("check", 0);
        String key = ReportDataManager.genKey(strRegisterNum, check + "");
        ReportDataManager.setAxleValue(key, NumberUtils.double2Str(axle),
                adjustBeforeAfter.equals("before") ? true : false);
    }

    /**
     * 选择数据库-表
     */
    private int selDB_TB() {
        long rowid = 0;
        sharedVehicle = getSharedPreferences("Vehicle_Info", MODE_PRIVATE);
        sharedMeasure = getSharedPreferences("check_measure", MODE_PRIVATE);
        check = sharedMeasure.getInt("check", 0);
        strRegisterNum = sharedVehicle.getString("registerNum", "");
        switch (check) {
            case 1:
                dbHelper1 = new SQLiteBurdenWheelHelpter(AxleMeasureActivity.this,
                        dbHelper1.DB_NAME, dbHelper1.TB_NAME1, null, 1);
                db1 = dbHelper1.getWritableDatabase();// 打开数据库
                ContentValues values1 = new ContentValues();
                values1.put("register_num", strRegisterNum);
                values1.put("measure_state", adjustBeforeAfter);
                values1.put("axle_value", axle);
                rowid = db1.insert(dbHelper1.TB_NAME1, null, values1);
                break;
            case 2:
                dbHelper2 = new SQLiteBurdenWheelHelpter(AxleMeasureActivity.this,
                        dbHelper2.DB_NAME, dbHelper2.TB_NAME2, null, 1);
                db2 = dbHelper2.getWritableDatabase();// 打开数据库
                ContentValues values2 = new ContentValues();
                values2.put("register_num", strRegisterNum);
                values2.put("measure_state", adjustBeforeAfter);
                values2.put("axle_value", axle);
                rowid = db1.insert(dbHelper2.TB_NAME2, null, values2);
                break;
            case 3:
                dbHelper3 = new SQLiteBurdenWheelHelpter(AxleMeasureActivity.this,
                        dbHelper3.DB_NAME, dbHelper3.TB_NAME3, null, 1);
                db3 = dbHelper3.getWritableDatabase();// 打开数据库
                ContentValues values3 = new ContentValues();
                values3.put("register_num", strRegisterNum);
                values3.put("measure_state", adjustBeforeAfter);
                values3.put("axle_value", axle);
                rowid = db3.insert(dbHelper3.TB_NAME3, null, values3);
                break;
            case 4:
                dbHelper4 = new SQLiteBurdenWheelHelpter(AxleMeasureActivity.this,
                        dbHelper4.DB_NAME, dbHelper4.TB_NAME4, null, 1);
                db4 = dbHelper4.getWritableDatabase();// 打开数据库
                ContentValues values4 = new ContentValues();
                values4.put("register_num", strRegisterNum);
                values4.put("measure_state", adjustBeforeAfter);
                values4.put("axle_value", axle);
                rowid = db4.insert(dbHelper4.TB_NAME4, null, values4);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rowid);
        }
        return (int) rowid;
    }

    private void startSendThread() {
        if (sendThread != null) {
            sendThread.close();
            sendThread = null;
        }
        sendThread = new SendThread();
        sendThread.start();
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
        if (socket) {
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
            Intent intent = new Intent(AxleMeasureActivity.this, MeasureAdjustActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
