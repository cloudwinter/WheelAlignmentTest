package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;

public class DeviceDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DeviceDetailsActivity";

    private ImageView imgBack;
    private TextView tv_ccd_data0_a, tv_ccd_data1_a, tv_roll_a, tv_pitch_a, tv_yaw_a;
    private TextView tv_ccd_data0_b, tv_ccd_data1_b, tv_roll_b, tv_pitch_b, tv_yaw_b;

    private double reada1, reada2, reada3, reada4, reada5;
    private double readb1, readb2, readb3, readb4, readb5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_details);

        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        reada1 = intent.getDoubleExtra("reada1", 0);
        reada2 = intent.getDoubleExtra("reada2", 0);
        reada3 = intent.getDoubleExtra("reada3", 0);
        reada4 = intent.getDoubleExtra("reada4", 0);
        reada5 = intent.getDoubleExtra("reada5", 0);
        readb1 = intent.getDoubleExtra("readb1", 0);
        readb2 = intent.getDoubleExtra("readb2", 0);
        readb3 = intent.getDoubleExtra("readb3", 0);
        readb4 = intent.getDoubleExtra("readb4", 0);
        readb5 = intent.getDoubleExtra("readb5", 0);
        Log.i(TAG, "getIntentData: reada1=" + reada1);
        tv_ccd_data0_a.setText(reada1 + "");
        tv_ccd_data1_a.setText(reada2 + "");
        tv_roll_a.setText(reada3 + "");
        tv_pitch_a.setText(reada4 + "");
        tv_yaw_a.setText(reada5 + "");
        tv_ccd_data0_b.setText(readb1 + "");
        tv_ccd_data1_b.setText(readb2 + "");
        tv_roll_b.setText(readb3 + "");
        tv_pitch_b.setText(readb4 + "");
        tv_yaw_b.setText(readb5 + "");
    }

    private void initView() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(DeviceDetailsActivity.this, CalibrationActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(DeviceDetailsActivity.this, CalibrationActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
