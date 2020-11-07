package com.inprintech.wheelalignmenttest.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.Constants;
import com.inprintech.wheelalignmenttest.Utils.NumberUtils;
import com.inprintech.wheelalignmenttest.Utils.SocketUitls;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.inprintech.wheelalignmenttest.common.CommonConstants;
import com.inprintech.wheelalignmenttest.core.RunningContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

/**
 * 同轴调整
 */
public class CoaxialAdjustActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CoaxialAdjustActivity";


    private static final int MSG_SET_CHANNEL1 = 107;
    private static final int MSG_SET_CHANNEL2 = 108;

    private double defaultInterval = 0.3;

    private ImageView imgBack;
    private LinearLayout llRight, llLeft;
    //Left
    private TextView tv_horizontal_angle, tv_reference_value, tv_compensate,tv_tips;
    private Button btnSave, btnClear;
    private View viewCoaxialBg;
    //Right
    private TextView tv_horizontal_angle_r, tv_reference_value_r, tv_compensate_r,tv_tips_r;
    private Button btnSave_r, btnClear_r;
    private View viewCoaxialBg_r;


    private SocketUitls socketUitls = new SocketUitls();
    private boolean socket;
    private sendThread1 send1;
    private sendThread2 send2;

    private byte[] data = new byte[26];
    private int channel = 0;
    private double Roll_left = 0, Roll_right = 0;
    private double Pitch_left = 0, Pitch_right = 0;
    private double Y = 0, Z = 0;
    private double reference_value_left = 0, p2_left = 0, reference_value_right = 0, p2_right = 0;
    private double horizontal_angle_left = 0, horizontal_angle_right = 0;
    private double compensate_left = 0, compensate_right = 0;
    private int j = 0, j2 = 0;
    private boolean isKeepLeft, isKeepRight;

    private boolean isSendChannel2;

    private static int timeInterVal = CommonConstants.TIME_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaxial_adjust);
        // 注释同轴调整，调整前后选择
        //showSingleAlertDialog();
        initView();
    }

    private void initView() {
        viewCoaxialBg = findViewById(R.id.view_coaxial_bg);
        btnSave = findViewById(R.id.btn_keep);
        btnClear = findViewById(R.id.btn_clean);
        llLeft = findViewById(R.id.ll_left);
        llRight = findViewById(R.id.ll_right);
        tv_horizontal_angle = findViewById(R.id.tv_horizontal_angle);
        tv_reference_value = findViewById(R.id.tv_reference_value);
        tv_compensate = findViewById(R.id.tv_compensate_left);
        tv_tips = findViewById(R.id.tv_tips_left);
        tv_tips_r = findViewById(R.id.tv_tips_right);
        imgBack = findViewById(R.id.img_back_main);
        viewCoaxialBg_r = findViewById(R.id.view_coaxial_bg_right);
        btnSave_r = findViewById(R.id.btn_keep_right);
        btnClear_r = findViewById(R.id.btn_clean_right);
        tv_horizontal_angle_r = findViewById(R.id.tv_horizontal_angle_right);
        tv_reference_value_r = findViewById(R.id.tv_reference_value_right);
        tv_compensate_r = findViewById(R.id.tv_compensate_right);

        imgBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnSave_r.setOnClickListener(this);
        btnClear_r.setOnClickListener(this);

        tv_reference_value.setText("0");
        tv_reference_value_r.setText("0");


        SharedPreferences sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
        float sharedCoaxialFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.COAXIAL_KEY, 0L);
        if (sharedCoaxialFloat != 0) {
            defaultInterval = sharedCoaxialFloat;
        }

        timeInterVal = sharedStore.getInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, CommonConstants.TIME_INTERVAL);

        openSocket();
    }

    private void openSocket() {
        socket = socketUitls.openSocket();
        if (socket) {
            getSend1();
            socketUitls.setOnDataReceiveListener(receiveListener);
        } else {
            return;
        }
    }



    private void setChannel2(byte[] dataParam) {
        Roll_right = UITools.Roll(dataParam[18], dataParam[19]);
        Log.i(TAG, "run: Roll_right-" + Roll_right);
        Pitch_right = UITools.Pitch(dataParam[20], dataParam[21]);
        Log.i(TAG, "run: Pitch_left-" + Pitch_right);
        // 设置左侧水平倾角（一直变化）
        tv_horizontal_angle_r.setText(NumberUtils.double2Str(Roll_right));

        if (reference_value_right == 0) {
            // 未设置过基准值
            compensate_right = Pitch_right;
        } else {
            // 设置过了基准值
            // modify 2020-08-24 显示pitch
            compensate_right = Pitch_right;
            //compensate_right = calculateCompensateAfterClear(Pitch_right,reference_value_right);
        }
        // modify 2020-09-07 一直显示pitch值
        tv_compensate_r.setText(NumberUtils.double2Str(compensate_right));

//        if (isKeepRight) {
//            // 确认过之后显示/2的结果
//            tv_compensate_r.setText(NumberUtils.double2Str(compensate_right / 2));
//        } else {
//            // 设置同轴补偿
//            tv_compensate_r.setText(NumberUtils.double2Str(compensate_right));
//        }
        //如果设置了基准值并且和水平倾角 如果差距在+-0.3之间显示绿色
        if (Math.abs(compensate_right) <= defaultInterval) {
            viewCoaxialBg_r.setBackgroundColor(getResources().getColor(R.color.coaxial_green));
        } else {
            viewCoaxialBg_r.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }

    private void setChannel1(byte[] dataParam) {
        Roll_left = UITools.Roll(dataParam[18], dataParam[19]);
        Log.i(TAG, "run: Roll_left-" + Roll_left);
        Pitch_left = UITools.Pitch(dataParam[20], dataParam[21]);
        Log.i(TAG, "run: Pitch_left-" + Pitch_left);
        // 设置左侧水平倾角（一直变化）
        tv_horizontal_angle.setText(NumberUtils.double2Str(Roll_left));

        if (reference_value_left == 0) {
            // 未设置过基准值
            compensate_left = Pitch_left;
        } else {
            // 设置过了基准值
            // modify 2020-08-24 显示pitch
            compensate_left = Pitch_left;
            //compensate_left = calculateCompensateAfterClear(Pitch_left,reference_value_left);
        }
        // 设置同轴补偿
        // modify 2020-09-07 一直显示pitch值
        tv_compensate.setText(NumberUtils.double2Str(compensate_left));

//        if (isKeepLeft) {
//            // 确认过之后显示/2的结果
//            tv_compensate.setText(NumberUtils.double2Str(compensate_left / 2));
//
//        } else {
//            // 设置同轴补偿
//            tv_compensate.setText(NumberUtils.double2Str(compensate_left));
//        }

        //如果设置了基准值并且和水平倾角 如果差距在+-0.3之间显示绿色
        if (Math.abs(compensate_left) <= defaultInterval) {
            viewCoaxialBg.setBackgroundColor(getResources().getColor(R.color.coaxial_green));
        } else {
            viewCoaxialBg.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
    }


    /**
     * 设置清楚之后计算同轴补偿的计算结果
     * @param pitch
     * @param reference_value
     * @return
     */
    private double calculateCompensateAfterClear(double pitch,double reference_value) {
        double compensate = pitch - reference_value;
        if (compensate > 180) {
            compensate = pitch + reference_value + 180;
        } else if (compensate < -180) {
            compensate = pitch + reference_value - 180;
        }
        return compensate;
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
                Intent intent = new Intent(CoaxialAdjustActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                alertDialog2.dismiss();
            }
        });

        alertDialog2 = alertBuilder.create();
        alertDialog2.setCanceledOnTouchOutside(false);
        alertDialog2.show();
    }

    private void getSend1() {
        send1 = new sendThread1();
        send1.start();
    }

    private void getSend2() {
        send2 = new sendThread2();
        send2.start();
        isSendChannel2 = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(CoaxialAdjustActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_keep:
                if (reference_value_left == 0) {
                    UITools.showToast(CoaxialAdjustActivity.this, R.string.str_coaxial_adjust_save_error);
                    break;
                }
                p2_left = Pitch_left;
                tv_reference_value.setText(NumberUtils.double2Str((reference_value_left+p2_left)/2));
                isKeepLeft = true;
                getSend2();
                llRight.setVisibility(View.VISIBLE);
                playMusic();
                break;
            case R.id.btn_clean:
                // 点击清除设置基准值
//                String referenceValue = tv_horizontal_angle.getText().toString();
                reference_value_left = Pitch_left;
                tv_reference_value.setText(NumberUtils.double2Str(reference_value_left));
                tv_tips.setVisibility(View.VISIBLE);
                // 重置保存状态
                isKeepLeft = false;
                playMusic();
                break;
            case R.id.btn_keep_right:
                if (reference_value_right == 0) {
                    UITools.showToast(CoaxialAdjustActivity.this, R.string.str_coaxial_adjust_save_error);
                    break;
                }
                p2_right = Pitch_right;
                tv_reference_value_r.setText(NumberUtils.double2Str((reference_value_right+p2_right)/2));
                isKeepRight = true;
                UITools.showToast(CoaxialAdjustActivity.this, R.string.str_save_successfully);
                playMusic();
                break;
            case R.id.btn_clean_right:
                // 点击清除设置基准值
                reference_value_right = Pitch_right;
                tv_reference_value_r.setText(NumberUtils.double2Str(reference_value_right));
                // 重置保存状态
                isKeepRight = false;
                playMusic();
                break;
        }
    }


    private void playMusic() {
        MediaPlayer music = MediaPlayer.create(this, R.raw.lting);
        music.start();
    }

    private class sendThread1 extends Thread {
        private boolean mRunning = false;

        @Override
        public void run() {
            mRunning = true;
            Log.i(TAG, "run: 进入线程");
            super.run();
            while (mRunning) {
                UITools.sleeoTime(timeInterVal);
                sendHandler.sendEmptyMessage(1);
                Log.d(TAG, "run: 发送设备1信号");
            }
        }

        public void close() {
            mRunning = false;
        }
    }

    private class sendThread2 extends Thread {
        private boolean mRunning = false;

        @Override
        public void run() {
            mRunning = true;
            Log.i(TAG, "run: 进入线程");
            super.run();
            while (mRunning) {
                UITools.sleeoTime(timeInterVal);
                sendHandler.sendEmptyMessage(2);
                Log.d(TAG, "run: 发送设备2信号");
            }
        }

        public void close() {
            mRunning = false;
        }
    }

    private Handler sendHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    socketUitls.sendOrder(Constants.measure());
                    break;
                case 2:
                    socketUitls.sendOrder(Constants.measure2());
                    break;
                case MSG_SET_CHANNEL1:
                    setChannel1((byte[]) msg.obj);
                    break;
                case MSG_SET_CHANNEL2:
                    setChannel2((byte[]) msg.obj);
                    break;
            }
        }
    };



    private SocketUitls.OnDataReceiveListener receiveListener = new SocketUitls.OnDataReceiveListener() {
        @Override
        public void onDataReceive(byte[] buffer, int size) {
            data = buffer;
            Log.i(TAG, "onDataReceive: " + UITools.bytesToHexString(data));
            int msgType = UITools.getUnsignedByte(data[6]);
            Log.i(TAG, "run: 消息类型-" + msgType);
            if (msgType == 60) {
                int roll_pitch_yaw = UITools.getUnsignedByte(data[4]);
                if (roll_pitch_yaw == 0) {
                    // 无效数据
                    return;
                }
                channel = UITools.getUnsignedByte(data[8]);
                Log.i(TAG, "run: 通道-" + channel);
                if (channel == 1) {
                    sendHandler.sendMessage(sendHandler.obtainMessage(MSG_SET_CHANNEL1,data));
                } else if (channel == 2) {
                    sendHandler.sendMessage(sendHandler.obtainMessage(MSG_SET_CHANNEL2,data));
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        socketUitls.closeSocket();
        if (send1 != null) {
            send1.close();
        }
        if (send2 != null) {
            send2.close();
        }
    }

    @Override
    protected void onRestart() {
        openSocket();
        if (isSendChannel2) {
            RunningContext.threadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(timeInterVal);
                        getSend2();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sendHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(CoaxialAdjustActivity.this, MeasureAdjustActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
