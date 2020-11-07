package com.inprintech.wheelalignmenttest.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.ActivityCollector;
import com.inprintech.wheelalignmenttest.Utils.UITools;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    private LinearLayout imgUserData, imgSystemSetup, imgStartPositioning;

    private long exitTime = 0;// 退出时间

    private SharedPreferences sharedDefining;
    private SharedPreferences sharedVehicle;
    private SharedPreferences sharedMeasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        imgStartPositioning = findViewById(R.id.img_start_positioning);
        imgUserData = findViewById(R.id.img_user_data);
        imgSystemSetup = findViewById(R.id.img_system_setup);

        imgSystemSetup.setOnClickListener(this);
        imgUserData.setOnClickListener(this);
        imgStartPositioning.setOnClickListener(this);

        sharedDefining = getSharedPreferences("defining_Vehicles", MODE_PRIVATE);
        if (sharedDefining != null) {
            sharedDefining.edit().clear().commit();
        }
        sharedVehicle = getSharedPreferences("Vehicle_Info", MODE_PRIVATE);
        if (sharedVehicle != null) {
            sharedVehicle.edit().clear().commit();
        }
        sharedMeasure = getSharedPreferences("check_measure", MODE_PRIVATE);
        if (sharedMeasure != null) {
            sharedMeasure.edit().clear().commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_user_data:
                Intent intent2 = new Intent(MainActivity.this, UserRecordActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.img_start_positioning:
                Intent intent3 = new Intent(MainActivity.this, VehicleInfoActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.img_system_setup:
                Intent intent4 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent4);
                finish();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 判断间隔时间 大于2秒就退出应用
            if ((System.currentTimeMillis() - exitTime) > 2500) {
                UITools.showToast(MainActivity.this, R.string.str_exit_the_program);
                // 计算两次返回键按下的时间差
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
