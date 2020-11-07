package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.Model.KingpinModel;
import com.inprintech.wheelalignmenttest.R;

/**
 * 综合展示
 */
public class DisplayAllActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DisplayAllActivity";

    private ImageView imgBack;

    private TextView tv1_caster_angle,tv1_in_angle,tv1_max_steering_angle,tv1_camber_angle;
    private TextView tv2_caster_angle,tv2_in_angle,tv2_max_steering_angle,tv2_camber_angle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all);

        initView();
    }

    private void initView(){
        imgBack = findViewById(R.id.img_back_main);
        imgBack.setOnClickListener(this);

        tv1_caster_angle = findViewById(R.id.tv1_caster_angle);
        tv1_in_angle = findViewById(R.id.tv1_in_angle);
        tv1_max_steering_angle = findViewById(R.id.tv1_max_steering_angle);
        tv1_camber_angle = findViewById(R.id.tv1_camber_angle);
        tv2_caster_angle = findViewById(R.id.tv2_caster_angle);
        tv2_in_angle = findViewById(R.id.tv2_in_angle);
        tv2_max_steering_angle = findViewById(R.id.tv2_max_steering_angle);
        tv2_camber_angle = findViewById(R.id.tv2_camber_angle);

        setDate();
    }


    private void setDate() {
        SharedPreferences sharedPreferences = getSharedPreferences("Kingpin",MODE_PRIVATE);
        String model1Json = sharedPreferences.getString("1",null);
        String model2Json = sharedPreferences.getString("2",null);
        if (!TextUtils.isEmpty(model1Json)) {
            KingpinModel model1 = new Gson().fromJson(model1Json, KingpinModel.class);
            tv1_caster_angle.setText(model1.casterAngle);
            tv1_in_angle.setText(model1.inAngle);
            tv1_max_steering_angle.setText(model1.maxSteeringAngle);
            tv1_camber_angle.setText(model1.camberAngle);
        }
        if (!TextUtils.isEmpty(model2Json)) {
            KingpinModel model2 = new Gson().fromJson(model2Json, KingpinModel.class);
            tv2_caster_angle.setText(model2.casterAngle);
            tv2_in_angle.setText(model2.inAngle);
            tv2_max_steering_angle.setText(model2.maxSteeringAngle);
            tv2_camber_angle.setText(model2.camberAngle);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DisplayAllActivity.this, MeasureAdjustActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(DisplayAllActivity.this, MeasureAdjustActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
