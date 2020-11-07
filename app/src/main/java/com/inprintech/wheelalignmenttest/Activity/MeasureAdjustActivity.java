package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;

/**
 * 测量调整
 */
public class MeasureAdjustActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MeasureAdjustActivity";

    private ImageView imgBack;
    private LinearLayout llMeasure;
    private LinearLayout ll1, ll2, ll3, ll4;
    private LinearLayout llCoaxial, llKingpin, llToe_in, llAxle, llSync, llPrinting;
    private ImageView img1, img2, img3, img4, imgAxle;

    private SharedPreferences sharedDefining;
    private String sel1, sel2, sel3, sel4;

    private SharedPreferences sharedMeasure;
    private SharedPreferences.Editor editorMeasure;
    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_adjust);

        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back_main);
        imgAxle = findViewById(R.id.img_axle);
        img1 = findViewById(R.id.img_1);
        img2 = findViewById(R.id.img_2);
        img3 = findViewById(R.id.img_3);
        img4 = findViewById(R.id.img_4);
        llMeasure = findViewById(R.id.ll_measure);
        llCoaxial = findViewById(R.id.ll_coaxial);
        llKingpin = findViewById(R.id.ll_kingpin);
        llToe_in = findViewById(R.id.ll_toe_in);
        llSync = findViewById(R.id.ll_sync);
        llAxle = findViewById(R.id.ll_axle);
        llPrinting = findViewById(R.id.ll_printing);
        ll1 = findViewById(R.id.ll_1);
        ll2 = findViewById(R.id.ll_2);
        ll3 = findViewById(R.id.ll_3);
        ll4 = findViewById(R.id.ll_4);

        imgBack.setOnClickListener(this);
        llCoaxial.setOnClickListener(this);
        llKingpin.setOnClickListener(this);
        llToe_in.setOnClickListener(this);
        llSync.setOnClickListener(this);
        llAxle.setOnClickListener(this);
        llPrinting.setOnClickListener(this);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);

        sharedDefining = getSharedPreferences("defining_Vehicles", MODE_PRIVATE);
        sel1 = sharedDefining.getString("sel1", "select1_0");
        sel2 = sharedDefining.getString("sel2", "select2_0");
        sel3 = sharedDefining.getString("sel3", "select3_0");
        sel4 = sharedDefining.getString("sel4", "select4_0");
        selectNum(sel1, sel2, sel3, sel4);

        sharedMeasure = getSharedPreferences("check_measure", MODE_PRIVATE);
        check = sharedMeasure.getInt("check", 0);
        measurePatternSel(check);
    }

    private void measurePatternSel(int i) {
        if (i > 0) {
            llMeasure.setVisibility(View.VISIBLE);
        }
        switch (i) {
            case 1:
                ll1.setBackgroundResource(R.mipmap.measure_s_1);
                ll2.setBackgroundResource(R.mipmap.measure_n_2);
                ll3.setBackgroundResource(R.mipmap.measure_n_3);
                ll4.setBackgroundResource(R.mipmap.measure_n_4);
                if (sel1.equals("select1_2")){
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                break;
            case 2:
                ll1.setBackgroundResource(R.mipmap.measure_n_1);
                ll2.setBackgroundResource(R.mipmap.measure_s_2);
                ll3.setBackgroundResource(R.mipmap.measure_n_3);
                ll4.setBackgroundResource(R.mipmap.measure_n_4);
                if (sel2.equals("select2_2")){
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                break;
            case 3:
                ll1.setBackgroundResource(R.mipmap.measure_n_1);
                ll2.setBackgroundResource(R.mipmap.measure_n_2);
                ll3.setBackgroundResource(R.mipmap.measure_s_3);
                ll4.setBackgroundResource(R.mipmap.measure_n_4);
                if (sel3.equals("select3_2")){
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                break;
            case 4:
                ll1.setBackgroundResource(R.mipmap.measure_n_1);
                ll2.setBackgroundResource(R.mipmap.measure_n_2);
                ll3.setBackgroundResource(R.mipmap.measure_n_3);
                ll4.setBackgroundResource(R.mipmap.measure_s_4);
                if (sel4.equals("select4_2")){
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                break;
        }
    }

    private void selectNum(String sel1, String sel2, String sel3, String sel4) {
        Log.i(TAG, "initView: --" + sel1);
        Log.i(TAG, "initView: --" + sel2);
        Log.i(TAG, "initView: --" + sel3);
        Log.i(TAG, "initView: --" + sel4);
        if (sel1.equals("select1_0") && sel2.equals("select2_0")
                && sel3.equals("select3_0") && sel4.equals("select4_0")) {
            Log.i(TAG, "initView: 0000");
            ll1.setVisibility(View.INVISIBLE);
            ll2.setVisibility(View.INVISIBLE);
            ll3.setVisibility(View.INVISIBLE);
            ll4.setVisibility(View.INVISIBLE);
        }

        if (sel1.equals("select1_2") && sel2.equals("select2_2")
                && sel3.equals("select3_2") && sel4.equals("select4_2")) {
            llAxle.setEnabled(true);
            imgAxle.setBackgroundResource(R.mipmap.axle_yes);
        } else {
            llAxle.setEnabled(false);
            imgAxle.setBackgroundResource(R.mipmap.axle_no);
        }
        if (sel1.equals("select1_1")) {
            img1.setBackgroundResource(R.mipmap.to_turn_to);
            llAxle.setEnabled(false);
            ll1.setVisibility(View.VISIBLE);
        } else if (sel1.equals("select1_2")) {
            img1.setBackgroundResource(R.mipmap.bearing);
            ll1.setVisibility(View.VISIBLE);
        } else if (sel1.equals("select1_0")) {
            ll1.setVisibility(View.INVISIBLE);
        }
        if (sel2.equals("select2_1")) {
            llAxle.setEnabled(false);
            img2.setBackgroundResource(R.mipmap.to_turn_to);
            ll2.setVisibility(View.VISIBLE);
        } else if (sel2.equals("select2_2")) {
            img2.setBackgroundResource(R.mipmap.bearing);
            ll2.setVisibility(View.VISIBLE);
        } else if (sel1.equals("select2_0")) {
            ll2.setVisibility(View.INVISIBLE);
        }

        if (sel3.equals("select3_1")) {
            llAxle.setEnabled(false);
            img3.setBackgroundResource(R.mipmap.to_turn_to);
            ll3.setVisibility(View.VISIBLE);
        } else if (sel3.equals("select3_2")) {
            img3.setBackgroundResource(R.mipmap.bearing);
            ll3.setVisibility(View.VISIBLE);
        } else if (sel1.equals("select3_0")) {
            ll3.setVisibility(View.INVISIBLE);
        }

        if (sel4.equals("select4_1")) {
            llAxle.setEnabled(false);
            img4.setBackgroundResource(R.mipmap.to_turn_to);
            ll4.setVisibility(View.VISIBLE);
        } else if (sel4.equals("select4_2")) {
            img4.setBackgroundResource(R.mipmap.bearing);
            ll4.setVisibility(View.VISIBLE);
        } else if (sel1.equals("select4_0")) {
            ll4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        sharedMeasure = getSharedPreferences("check_measure", MODE_PRIVATE);
        editorMeasure = sharedMeasure.edit();
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(MeasureAdjustActivity.this, DefiningVehiclesActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_coaxial:
                Intent intent0 = new Intent(MeasureAdjustActivity.this, CoaxialAdjustActivity.class);
                startActivity(intent0);
                finish();
                break;
            case R.id.ll_kingpin:
                Intent intent1 = new Intent(MeasureAdjustActivity.this, KingpinMeasureActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.ll_toe_in:
                Intent intent2 = new Intent(MeasureAdjustActivity.this, ToeinMeasureActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.ll_sync:
                Intent intent3 = new Intent(MeasureAdjustActivity.this, SyncAdjustActivity.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.ll_axle:
                Intent intent4 = new Intent(MeasureAdjustActivity.this, AxleMeasureActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.ll_printing:
                Intent intent5 = new Intent(MeasureAdjustActivity.this, PrintReportActivity.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.ll_1:
                editorMeasure.putInt("check", 1);
                selMeasure(1);
                break;
            case R.id.ll_2:
                editorMeasure.putInt("check", 2);
                selMeasure(2);
                break;
            case R.id.ll_3:
                editorMeasure.putInt("check", 3);
                selMeasure(3);
                break;
            case R.id.ll_4:
                editorMeasure.putInt("check", 4);
                selMeasure(4);
                break;
        }
        editorMeasure.commit();
    }

    private void selMeasure(int i) {
        llMeasure.setVisibility(View.VISIBLE);
        switch (i) {
            case 1:
                if (sel1.equals("select1_2")) {
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                ll1.setBackgroundResource(R.mipmap.measure_s_1);
                ll2.setBackgroundResource(R.mipmap.measure_n_2);
                ll3.setBackgroundResource(R.mipmap.measure_n_3);
                ll4.setBackgroundResource(R.mipmap.measure_n_4);
                break;
            case 2:
                if (sel2.equals("select2_2")) {
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                ll1.setBackgroundResource(R.mipmap.measure_n_1);
                ll2.setBackgroundResource(R.mipmap.measure_s_2);
                ll3.setBackgroundResource(R.mipmap.measure_n_3);
                ll4.setBackgroundResource(R.mipmap.measure_n_4);
                break;
            case 3:
                if (sel3.equals("select3_2")) {
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                ll1.setBackgroundResource(R.mipmap.measure_n_1);
                ll2.setBackgroundResource(R.mipmap.measure_n_2);
                ll3.setBackgroundResource(R.mipmap.measure_s_3);
                ll4.setBackgroundResource(R.mipmap.measure_n_4);
                break;
            case 4:
                if (sel4.equals("select4_2")) {
                    llAxle.setEnabled(true);
                    imgAxle.setBackgroundResource(R.mipmap.axle_yes);
                } else {
                    llAxle.setEnabled(false);
                    imgAxle.setBackgroundResource(R.mipmap.axle_no);
                }
                ll1.setBackgroundResource(R.mipmap.measure_n_1);
                ll2.setBackgroundResource(R.mipmap.measure_n_2);
                ll3.setBackgroundResource(R.mipmap.measure_n_3);
                ll4.setBackgroundResource(R.mipmap.measure_s_4);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(MeasureAdjustActivity.this, DefiningVehiclesActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
