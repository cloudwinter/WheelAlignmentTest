package com.inprintech.wheelalignmenttest.Activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteHelpter;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteSelMeasureHelpter;
import com.inprintech.wheelalignmenttest.R;

/**
 * 定义车辆
 */
public class DefiningVehiclesActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "DefiningVehiclesActivity";

    private Button btnNext;
    private ImageView imgBack;
    private ImageView img1, img2, img3, img4;
    private RadioGroup rg1, rg2, rg3, rg4;
    private RadioButton rbFixedSteering1, rbFixedSteering2, rbFixedSteering3, rbFixedSteering4;
    private RadioButton rbLoadBearingBridge1, rbLoadBearingBridge2, rbLoadBearingBridge3, rbLoadBearingBridge4;

    private SharedPreferences sharedDefining;
    private SharedPreferences.Editor editorDefining;
    private SharedPreferences sharedVehicle;

    SQLiteSelMeasureHelpter dbHelper;
    SQLiteDatabase db;

    private String registerNum = null;
    private String sel1 = "select1_0", sel2 = "select2_0", sel3 = "select3_0", sel4 = "select4_0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defining_cehicles);

        initView();
    }

    @SuppressLint("LongLogTag")
    private void initView() {
        imgBack = findViewById(R.id.img_back_main);
        btnNext = findViewById(R.id.btn_next);
        img1 = findViewById(R.id.img_1);
        img2 = findViewById(R.id.img_2);
        img3 = findViewById(R.id.img_3);
        img4 = findViewById(R.id.img_4);
        rg1 = findViewById(R.id.rg_1);
        rg2 = findViewById(R.id.rg_2);
        rg3 = findViewById(R.id.rg_3);
        rg4 = findViewById(R.id.rg_4);
        rbFixedSteering1 = findViewById(R.id.cb_fixed_steering1);
        rbFixedSteering2 = findViewById(R.id.cb_fixed_steering2);
        rbFixedSteering3 = findViewById(R.id.cb_fixed_steering3);
        rbFixedSteering4 = findViewById(R.id.cb_fixed_steering4);
        rbLoadBearingBridge1 = findViewById(R.id.cb_load_bearing_bridge1);
        rbLoadBearingBridge2 = findViewById(R.id.cb_load_bearing_bridge2);
        rbLoadBearingBridge3 = findViewById(R.id.cb_load_bearing_bridge3);
        rbLoadBearingBridge4 = findViewById(R.id.cb_load_bearing_bridge4);

        rg1.setOnCheckedChangeListener(this);
        rg2.setOnCheckedChangeListener(this);
        rg3.setOnCheckedChangeListener(this);
        rg4.setOnCheckedChangeListener(this);
        imgBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        rbChecked();

        sharedVehicle = getSharedPreferences("Vehicle_Info",MODE_PRIVATE);
        registerNum = sharedVehicle.getString("registerNum","");
        Log.i(TAG, "initView: " + registerNum);
        dbHelper = new SQLiteSelMeasureHelpter(this, dbHelper.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库
    }

    @SuppressLint("LongLogTag")
    private void rbChecked(){
        sharedDefining = getSharedPreferences("defining_Vehicles", MODE_PRIVATE);
        sel1 = sharedDefining.getString("sel1", "select1_0");
        sel2 = sharedDefining.getString("sel2", "select2_0");
        sel3 = sharedDefining.getString("sel3", "select3_0");
        sel4 = sharedDefining.getString("sel4", "select4_0");
        Log.i(TAG, "sel1: " + sel1);
        Log.i(TAG, "sel2: " + sel2);
        Log.i(TAG, "sel3: " + sel3);
        Log.i(TAG, "sel4: " + sel4);

        if (sel1.equals("select1_1")){
            rbFixedSteering1.setChecked(true);
            img1.setImageResource(R.mipmap.to_turn_to);
        } else if (sel1.equals("select1_2")){
            rbLoadBearingBridge1.setChecked(true);
            img1.setImageResource(R.mipmap.bearing);
        }

        if (sel2.equals("select2_1")){
            rbFixedSteering2.setChecked(true);
            img2.setImageResource(R.mipmap.to_turn_to);
        } else if (sel2.equals("select2_2")){
            rbLoadBearingBridge2.setChecked(true);
            img2.setImageResource(R.mipmap.bearing);
        }

        if (sel3.equals("select3_1")){
            rbFixedSteering3.setChecked(true);
            img3.setImageResource(R.mipmap.to_turn_to);
        } else if (sel3.equals("select3_2")){
            rbLoadBearingBridge3.setChecked(true);
            img3.setImageResource(R.mipmap.bearing);
        }

        if (sel4.equals("select4_1")){
            rbFixedSteering4.setChecked(true);
            img4.setImageResource(R.mipmap.to_turn_to);
        } else if (sel4.equals("select4_2")){
            rbLoadBearingBridge4.setChecked(true);
            img4.setImageResource(R.mipmap.bearing);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int color1 = getResources().getColor(R.color.colorBule);
        int color2 = getResources().getColor(R.color.colorBlack);
        switch (checkedId) {
            case R.id.cb_fixed_steering1://转向
                rbFixedSteering1.setTextColor(color1);
                rbLoadBearingBridge1.setTextColor(color2);
                img1.setImageResource(R.mipmap.to_turn_to);
                sel1 = "select1_1";
                break;
            case R.id.cb_load_bearing_bridge1://承重桥
                rbFixedSteering1.setTextColor(color2);
                rbLoadBearingBridge1.setTextColor(color1);
                img1.setImageResource(R.mipmap.bearing);
                sel1 = "select1_2";
                break;
            case R.id.cb_fixed_steering2://转向
                rbFixedSteering2.setTextColor(color1);
                rbLoadBearingBridge2.setTextColor(color2);
                img2.setImageResource(R.mipmap.to_turn_to);
                sel2 = "select2_1";
                break;
            case R.id.cb_load_bearing_bridge2://承重桥
                rbLoadBearingBridge2.setTextColor(color1);
                rbFixedSteering2.setTextColor(color2);
                img2.setImageResource(R.mipmap.bearing);
                sel2 = "select2_2";
                break;
            case R.id.cb_fixed_steering3://转向
                rbFixedSteering3.setTextColor(color1);
                rbLoadBearingBridge3.setTextColor(color2);
                img3.setImageResource(R.mipmap.to_turn_to);
                sel3 = "select3_1";
                break;
            case R.id.cb_load_bearing_bridge3://承重桥
                rbLoadBearingBridge3.setTextColor(color1);
                rbFixedSteering3.setTextColor(color2);
                img3.setImageResource(R.mipmap.bearing);
                sel3 = "select3_2";
                break;
            case R.id.cb_fixed_steering4://转向
                rbFixedSteering4.setTextColor(color1);
                rbLoadBearingBridge4.setTextColor(color2);
                img4.setImageResource(R.mipmap.to_turn_to);
                sel4 = "select4_1";
                break;
            case R.id.cb_load_bearing_bridge4://承重桥
                rbLoadBearingBridge4.setTextColor(color1);
                rbFixedSteering4.setTextColor(color2);
                img4.setImageResource(R.mipmap.bearing);
                sel4 = "select4_2";
                break;
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                ContentValues values = new ContentValues();
                Log.i(TAG, "onClick: " + registerNum+ sel1 + sel2 + sel3 + sel4);
                values.put("register_num", registerNum );
                values.put("selects_1", sel1);
                values.put("selects_2", sel2);
                values.put("selects_3", sel3);
                values.put("selects_4", sel4);
                long rowid = db.insert(dbHelper.TB_NAME, null, values);
                if (rowid == -1) {
                    Log.i(TAG, "onClick: 插入失败");
                } else {
                    Log.i(TAG, "onClick: 插入成功" + rowid);
                }
                sharedDefining = getSharedPreferences("defining_Vehicles", MODE_PRIVATE);
                editorDefining = sharedDefining.edit();
                editorDefining.putString("sel1", sel1);
                editorDefining.putString("sel2", sel2);
                editorDefining.putString("sel3", sel3);
                editorDefining.putString("sel4", sel4);
                editorDefining.commit();
                Intent intent = new Intent(DefiningVehiclesActivity.this, MeasureAdjustActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.img_back_main:
                Intent intentBack = new Intent(DefiningVehiclesActivity.this, VehicleInfoActivity.class);
                startActivity(intentBack);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(DefiningVehiclesActivity.this, VehicleInfoActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
