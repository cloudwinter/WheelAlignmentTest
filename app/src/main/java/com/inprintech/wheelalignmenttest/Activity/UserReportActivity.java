package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.ReportDataManager;
import com.inprintech.wheelalignmenttest.Model.ReportModel;
import com.inprintech.wheelalignmenttest.Model.ReportValue;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.ActivityCollector;
import com.inprintech.wheelalignmenttest.Utils.SavePhoto;
import com.inprintech.wheelalignmenttest.Utils.UITools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户报表
 */
public class UserReportActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "UserReportActivity";

    private static final String PACKNAME = "com.inprintech.wheelalignmenttest";
    private static final String DEFTYPE = "id";

    private ImageView imgBack;
    private ImageView imgQm;
    private TextView tvRegisterNum, tvDate, tvCustomerInfo, tvVehicleType, tvMileage;
    private Button btnPrinting;
    private LinearLayout llReportForm;
    private LinearLayout llToTurnTo1, llToTurnTo2, llToTurnTo3, llToTurnTo4;
    private LinearLayout llBurdenWheel1, llBurdenWheel2, llBurdenWheel3, llBurdenWheel4;
    private TextView tv_wheel_camber_a,tv_wheel_camber_b,tv_front_beam_a,tv_front_beam_b;

    private byte[] btAutograph;
    private String sel1, sel2, sel3, sel4;
    private String strRegisterNum, strDate, strCustomerInfo, strVehicleType, strMileage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_report);

        initView();
    }

    private void initView() {
        btnPrinting = findViewById(R.id.btn_printing);
        imgQm = findViewById(R.id.img_qm);
        imgBack = findViewById(R.id.img_back_main);
        tvRegisterNum = findViewById(R.id.tv_register_number);
        tvDate = findViewById(R.id.tv_date);
        tvCustomerInfo = findViewById(R.id.tv_customer_info);
        tvVehicleType = findViewById(R.id.tv_vehicle_type);
        tvMileage = findViewById(R.id.tv_mileage);
        llReportForm = findViewById(R.id.ll_report_form);
        llToTurnTo1 = findViewById(R.id.ll_to_turn_to_1);
        llToTurnTo2 = findViewById(R.id.ll_to_turn_to_2);
        llToTurnTo3 = findViewById(R.id.ll_to_turn_to_3);
        llToTurnTo4 = findViewById(R.id.ll_to_turn_to_4);
        llBurdenWheel1 = findViewById(R.id.ll_burden_wheel_1);
        llBurdenWheel2 = findViewById(R.id.ll_burden_wheel_2);
        llBurdenWheel3 = findViewById(R.id.ll_burden_wheel_3);
        llBurdenWheel4 = findViewById(R.id.ll_burden_wheel_4);

        imgBack.setOnClickListener(this);
        btnPrinting.setOnClickListener(this);
        getIntentData();
        dataShow(sel1,sel2,sel3,sel4);

        setDate();
    }

    private void setDate() {
        String key = null;
        for (int i = 1; i < 4; i++) {
            key = ReportDataManager.genKey(strRegisterNum, i+"");
            ReportModel reportModel = ReportDataManager.getReportModel(key);
            if (reportModel == null) {
                continue;
            }

            boolean isTurn = isTurn(i);
            // 左侧
            // 外倾角
            setValue("tv" + i + "_wheel_camber", reportModel.getLeftCamberAngle(), isTurn,false);
            // 前束角
            setValue("tv" + i + "_front_beam", reportModel.getLeftToeinAngle(), isTurn,false);
            // 主销后倾角
            setValue("tv" + i + "_kingpin_backward", reportModel.getLeftKingpinCasterAngle(), isTurn,false);
            // 主销内倾角
            setValue("tv" + i + "_kingpin_backward", reportModel.getLeftKingpinInAngle(), isTurn,false);

            // 右侧
            // 外倾角
            setValue("tv" + i + "_wheel_camber", reportModel.getRightCamberAngle(), isTurn,true);
            // 前束角
            setValue("tv" + i + "_front_beam", reportModel.getRightToeinAngle(), isTurn,true);
            // 主销后倾角
            setValue("tv" + i + "_kingpin_backward", reportModel.getRightKingpinCasterAngle(), isTurn,true);
            // 主销内倾角
            setValue("tv" + i + "_kingpin_backward", reportModel.getRightKingpinInAngle(), isTurn, true);

            // 总前束
            setValue("tv" + i + "_total_front_beam", reportModel.getTotalToein(), isTurn,false);

            // 同步值
            setValue("tv" + i + "_sync_value", reportModel.getSyncValue(), isTurn,false);

            // 车桥值
            setValue("tv" + i + "_axle_survey", reportModel.getAxleValue(), false,false);



        }
    }


    private boolean isTurn(int i) {
        String select = getIntent().getStringExtra("sel" + i);
        if (select.equals("select" + i + "_1")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 设置数据
     * @param name
     * @param value
     * @param right 是否是右侧
     */
    private void setValue(String name, ReportValue value, boolean isTurn, boolean right) {
        if (value == null) {
            return;
        }

        if (isTurn) {
            // 如果是转向
            String name_a = right ? (name + "_a2") : (name + "_a");
            String name_b = right ? (name + "_b2") : (name + "_b");
            int a_id = getResources().getIdentifier(name_a, DEFTYPE, PACKNAME);
            int b_id = getResources().getIdentifier(name_b, DEFTYPE, PACKNAME);
            TextView a_view = findViewById(a_id);
            TextView b_view = findViewById(b_id);
            a_view.setText(value.getAdjustBefore());
            b_view.setText(value.getAdjustAfter());
        } else {
            String name_bw_a = right ? (name + "_bw_a2") : (name + "_bw_a");
            String name_bw_b = right ? (name + "_bw_b2") : (name + "_bw_b");
            int bw_a_id = getResources().getIdentifier(name_bw_a, DEFTYPE, PACKNAME);
            int bw_b_id = getResources().getIdentifier(name_bw_b, DEFTYPE, PACKNAME);
            TextView bw_a_view = findViewById(bw_a_id);
            TextView bw_b_view = findViewById(bw_b_id);
            bw_a_view.setText(value.getAdjustBefore());
            bw_b_view.setText(value.getAdjustAfter());
        }
    }

    private void dataShow(String sel1, String sel2, String sel3, String sel4) {
        Log.i(TAG, "sel1: " + sel1);
        Log.i(TAG, "sel2: " + sel2);
        Log.i(TAG, "sel3: " + sel3);
        Log.i(TAG, "sel4: " + sel4);
        if ("select1_1".equals(sel1)) {
            llToTurnTo1.setVisibility(View.VISIBLE);
        } else if ("select1_2".equals(sel1)) {
            llBurdenWheel1.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo1.setVisibility(View.GONE);
            llBurdenWheel1.setVisibility(View.GONE);
        }

        if ("select2_1".equals(sel2)) {
            llToTurnTo2.setVisibility(View.VISIBLE);
        } else if ("select2_2".equals(sel2)) {
            llBurdenWheel2.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo2.setVisibility(View.GONE);
            llBurdenWheel2.setVisibility(View.GONE);
        }
        if ("select3_1".equals(sel3)) {
            llToTurnTo3.setVisibility(View.VISIBLE);
        } else if ("select3_2".equals(sel3)) {
            llBurdenWheel3.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo3.setVisibility(View.GONE);
            llBurdenWheel3.setVisibility(View.GONE);
        }
        if ("select4_1".equals(sel4)) {
            llToTurnTo4.setVisibility(View.VISIBLE);
        } else if ("select4_2".equals(sel4)) {
            llBurdenWheel4.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo4.setVisibility(View.GONE);
            llBurdenWheel4.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        strRegisterNum = intent.getStringExtra("registerNum");
        strDate = intent.getStringExtra("date");
        strCustomerInfo = intent.getStringExtra("customerInfo");
        strVehicleType = intent.getStringExtra("vehiclTeype");
        strMileage = intent.getStringExtra("mileage");
        btAutograph = intent.getByteArrayExtra("autograph");
        sel1 = intent.getStringExtra("sel1");
        sel2 = intent.getStringExtra("sel2");
        sel3 = intent.getStringExtra("sel3");
        sel4 = intent.getStringExtra("sel4");
        Log.i(TAG, "initView:-- " + strVehicleType + "--" + sel1);
        if (UITools.isNotEmpty(strRegisterNum)) {
            tvRegisterNum.setText(strRegisterNum);
            tvDate.setText(strDate);
            tvCustomerInfo.setText(strCustomerInfo);
            tvVehicleType.setText(strVehicleType);
            tvMileage.setText(strMileage);
            imgQm.setImageBitmap(UITools.byteTobitmap(btAutograph));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
//                Intent intent = new Intent(UserReportActivity.this, UserRecordActivity.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.btn_printing:
                try {
                    SavePhoto savePhoto = new SavePhoto(UserReportActivity.this);
                    savePhoto.SaveBitmapFromView(llReportForm);
                    UITools.showToast(UserReportActivity.this, R.string.str_saved_to_allery);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            Log.i(TAG, "onKeyDown: " + KeyEvent.KEYCODE_BACK);
//            Intent intent = new Intent(UserReportActivity.this, UserRecordActivity.class);
//            startActivity(intent);
//            finish();
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
