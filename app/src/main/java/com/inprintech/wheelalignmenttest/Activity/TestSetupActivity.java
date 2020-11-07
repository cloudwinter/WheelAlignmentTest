package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.inprintech.wheelalignmenttest.common.CommonConstants;

/**
 * 店家设置
 */
public class TestSetupActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "StoreSetupActivity";

    private ImageView imgBack, imgLayout;
    private LinearLayout llConfirmMsg;

    private EditText etSync,etSyncPitch;
    private EditText etKngpinyPitch, etKngpin;
    private EditText etCoaxial;
    private EditText etToeIn,etToeInPitch;
    private EditText etAxle;

    private EditText etTimeInterval;

    private SharedPreferences sharedStore;
    private SharedPreferences.Editor editorStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_setup);
        initView();
    }

    private void initView() {
        imgLayout = findViewById(R.id.img_layout);
        imgBack = findViewById(R.id.img_back_main);
        llConfirmMsg = findViewById(R.id.ll_confirm_msg);

        etSync = findViewById(R.id.et_sync);
        etSyncPitch = findViewById(R.id.et_sync_pitch);
        etKngpin = findViewById(R.id.et_kngpin);
        etKngpinyPitch = findViewById(R.id.et_kngpin_pitch);
        etCoaxial = findViewById(R.id.et_coaxial);
        etToeIn = findViewById(R.id.et_toe_in);
        etToeInPitch = findViewById(R.id.et_toe_in_pitch);
        etAxle = findViewById(R.id.et_axle);

        etTimeInterval = findViewById(R.id.et_time_interval);

        imgBack.setOnClickListener(this);
        etSync.setOnClickListener(this);
        etKngpin.setOnClickListener(this);
        llConfirmMsg.setOnClickListener(this);

        SharedPreferences sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
        // 同步
        float sharedSyncFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.SYNC_KEY, 0L);
        if (sharedSyncFloat != 0) {
            etSync.setText(sharedSyncFloat + "");
        }
        float sharedSyncPitchFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.SYNC_PITCH_KEY, 0L);
        if (sharedSyncPitchFloat != 0) {
            etSyncPitch.setText(sharedSyncPitchFloat + "");
        }

        // 注销
        float sharedkngpinFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.KNGPIN_KEY, 0L);
        if (sharedkngpinFloat != 0) {
            etKngpin.setText(sharedkngpinFloat + "");
        }
        float sharedkngpinPitchFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.KNGPIN_PITCH_KEY, 0L);
        if (sharedkngpinPitchFloat != 0) {
            etKngpinyPitch.setText(sharedkngpinPitchFloat + "");
        }

        // 同轴
        float sharedCoaxialFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.COAXIAL_KEY, 0L);
        if (sharedCoaxialFloat != 0) {
            etCoaxial.setText(sharedCoaxialFloat + "");
        }

        // 前束
        float sharedToeInFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.TOE_IN_KEY, 0L);
        if (sharedToeInFloat != 0) {
            etToeIn.setText(sharedToeInFloat + "");
        }
        float sharedToeInPitchFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.TOE_IN_PITCH_KEY, 0L);
        if (sharedToeInPitchFloat != 0) {
            etToeInPitch.setText(sharedToeInPitchFloat + "");
        }

        //车桥
        float sharedAxleFloat = sharedStore.getFloat(CommonConstants.TestStoreShare.AXLE_KEY, 0L);
        if (sharedAxleFloat != 0) {
            etAxle.setText(sharedAxleFloat + "");
        }

        //时间间隔
        int timeInterval = sharedStore.getInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, 0);
        if (timeInterval != 0) {
            etTimeInterval.setText(timeInterval + "");
        }



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_confirm_msg:
                sharedStore = getSharedPreferences(CommonConstants.TestStoreShare.SHARENAME, MODE_PRIVATE);
                editorStore = sharedStore.edit();
                String syncText = etSync.getText().toString().trim();
                String syncPitch = etSyncPitch.getText().toString().trim();
                editorStore.putFloat(CommonConstants.TestStoreShare.SYNC_KEY, Float.parseFloat(syncText));
                editorStore.putFloat(CommonConstants.TestStoreShare.SYNC_PITCH_KEY, Float.parseFloat(syncPitch));

                String kngpinText = etKngpin.getText().toString().trim();
                String kngpinPitchText = etKngpinyPitch.getText().toString().trim();
                editorStore.putFloat(CommonConstants.TestStoreShare.KNGPIN_KEY, Float.parseFloat(kngpinText));
                editorStore.putFloat(CommonConstants.TestStoreShare.KNGPIN_PITCH_KEY, Float.parseFloat(kngpinPitchText));


                String toeInText = etToeIn.getText().toString().trim();
                String toeInPitchText = etToeInPitch.getText().toString().trim();
                editorStore.putFloat(CommonConstants.TestStoreShare.TOE_IN_KEY, Float.parseFloat(toeInText));
                editorStore.putFloat(CommonConstants.TestStoreShare.TOE_IN_PITCH_KEY, Float.parseFloat(toeInPitchText));


                String coaxialText = etKngpin.getText().toString().trim();
                editorStore.putFloat(CommonConstants.TestStoreShare.COAXIAL_KEY, Float.parseFloat(coaxialText));


                String axleText = etAxle.getText().toString().trim();
                editorStore.putFloat(CommonConstants.TestStoreShare.AXLE_KEY, Float.parseFloat(axleText));

                String timeInterval = etTimeInterval.getText().toString().trim();
                editorStore.putInt(CommonConstants.TestStoreShare.TIME_INTERVAL_KEY, Integer.parseInt(timeInterval));

                editorStore.commit();
                Intent intent = new Intent(TestSetupActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                UITools.showToast(TestSetupActivity.this, R.string.str_save_successfully);
                break;
            case R.id.img_back_main:
                Intent intentBack = new Intent(TestSetupActivity.this, SettingsActivity.class);
                startActivity(intentBack);
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(TestSetupActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
