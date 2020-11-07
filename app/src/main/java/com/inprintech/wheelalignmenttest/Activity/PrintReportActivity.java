package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.inprintech.wheelalignmenttest.Utils.UITools;

public class PrintReportActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PrintReportActivity";

    private static final String PACKNAME = "com.inprintech.wheelalignmenttest";
    private static final String DEFTYPE = "id";

    private ImageView imgBack;
    private ImageView imgQm;
    private TextView tvRegisterNum, tvDate, tvCustomerInfo, tvVehicleType, tvMileage;
    private LinearLayout llToTurnTo1, llToTurnTo2, llToTurnTo3, llToTurnTo4;
    private LinearLayout llBurdenWheel1, llBurdenWheel2, llBurdenWheel3, llBurdenWheel4;

    private SharedPreferences sharedVehicle;
    private SharedPreferences sharedDefining;
    private String sel1, sel2, sel3, sel4;

    private byte[] btAutograph;
    private String strRegisterNum, strDate, strCustomerInfo, strVehicleType, strMileage, strAutograph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_report);

        initView();
    }

    private void initView() {
        imgQm = findViewById(R.id.img_qm);
        imgBack = findViewById(R.id.img_back_main);
        tvRegisterNum = findViewById(R.id.tv_register_number);
        tvDate = findViewById(R.id.tv_date);
        tvCustomerInfo = findViewById(R.id.tv_customer_info);
        tvVehicleType = findViewById(R.id.tv_vehicle_type);
        tvMileage = findViewById(R.id.tv_mileage);
        llToTurnTo1 = findViewById(R.id.ll_to_turn_to_1);
        llToTurnTo2 = findViewById(R.id.ll_to_turn_to_2);
        llToTurnTo3 = findViewById(R.id.ll_to_turn_to_3);
        llToTurnTo4 = findViewById(R.id.ll_to_turn_to_4);
        llBurdenWheel1 = findViewById(R.id.ll_burden_wheel_1);
        llBurdenWheel2 = findViewById(R.id.ll_burden_wheel_2);
        llBurdenWheel3 = findViewById(R.id.ll_burden_wheel_3);
        llBurdenWheel4 = findViewById(R.id.ll_burden_wheel_4);

        imgBack.setOnClickListener(this);

        sharedVehicle = getSharedPreferences("Vehicle_Info", MODE_PRIVATE);
        strRegisterNum = sharedVehicle.getString("registerNum", "");
        strDate = sharedVehicle.getString("date", "");
        strCustomerInfo = sharedVehicle.getString("customerInfo", "");
        strVehicleType = sharedVehicle.getString("vehicleType", "");
        strMileage = sharedVehicle.getString("mileage", "");
        strAutograph = sharedVehicle.getString("autograph", "");
        Log.i(TAG, "initView: ---" + strRegisterNum);
        tvRegisterNum.setText(strRegisterNum + "");
        tvDate.setText(strDate + "");
        tvCustomerInfo.setText(strCustomerInfo + "");
        tvVehicleType.setText(strVehicleType + "");
        tvMileage.setText(strMileage + "");
        btAutograph = UITools.StringToBytes(strAutograph);
        imgQm.setImageBitmap(UITools.byteTobitmap(btAutograph));


        sharedDefining = getSharedPreferences("defining_Vehicles", MODE_PRIVATE);
        sel1 = sharedDefining.getString("sel1", "");
        sel2 = sharedDefining.getString("sel2", "");
        sel3 = sharedDefining.getString("sel3", "");
        sel4 = sharedDefining.getString("sel4", "");
        dataShow(sel1, sel2, sel3, sel4);

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
        String select = sharedDefining.getString("sel" + i, "");
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
    private void setValue(String name, ReportValue value,boolean isTurn,boolean right) {
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
        if (sel1.equals("select1_1")) {
            llToTurnTo1.setVisibility(View.VISIBLE);
        } else if (sel1.equals("select1_2")) {
            llBurdenWheel1.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo1.setVisibility(View.GONE);
            llBurdenWheel1.setVisibility(View.GONE);
        }

        if (sel2.equals("select2_1")) {
            llToTurnTo2.setVisibility(View.VISIBLE);
        } else if (sel2.equals("select2_2")) {
            llBurdenWheel2.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo2.setVisibility(View.GONE);
            llBurdenWheel2.setVisibility(View.GONE);
        }

        if (sel3.equals("select3_1")) {
            llToTurnTo3.setVisibility(View.VISIBLE);
        } else if (sel3.equals("select3_2")) {
            llBurdenWheel3.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo3.setVisibility(View.GONE);
            llBurdenWheel3.setVisibility(View.GONE);
        }

        if (sel4.equals("select4_1")) {
            llToTurnTo4.setVisibility(View.VISIBLE);
        } else if (sel4.equals("select4_2")) {
            llBurdenWheel4.setVisibility(View.VISIBLE);
        } else {
            llToTurnTo4.setVisibility(View.GONE);
            llBurdenWheel4.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(PrintReportActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(PrintReportActivity.this, MeasureAdjustActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
