package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.inprintech.wheelalignmenttest.Adapter.DeviceAdapter;
import com.inprintech.wheelalignmenttest.Adapter.UserRecordAdapter;
import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteDeviceHelpter;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteHelpter;
import com.inprintech.wheelalignmenttest.Model.UserRecord;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.FilterIdListener;
import com.inprintech.wheelalignmenttest.Utils.FilterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备管理
 */
public class DeviceManagerActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DeviceManagerActivity";

    private ImageView imgBack;
    private ListView lvDeviceId;
    private EditText etIdNum;

    List<String> deviceList = new ArrayList<>();
    private boolean seeOrdel = false;
    private DeviceAdapter deviceAdapter;
    SQLiteDeviceHelpter dbHelper;
    SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);

        initView();
    }

    private void initView() {
        lvDeviceId = findViewById(R.id.lv_device);
        etIdNum = findViewById(R.id.et_query_device);
        imgBack = findViewById(R.id.img_back_main);

        imgBack.setOnClickListener(this);

        dbHelper = new SQLiteDeviceHelpter(this, dbHelper.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库

        setData();
        setListeners();
    }

    /**
     * 设置Adapter
     */
    private void setData() {
        initData();
        deviceAdapter = new DeviceAdapter(DeviceManagerActivity.this, deviceList, new FilterIdListener() {
            @Override
            public void getFilterData(List<String> list) {
                Log.i(TAG, "getFilterData: --" + list.toArray().toString());
            }
        }, seeOrdel);
        lvDeviceId.setAdapter(deviceAdapter);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        deviceList.clear();
        if (tabbleIsExist(db, dbHelper.TB_NAME)) {
            cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String deviceId = cursor.getString(1);
                deviceList.add(deviceId);
                cursor.moveToNext();
            }
        }
    }

    /**
     * 判断数据库中表是否存在 以及表中是否有数据
     *
     * @param db
     * @param tableName
     * @return
     */
    private boolean tabbleIsExist(SQLiteDatabase db, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from " + dbHelper.DB_NAME + " where type ='table' and name ='" + tableName.trim() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 输入框监听事件
     */
    private void setListeners() {
        etIdNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //编辑框内容改变的时候会执行该方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (deviceAdapter != null) {
                    deviceAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(DeviceManagerActivity.this, CalibrationActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(DeviceManagerActivity.this, CalibrationActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
