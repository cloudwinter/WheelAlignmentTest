package com.inprintech.wheelalignmenttest.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteHelpter;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.inprintech.wheelalignmenttest.View.LinePathView;
import com.inprintech.wheelalignmenttest.View.SignatureView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * 车辆信息
 */
public class VehicleInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "VehicleInfoActivity";

    private Button btnNext;
    private ImageView imgBack, imgQm;
    private TextView tvEdit, etDate;
    private EditText etRegisterNum, etCustomerInfo, etVehicleType, etMileage;
    private LinePathView etAutograph;

    private EditText[] editTexts;
    byte[] imgData;
    private String strRegisterNum, strDate, strCustomerInfo, strVehicleType, strMileage, strAutograph;

    private SharedPreferences sharedVehicle;
    private SharedPreferences.Editor editorVehicle;

    SQLiteHelpter dbHelper;
    SQLiteDatabase db;
    private Cursor cursor;
    private ContentValues selCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_location);
        initView();
    }

    private void initView() {
        imgQm = findViewById(R.id.img_qm);
        imgBack = findViewById(R.id.img_back_main);
        tvEdit = findViewById(R.id.tv_edit);
        btnNext = findViewById(R.id.btn_next);
        etRegisterNum = findViewById(R.id.et_register_number);
        etDate = findViewById(R.id.et_date);
        etCustomerInfo = findViewById(R.id.et_customer_info);
        etVehicleType = findViewById(R.id.et_vehicle_type);
        etMileage = findViewById(R.id.et_mileage);
        etAutograph = findViewById(R.id.et_autograph);

        etAutograph.setBackColor(Color.WHITE);
        etAutograph.setPaintWidth(20);
        etAutograph.setPenColor(Color.BLACK);

        editTexts = new EditText[]{etRegisterNum, etCustomerInfo, etVehicleType, etMileage};

        etDate.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        dbHelper = new SQLiteHelpter(this, dbHelper.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库

        sharedVehicle = getSharedPreferences("Vehicle_Info", MODE_PRIVATE);
        strRegisterNum = sharedVehicle.getString("registerNum", "");
        strDate = sharedVehicle.getString("date", "");
        strCustomerInfo = sharedVehicle.getString("customerInfo", "");
        strVehicleType = sharedVehicle.getString("vehicleType", "");
        strMileage = sharedVehicle.getString("mileage", "");
        strAutograph = sharedVehicle.getString("autograph", "");
        if (UITools.isNotEmpty(strRegisterNum)) {
            UITools.setEditFocusable(editTexts, false);
            etRegisterNum.setText(strRegisterNum);
            etDate.setText(strDate);
            etCustomerInfo.setText(strCustomerInfo);
            etVehicleType.setText(strVehicleType);
            etMileage.setText(strMileage);
            imgData = UITools.StringToBytes(strAutograph);
            imgQm.setVisibility(View.VISIBLE);
            etAutograph.setVisibility(View.GONE);
            imgQm.setImageBitmap(UITools.byteTobitmap(imgData));
        } else {
            UITools.setEditFocusable(editTexts, true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                sharedVehicle = getSharedPreferences("Vehicle_Info", MODE_PRIVATE);
                editorVehicle = sharedVehicle.edit();
                strRegisterNum = etRegisterNum.getText().toString().trim();
                strDate = etDate.getText().toString().trim();
                strCustomerInfo = etCustomerInfo.getText().toString().trim();
                strVehicleType = etVehicleType.getText().toString().trim();
                strMileage = etMileage.getText().toString().trim();
                if (UITools.isNotEmpty(strRegisterNum)) {
                    if (UITools.isNotEmpty(strDate)) {
                        ContentValues values = new ContentValues();
                        values.put("register_num", strRegisterNum);
                        values.put("date", strDate);
                        values.put("customer_info", strCustomerInfo);
                        values.put("vehicl_teype", strVehicleType);
                        values.put("mileage", strMileage);
                        byte[] imgData = etAutograph.save(true, 10);
                        Log.i(TAG, "onClick: ----" + strVehicleType);
                        values.put("autograph", imgData);
                        long rowid = db.insert(dbHelper.TB_NAME, null, values);
                        if (rowid == -1) {
                            Log.i(TAG, "onClick: --数据插入失败");
                        } else {
                            editorVehicle.putString("registerNum", strRegisterNum);
                            editorVehicle.putString("date", strDate);
                            editorVehicle.putString("customerInfo", strCustomerInfo);
                            editorVehicle.putString("vehicleType", strVehicleType);
                            editorVehicle.putString("mileage", strMileage);
                            editorVehicle.putString("autograph", UITools.bytesToHexString(imgData));
                            editorVehicle.commit();
                            Log.i(TAG, "onClick: --数据插入成功");
                            Intent intent = new Intent(VehicleInfoActivity.this, DefiningVehiclesActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        UITools.showToast(VehicleInfoActivity.this, R.string.str_incorrect_date_format);
                        return;
                    }
                } else {
                    UITools.showToast(VehicleInfoActivity.this, R.string.str_registration_number_cannot_null);
                    return;
                }
                break;
            case R.id.img_back_main:
                Intent intentBack = new Intent(VehicleInfoActivity.this, MainActivity.class);
                startActivity(intentBack);
                finish();
                break;
            case R.id.tv_edit:
                String editStr = tvEdit.getText().toString().trim();
                if (editStr.equals("完成") || editStr.equals("Complete")) {
                    UITools.setEditFocusable(editTexts, false);
                    byte[] imgData = etAutograph.save(true, 10);
                    if (imgData != null) {
                        imgQm.setVisibility(View.VISIBLE);
                        etAutograph.setVisibility(View.GONE);
                        imgQm.setImageBitmap(UITools.byteTobitmap(imgData));
                    }
                    tvEdit.setText(getResources().getText(R.string.str_edit));
                } else if (editStr.equals("编辑") || editStr.equals("edit")) {
                    imgQm.setVisibility(View.GONE);
                    etAutograph.setVisibility(View.VISIBLE);
                    UITools.setEditFocusable(editTexts, true);
                    etAutograph.clear();
                    etAutograph.setBackColor(Color.WHITE);
                    etAutograph.setPaintWidth(20);
                    etAutograph.setPenColor(Color.BLACK);
                    tvEdit.setText(getResources().getText(R.string.str_complete));
                }
                break;
            case R.id.et_date:
                showDatePickDialog();
                break;
        }
    }


//    private void playMusic(int musicId) {
//        MediaPlayer music = MediaPlayer.create(this, musicId);
//        music.start();
//    }

    /**
     * 日期选择器
     */
    private void showDatePickDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(VehicleInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, "onDateSet: " + monthOfYear);
                String month, day = null;
                if (monthOfYear < 10) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                strDate = year + "-" + month + "-" + day;
                etDate.setText(strDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(VehicleInfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
