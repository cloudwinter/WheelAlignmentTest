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

import com.google.gson.Gson;
import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.ReportDataManager;
import com.inprintech.wheelalignmenttest.Model.KingpinModel;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.Constants;
import com.inprintech.wheelalignmenttest.Utils.NumberUtils;
import com.inprintech.wheelalignmenttest.Utils.SocketUitls;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.inprintech.wheelalignmenttest.common.CommonConstants;

import androidx.appcompat.app.AlertDialog;

/**
 * 主销测量
 */
public class KingpinMeasureActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "KingpinMeasureActivity";

    private double defaultInterval = 10;
    private double defaultPitch = 0.5;

    private ImageView imgBack;
    private TextView tvTab1, tvTab2;
    private TextView tvTips;

    // 设备1信息
    private LinearLayout layout;
    private TextView tvPitch, tvI,tvRoll, tvYaw;
    private TextView tvL0;
    private TextView tvP1, tvW1, tvY1;
    private TextView tvP2, tvW2, tvY2;
    private TextView tv_caster_angle, tv_camber, tv_max_steering_angle, tv_camber_angle;
    private Button btnAdjusting,btnDetermine, btnClear, btnSubmission;

    // 设备2信息
    private LinearLayout layout2;
    private TextView tv2Pitch,tv2I, tv2Roll, tv2Yaw;
    private TextView tv2L0;
    private TextView tv2P1, tv2W1, tv2Y1;
    private TextView tv2P2, tv2W2, tv2Y2;
    private TextView tv2_caster_angle, tv2_camber, tv2_max_steering_angle, tv2_camber_angle;
    private Button btn2Adjusting,btn2Determine, btn2Clear, btn2Submission;


    private SocketUitls socketUitls = new SocketUitls();
    private boolean socket;
    private SendThread sendThread;

    private byte[] data = new byte[26];
    private int channel = 0;
    private double roll1 = 0, pitch1 = 0, yaw1,i1 = 0;
    private double roll2 = 0, pitch2 = 0, yaw2,i2 = 0;
    private double l0_1 = 0;
    private double l0_2 = 0;
    private double p1_1 = 0, w1_1 = 0, y1_1 = 0, p1_2 = 0, w1_2 = 0, y1_2 = 0;
    private double p2_1 = 0, w2_1 = 0, y2_1 = 0, p2_2 = 0, w2_2 = 0, y2_2 = 0;
    private double a1 = 0;//后倾角
    private double b1 = 0;//内倾角
    private double r1 = 0;//最大转向角
    private double w1 = 0;//外倾角
    private double a2 = 0;//后倾角
    private double b2 = 0;//内倾角
    private double r2 = 0;//最大转向角
    private double w2 = 0;//外倾角

    private SharedPreferences sharedKingpin;
    private SharedPreferences.Editor editorKingpin;

    private int check = 0;
    private String strRegisterNum;
    private String sel1, sel2, sel3, sel4;
    private SharedPreferences sharedDefining;
    private SharedPreferences sharedVehicle;
    private SharedPreferences sharedMeasure;
    // 默认选中设备1，（1表示选中设备1，2选中表示设备2）
    private int selectTab = 1;

    private boolean isAdjusting1,isAdjusting2;

    private boolean isDetermine1_1, isDetermine1_2;
    private boolean isDetermine2_1, isDetermine2_2;

    private static int timeInterVal = CommonConstants.TIME_INTERVAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kingpin_measure);
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
                Intent intent = new Intent(KingpinMeasureActivity.this, MeasureAdjustActivity.class);
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
        imgBack = findViewById(R.id.img_back_main);
        tvTab1 = findViewById(R.id.tv_tab_channel_1);
        tvTab2 = findViewById(R.id.tv_tab_channel_2);
        tvTips = findViewById(R.id.tv_tips);
        imgBack.setOnClickListener(this);
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);

        initChannel1View();
        initChannel2View();
        selectTab(1);


        SharedPreferences sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
        float sharedStoreFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.KNGPIN_KEY, 0L);
        if (sharedStoreFloat != 0) {
            defaultInterval = sharedStoreFloat;
        }
        float sharedStorePitchFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.KNGPIN_PITCH_KEY, 0L);
        if (sharedStorePitchFloat != 0) {
            defaultPitch = sharedStorePitchFloat;
        }

        timeInterVal = sharedStore.getInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, CommonConstants.TIME_INTERVAL);

        initSocket();
    }


    private void initChannel1View() {
        layout = findViewById(R.id.layout_channel);
        tvI = findViewById(R.id.tv_I);
        tvPitch = findViewById(R.id.tv_pitch);
        tvRoll = findViewById(R.id.tv_roll);
        tvYaw = findViewById(R.id.tv_yaw);
        tvL0 = findViewById(R.id.tv_L0);
        tvP1 = findViewById(R.id.tv_p1);
        tvP2 = findViewById(R.id.tv_p2);
        tvW1 = findViewById(R.id.tv_w1);
        tvW2 = findViewById(R.id.tv_w2);
        tvY1 = findViewById(R.id.tv_y1);
        tvY2 = findViewById(R.id.tv_y2);
        tv_caster_angle = findViewById(R.id.tv_caster_angle);
        tv_camber = findViewById(R.id.tv_camber);
        tv_max_steering_angle = findViewById(R.id.tv_max_steering_angle);
        tv_camber_angle = findViewById(R.id.tv_camber_angle);
        btnAdjusting = findViewById(R.id.btn_adjusting);
        btnDetermine = findViewById(R.id.btn_determine);
        btnClear = findViewById(R.id.btn_clean);
        btnSubmission = findViewById(R.id.btn_submission);

        btnAdjusting.setOnClickListener(this);
        btnSubmission.setOnClickListener(this);
        btnDetermine.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    private void initChannel2View() {
        layout2 = findViewById(R.id.layout2_channel);
        tv2I = findViewById(R.id.tv2_I);
        tv2Pitch = findViewById(R.id.tv2_pitch);
        tv2Roll = findViewById(R.id.tv2_roll);
        tv2Yaw = findViewById(R.id.tv2_yaw);
        tv2L0 = findViewById(R.id.tv2_L0);
        tv2P1 = findViewById(R.id.tv2_p1);
        tv2P2 = findViewById(R.id.tv2_p2);
        tv2W1 = findViewById(R.id.tv2_w1);
        tv2W2 = findViewById(R.id.tv2_w2);
        tv2Y1 = findViewById(R.id.tv2_y1);
        tv2Y2 = findViewById(R.id.tv2_y2);
        tv2_caster_angle = findViewById(R.id.tv2_caster_angle);
        tv2_camber = findViewById(R.id.tv2_camber);
        tv2_max_steering_angle = findViewById(R.id.tv2_max_steering_angle);
        tv2_camber_angle = findViewById(R.id.tv2_camber_angle);
        btn2Adjusting = findViewById(R.id.btn2_adjusting);
        btn2Determine = findViewById(R.id.btn2_determine);
        btn2Clear = findViewById(R.id.btn2_clean);
        btn2Submission = findViewById(R.id.btn2_submission);

        btn2Adjusting.setOnClickListener(this);
        btn2Submission.setOnClickListener(this);
        btn2Determine.setOnClickListener(this);
        btn2Clear.setOnClickListener(this);
    }

    /**
     * 选中设备 1，2
     *
     * @param channel
     */
    private void selectTab(int channel) {
        tvTips.setText(getResources().getText(R.string.str_turn_wheel_left));
        if (channel == 1) {
            selectTab = 1;
            tvTab1.setSelected(true);
            tvTab2.setSelected(false);
            layout.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        } else if (channel == 2) {
            selectTab = 2;
            tvTab1.setSelected(false);
            tvTab2.setSelected(true);
            layout.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
    }


    private void initSocket() {
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
        public void onDataReceive(final byte[] buffer, int size) {
            data = buffer;
            Log.i(TAG, "onDataReceive: " + UITools.bytesToHexString(data));
            int msgType = UITools.getUnsignedByte(data[6]);
            Log.i(TAG, "run: 消息类型-" + msgType);
            if (msgType == 60) {
                int roll_pitch_yaw_flag = UITools.getUnsignedByte(data[4]);
                if (roll_pitch_yaw_flag == 0) {
                    // 添加无效数据验证
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        channel = UITools.getUnsignedByte(data[8]);
                        Log.i(TAG, "run: 通道-" + channel);
                        if (channel == 1) {
                            setChannel1(buffer);
                        } else if (channel == 2) {
                            setChannel2(buffer);

                        }
                    }
                });
            }
        }
    };

    private void setChannel2(byte[] dataParam) {
        roll2 = UITools.Roll(dataParam[18], dataParam[19]);
        pitch2 = UITools.Pitch(dataParam[20], dataParam[21]);
        yaw2 = UITools.Yaw(dataParam[22], dataParam[23]);
        tv2Pitch.setText(NumberUtils.double2Str(pitch2));
        tv2Roll.setText(NumberUtils.double2Str(roll2));
        tv2Yaw.setText(NumberUtils.double2Str(yaw2));

        if (!isAdjusting2) {
            // 未校准
            i2 = yaw2;
            tv2I.setText(NumberUtils.double2Str(i2));
            return;
        } else {
            // 校准过
            i2 = calculateIAfterAdjusting(yaw2,l0_2);
            tv2I.setText(NumberUtils.double2Str(i2));
        }

        Log.d(TAG, "roll2:" + roll2 + " pitch2:" + pitch2 + " yaw2:" + yaw2 + " l0_2:" + l0_2 + " i2:" + i2);

        if (!isDetermine2_1) {
            // 第一次进入时，还未点击确定
            if (i2 >= defaultInterval) {
                p2_1 = pitch2;
                w2_1 = roll2;
                y2_1 = i2;
                btn2Determine.setEnabled(true);
                determine2();
            } else {
                btn2Determine.setEnabled(false);
            }
        } else if (isDetermine2_1 && !isDetermine2_2) {
            // 第一次已经确定，
            if (isInterval(i2, -p2_1, defaultPitch)) {
                p2_2 = pitch2;
                w2_2 = roll2;
                y2_2 = i2;
                btn2Determine.setEnabled(true);
                determine2();
            } else {
                btn2Determine.setEnabled(false);
            }
        }
    }

    private void setChannel1(byte[] dataParam) {
        roll1 = UITools.Roll(dataParam[18], dataParam[19]);
        pitch1 = UITools.Pitch(dataParam[20], dataParam[21]);
        yaw1 = UITools.Yaw(dataParam[22], dataParam[23]);
        tvPitch.setText(NumberUtils.double2Str(pitch1));
        tvRoll.setText(NumberUtils.double2Str(roll1));
        tvYaw.setText(NumberUtils.double2Str(yaw1));

        if (!isAdjusting1) {
            // 未校准
            i1 = yaw1;
            tvI.setText(NumberUtils.double2Str(i1));
            return;
        } else {
            // 校准过
//            i1 = yaw1 - l0_1;
            i1 = calculateIAfterAdjusting(yaw1,l0_1);
            tvI.setText(NumberUtils.double2Str(i1));
        }

        Log.d(TAG, "roll1:" + roll1 + " pitch1:" + pitch1 + " yaw1:" + yaw1 + " l0_1:" + l0_1 + " i1:" + i1);

        if (!isDetermine1_1) {
            // 第一次进入时，还未点击确定
            if (i1 >= defaultInterval) {
                p1_1 = pitch1;
                w1_1 = roll1;
                y1_1 = i1;
                determine1();
                btnDetermine.setEnabled(true);
            } else {
                btnDetermine.setEnabled(false);
            }
        } else if (isDetermine1_1 && !isDetermine1_2) {
            // 第一次已经确定，
            if (isInterval(i1, -p1_1, defaultPitch)) {
                p1_2 = pitch1;
                w1_2 = roll1;
                y1_2 = i1;
                btnDetermine.setEnabled(true);
                determine1();
            } else {
                btnDetermine.setEnabled(false);
            }
        }
    }

    /**
     * 设置清楚之后计算同轴补偿的计算结果
     * @param param_yaw
     * @param param_l0
     * @return
     */
    private double calculateIAfterAdjusting(double param_yaw,double param_l0) {
        double result_i = param_yaw - param_l0;
        if (result_i > 180) {
            result_i = param_yaw + param_l0 + 180;
        } else if (result_i < -180) {
            result_i = param_yaw + param_l0 - 180;
        }
        return result_i;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(KingpinMeasureActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_tab_channel_1:
                selectTab(1);
                break;
            case R.id.tv_tab_channel_2:
                selectTab(2);
                break;
            case R.id.btn_adjusting:
                if (!isAdjusting1) {
                    isAdjusting1 = true;
                    l0_1 = i1;
                    tvL0.setText(NumberUtils.double2Str(l0_1));
                    btnAdjusting.setEnabled(false);
                }
                break;
            case R.id.btn2_adjusting:
                if (!isAdjusting2) {
                    isAdjusting2 = true;
                    l0_2 = i2;
                    tv2L0.setText(NumberUtils.double2Str(l0_2));
                    btn2Adjusting.setEnabled(false);
                }
                break;
            case R.id.btn_determine:
                determine1();
                break;
            case R.id.btn2_determine:
                determine2();
            case R.id.btn_clean:
                clear1();
                break;
            case R.id.btn2_clean:
                clear2();
                break;
            case R.id.btn_submission:
                UITools.showToast(KingpinMeasureActivity.this, R.string.str_save_successfully);
                commitKingpinToShared(1);
//                // 启动设备2
//                selectTab(2);
                break;
            case R.id.btn2_submission:
                UITools.showToast(KingpinMeasureActivity.this, R.string.str_save_successfully);
                commitKingpinToShared(2);
                // 跳转到综合展示
                Intent intents = new Intent(KingpinMeasureActivity.this, DisplayAllActivity.class);
                startActivity(intents);
                break;
        }
    }

    private void determine2() {
        if (!isDetermine2_1) {
            // 表示第一次点击确定
            isDetermine2_1 = true;
            tv2P1.setText(NumberUtils.double2Str(p2_1));
            tv2W1.setText(NumberUtils.double2Str(w2_1));
            tv2Y1.setText(NumberUtils.double2Str(y2_1));
            tvTips.setText(getResources().getText(R.string.str_turn_wheel_right));
            btn2Determine.setEnabled(false);
            playMusic();
        } else if (isDetermine2_1 && !isDetermine2_2) {
            // 表示第二次点击
            isDetermine2_2 = true;
            tv2P2.setText(NumberUtils.double2Str(p2_2));
            tv2W2.setText(NumberUtils.double2Str(w2_2));
            tv2Y2.setText(NumberUtils.double2Str(y2_2));
            btn2Determine.setEnabled(false);
            // 计算倾角数据
            abrw2();
            btn2Submission.setEnabled(true);
            playMusic();
        }
    }

    private void determine1() {
        if (!isDetermine1_1) {
            // 表示第一次点击确定
            isDetermine1_1 = true;
            tvP1.setText(NumberUtils.double2Str(p1_1));
            tvW1.setText(NumberUtils.double2Str(w1_1));
            tvY1.setText(NumberUtils.double2Str(y1_1));
            tvTips.setText(getResources().getText(R.string.str_turn_wheel_right));
            btnDetermine.setEnabled(false);
            playMusic();
        } else if (isDetermine1_1 && !isDetermine1_2) {
            // 表示第二次点击
            isDetermine1_2 = true;
            tvP2.setText(NumberUtils.double2Str(p1_2));
            tvW2.setText(NumberUtils.double2Str(w1_2));
            tvY2.setText(NumberUtils.double2Str(y1_2));
            btnDetermine.setEnabled(false);
            // 计算倾角数据
            abrw1();
            btnSubmission.setEnabled(true);
            playMusic();
        }
    }

    private void clear1() {
        btnAdjusting.setEnabled(true);
        isAdjusting1 = false;
        tvL0.setText("");
        // 设置按钮不可点击
        btnDetermine.setEnabled(false);
        isDetermine1_1 = false;
        isDetermine1_2 = false;
        tvP1.setText("");
        tvW1.setText("");
        tvY1.setText("");
        tvP2.setText("");
        tvW2.setText("");
        tvY2.setText("");
        tvTips.setText(getResources().getText(R.string.str_turn_wheel_left));
        tv_caster_angle.setText("");
        tv_camber.setText("");
        tv_max_steering_angle.setText("");
        tv_camber_angle.setText("");
    }

    private void clear2() {
        btn2Adjusting.setEnabled(true);
        isAdjusting2 = false;
        tv2L0.setText("");
        // 设置按钮不可点击
        btn2Determine.setEnabled(false);
        isDetermine1_1 = false;
        isDetermine1_2 = false;
        tv2P1.setText("");
        tv2W1.setText("");
        tv2Y1.setText("");
        tv2P2.setText("");
        tv2W2.setText("");
        tv2Y2.setText("");
        tvTips.setText(getResources().getText(R.string.str_turn_wheel_left));
        tv2_caster_angle.setText("");
        tv2_camber.setText("");
        tv2_max_steering_angle.setText("");
        tv2_camber_angle.setText("");
    }

    private void abrw1() {
        a1 = UITools.A(p1_1, p1_2, i1);
        b1 = UITools.B(w1_1, w1_2, i1);
        r1 = Math.abs(i1);
        w1 = Math.abs(w1_1 - w1_2);
        tv_caster_angle.setText(NumberUtils.double2Str(a1));
        tv_camber.setText(NumberUtils.double2Str(b1));
        tv_max_steering_angle.setText(NumberUtils.double2Str(r1));
        tv_camber_angle.setText(NumberUtils.double2Str(w1));
    }

    private void abrw2() {
        a2 = UITools.A(p2_1, p2_2, i2);
        b2 = UITools.B(w2_1, w2_2, i2);
        r2 = Math.abs(i2);
        w2 = Math.abs(w2_1 - w2_2);

        tv2_caster_angle.setText(NumberUtils.double2Str(a2));
        tv2_camber.setText(NumberUtils.double2Str(b2));
        tv2_max_steering_angle.setText(NumberUtils.double2Str(r2));
        tv2_camber_angle.setText(NumberUtils.double2Str(w2));

    }

    private void commitKingpinToShared(int channel) {
        sharedKingpin = getSharedPreferences("Kingpin", MODE_PRIVATE);
        editorKingpin = sharedKingpin.edit();
        KingpinModel kingpinModel = new KingpinModel();
        kingpinModel.channel = channel;
        if (channel == 1) {
            kingpinModel.casterAngle = NumberUtils.double2Str(a1);
            kingpinModel.inAngle = NumberUtils.double2Str(b1);
            kingpinModel.maxSteeringAngle = NumberUtils.double2Str(r1);
            kingpinModel.camberAngle = NumberUtils.double2Str(w1);
        } else if (channel == 2) {
            kingpinModel.casterAngle = NumberUtils.double2Str(a2);
            kingpinModel.inAngle = NumberUtils.double2Str(b2);
            kingpinModel.maxSteeringAngle = NumberUtils.double2Str(r2);
            kingpinModel.camberAngle = NumberUtils.double2Str(w2);
        }
        Gson gson = new Gson();
        String modelJson = gson.toJson(kingpinModel);
        editorKingpin.putString(channel + "", modelJson);
        editorKingpin.commit();
        saveData(kingpinModel);
    }


    /**
     * 保存数据到报表
     */
    private void saveData(KingpinModel kingpinModel) {
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
        ReportDataManager.setKinpin(key, kingpinModel,
                adjustBeforeAfter.equals("before") ? true : false);
    }

    private void playMusic() {
        MediaPlayer music = MediaPlayer.create(this, R.raw.kring);
        music.start();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(KingpinMeasureActivity.this, MeasureAdjustActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

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
        initSocket();
        super.onRestart();
    }
}
