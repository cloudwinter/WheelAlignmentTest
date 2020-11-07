package com.inprintech.wheelalignmenttest.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteDeviceHelpter;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.Constants;
import com.inprintech.wheelalignmenttest.Utils.SocketUitls;
import com.inprintech.wheelalignmenttest.Utils.UITools;

import java.net.Socket;

/**
 * 标定
 */
public class CalibrationActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "CalibrationActivity";

    private ImageView imgBack;
    private LinearLayout llCalibration, llUpload, llReadBack, llAdmin;
    private Button btnModify1, btnSave1;
    private Button btnModify2, btnSave2;
    private TextView tv_ccd_data0_a, tv_ccd_data1_a, tv_roll_a, tv_pitch_a, tv_yaw_a;
    private TextView tv_ccd_data0_b, tv_ccd_data1_b, tv_roll_b, tv_pitch_b, tv_yaw_b;
    private EditText et_ccd_data0_a, et_ccd_data1_a, et_roll_a, et_pitch_a, et_yaw_a;
    private EditText et_ccd_data0_b, et_ccd_data1_b, et_roll_b, et_pitch_b, et_yaw_b;

    int ccd_data0_a, ccd_data1_a, roll_a, pitch_a, yaw_a;
    int ccd_data0_b, ccd_data1_b, roll_b, pitch_b, yaw_b;
    double reada1, reada2, reada3, reada4, reada5;
    double readb1, readb2, readb3, readb4, readb5;

    private SocketUitls socketUitls = new SocketUitls();
    private boolean socket;
    private EditText[] editText1;
    private EditText[] editText2;

    SQLiteDeviceHelpter dbHelper;
    SQLiteDatabase db;

    private SharedPreferences sharedUpload;
    private SharedPreferences.Editor editorUpload;

    private SharedPreferences sharedUpload2;
    private SharedPreferences.Editor editorUpload2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back_main);
        llCalibration = findViewById(R.id.ll_calibration);
        llUpload = findViewById(R.id.ll_upload);
        llReadBack = findViewById(R.id.ll_read_back);
        llAdmin = findViewById(R.id.ll_administration);

        imgBack.setOnClickListener(this);
        llCalibration.setOnClickListener(this);
        llUpload.setOnClickListener(this);
        llReadBack.setOnClickListener(this);
        llAdmin.setOnClickListener(this);

        dbHelper = new SQLiteDeviceHelpter(this, dbHelper.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库

        sharedUpload = getSharedPreferences("Upload1", MODE_PRIVATE);
        if (sharedUpload != null) {
            sharedUpload.edit().clear().commit();
        }

        sharedUpload2 = getSharedPreferences("Upload2", MODE_PRIVATE);
        if (sharedUpload2 != null) {
            sharedUpload2.edit().clear().commit();
        }

        socket = socketUitls.openSocket();
        if (socket) {
            socketUitls.setOnDataReceiveListener(new SocketUitls.OnDataReceiveListener() {
                @Override
                public void onDataReceive(byte[] buffer, int size) {
                    Log.i(TAG, "onDataReceive: 接收到的数据--" + UITools.bytesToHexString(buffer));
                    int msgType = UITools.getUnsignedByte(buffer[6]);
                    Log.i(TAG, "run: 消息类型-" + msgType + "--" + UITools.byteToHex(buffer[6]));
                    if (msgType == 43) {
                        int channel = UITools.getUnsignedByte(buffer[8]);
                        Log.i(TAG, "run: 通道-" + channel + "--" + UITools.byteToHex(buffer[8]));
                        if (channel == 1) {
                            reada1 = UITools.Ccd_data(UITools.M(buffer[14], buffer[15]));
                            reada2 = UITools.Ccd_data(UITools.M(buffer[16], buffer[17]));
                            reada3 = UITools.Roll(buffer[18], buffer[19]);
                            reada4 = UITools.Pitch(buffer[20], buffer[21]);
                            reada5 = UITools.Yaw(buffer[22], buffer[23]);
                        } else if (channel == 2) {
                            readb1 = UITools.Ccd_data(UITools.M(buffer[14], buffer[15]));
                            readb2 = UITools.Ccd_data(UITools.M(buffer[16], buffer[17]));
                            readb3 = UITools.Roll(buffer[18], buffer[19]);
                            readb4 = UITools.Pitch(buffer[20], buffer[21]);
                            readb5 = UITools.Yaw(buffer[22], buffer[23]);

                        }
                        ContentValues values = new ContentValues();
                        String deviceId = UITools.byteToHex(buffer[10]) + UITools.byteToHex(buffer[11])
                                + UITools.byteToHex(buffer[12]) + UITools.byteToHex(buffer[13]);
                        Log.i(TAG, "设备ID: " + deviceId + "--" + reada1 + "--" + readb1);
                        values.put("device_id", deviceId);
                        values.put("reada1", reada1);
                        values.put("reada2", reada2);
                        values.put("reada3", reada3);
                        values.put("reada4", reada4);
                        values.put("reada5", reada5);
                        values.put("readb1", readb1);
                        values.put("readb2", readb2);
                        values.put("readb3", readb3);
                        values.put("readb4", readb4);
                        values.put("readb5", readb5);
                        long rowid = db.insert(dbHelper.TB_NAME, null, values);
                        if (rowid == -1) {
                            Log.i(TAG, "onClick: --数据插入失败");
                        } else {
                            Log.i(TAG, "onClick: --数据插入成功");
                        }
                    }
                }
            });
        } else {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(CalibrationActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_calibration://标定
                AlertDialog.Builder builder = new AlertDialog.Builder(CalibrationActivity.this);
                builder.setTitle(getResources().getString(R.string.str_device_selection));
                builder.setMessage(getResources().getString(R.string.str_please_select_device));
                builder.setPositiveButton(getResources().getString(R.string.str_device2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendCommand(Constants.calibration2());
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CalibrationActivity.this);
                        View viewDialog1 = View.inflate(CalibrationActivity.this, R.layout.dialog_calibration_success, null);
                        builder1.setView(viewDialog1);
                        final AlertDialog alertDialog1 = builder1.create();
                        alertDialog1.create();
                        alertDialog1.show();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.str_device1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendCommand(Constants.calibration());
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(CalibrationActivity.this);
                        View viewDialog2 = View.inflate(CalibrationActivity.this, R.layout.dialog_calibration_success, null);
                        builder2.setView(viewDialog2);
                        final AlertDialog alertDialog2 = builder2.create();
                        alertDialog2.create();
                        alertDialog2.show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.create();
                alertDialog.show();
                break;
            case R.id.ll_upload://上传
                AlertDialog.Builder builderUpload = new AlertDialog.Builder(CalibrationActivity.this);
                builderUpload.setTitle(getResources().getString(R.string.str_device_selection));
                builderUpload.setMessage(getResources().getString(R.string.str_please_select_device));
                builderUpload.setNegativeButton(getResources().getString(R.string.str_device1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builderUpload1 = new AlertDialog.Builder(CalibrationActivity.this);
                        View viewDialogUpload1 = View.inflate(CalibrationActivity.this, R.layout.dialog_upload1, null);
                        builderUpload1.setView(viewDialogUpload1);
                        et_ccd_data0_a = viewDialogUpload1.findViewById(R.id.et_ccd_data0_a);
                        et_ccd_data1_a = viewDialogUpload1.findViewById(R.id.et_ccd_data1_a);
                        et_roll_a = viewDialogUpload1.findViewById(R.id.et_roll_a);
                        et_pitch_a = viewDialogUpload1.findViewById(R.id.et_pitch_a);
                        et_yaw_a = viewDialogUpload1.findViewById(R.id.et_yaw_a);
                        btnModify1 = viewDialogUpload1.findViewById(R.id.btn_modify1);
                        btnSave1 = viewDialogUpload1.findViewById(R.id.btn_keep1);
                        final AlertDialog alertDialogUpload1 = builderUpload1.create();
                        sharedUpload = getSharedPreferences("Upload1", MODE_PRIVATE);
                        editorUpload = sharedUpload.edit();
                        alertDialogUpload1.create();
                        alertDialogUpload1.setCanceledOnTouchOutside(false);
                        alertDialogUpload1.show();
                        editText1 = new EditText[]{et_ccd_data0_a, et_ccd_data1_a, et_roll_a, et_pitch_a, et_yaw_a};
                        ccd_data0_a = sharedUpload.getInt("ccd_data0_a", 0);
                        if (ccd_data0_a != 0) {
                            UITools.setEditFocusable(editText1, false);
                            et_ccd_data0_a.setText(ccd_data0_a + "");
                            et_ccd_data1_a.setText(sharedUpload.getInt("ccd_data1_a", 0) + "");
                            et_roll_a.setText(sharedUpload.getInt("roll_a", 0) + "");
                            et_pitch_a.setText(sharedUpload.getInt("pitch_a", 0) + "");
                            et_yaw_a.setText(sharedUpload.getInt("yaw_a", 0) + "");
                        } else {
                            UITools.setEditFocusable(editText1, true);
                        }
                        btnSave1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (UITools.isNotEmpty(et_ccd_data0_a.getText().toString().trim()) && UITools.isNotEmpty(et_ccd_data1_a.getText().toString().trim())
                                        && UITools.isNotEmpty(et_roll_a.getText().toString().trim()) && UITools.isNotEmpty(et_pitch_a.getText().toString().trim())
                                        && UITools.isNotEmpty(et_yaw_a.getText().toString().trim())) {
                                    ccd_data0_a = Integer.valueOf(et_ccd_data0_a.getText().toString().trim()).intValue();
                                    ccd_data1_a = Integer.valueOf(et_ccd_data1_a.getText().toString().trim()).intValue();
                                    roll_a = Integer.valueOf(et_roll_a.getText().toString().trim()).intValue();
                                    pitch_a = Integer.valueOf(et_pitch_a.getText().toString().trim()).intValue();
                                    yaw_a = Integer.valueOf(et_yaw_a.getText().toString().trim()).intValue();
                                    editorUpload.putInt("ccd_data0_a", ccd_data0_a);
                                    editorUpload.putInt("ccd_data1_a", ccd_data1_a);
                                    editorUpload.putInt("roll_a", roll_a);
                                    editorUpload.putInt("pitch_a", pitch_a);
                                    editorUpload.putInt("yaw_a", yaw_a);
                                    UITools.setEditFocusable(editText1, false);
                                    byte[] Upload1 = Constants.upload((byte) UITools.Low(ccd_data0_a), (byte) UITools.High(ccd_data0_a),
                                            (byte) UITools.Low(ccd_data1_a), (byte) UITools.High(ccd_data1_a),
                                            (byte) UITools.Low(roll_a), (byte) UITools.High(roll_a),
                                            (byte) UITools.Low(pitch_a), (byte) UITools.High(pitch_a),
                                            (byte) UITools.Low(yaw_a), (byte) UITools.High(yaw_a));
                                    sendCommand(Upload1);
                                    Log.i(TAG, "onClick: " + UITools.byteToHex(Upload1[14]) + "-" + UITools.byteToHex(Upload1[15])
                                            + UITools.byteToHex(Upload1[16]) + "-" + UITools.byteToHex(Upload1[17])
                                            + UITools.byteToHex(Upload1[18]) + "-" + UITools.byteToHex(Upload1[19])
                                            + UITools.byteToHex(Upload1[20]) + "-" + UITools.byteToHex(Upload1[21])
                                            + UITools.byteToHex(Upload1[22]) + "-" + UITools.byteToHex(Upload1[23]));
                                    alertDialogUpload1.dismiss();
                                    editorUpload.commit();
                                    UITools.showToast(CalibrationActivity.this, R.string.str_upload_success);
                                } else {
                                    UITools.showToast(CalibrationActivity.this, R.string.str_data_cannot_empty);
                                }
                            }
                        });

                        btnModify1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UITools.setEditFocusable(editText1, true);
                            }
                        });
                    }
                });
                builderUpload.setPositiveButton(getResources().getString(R.string.str_device2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builderUpload2 = new AlertDialog.Builder(CalibrationActivity.this);
                        View viewDialogUpload2 = View.inflate(CalibrationActivity.this, R.layout.dialog_upload2, null);
                        builderUpload2.setView(viewDialogUpload2);
                        et_ccd_data0_b = viewDialogUpload2.findViewById(R.id.et_ccd_data0_b);
                        et_ccd_data1_b = viewDialogUpload2.findViewById(R.id.et_ccd_data1_b);
                        et_roll_b = viewDialogUpload2.findViewById(R.id.et_roll_b);
                        et_pitch_b = viewDialogUpload2.findViewById(R.id.et_pitch_b);
                        et_yaw_b = viewDialogUpload2.findViewById(R.id.et_yaw_b);
                        btnModify2 = viewDialogUpload2.findViewById(R.id.btn_modify2);
                        btnSave2 = viewDialogUpload2.findViewById(R.id.btn_keep2);
                        sharedUpload2 = getSharedPreferences("Upload2", MODE_PRIVATE);
                        editorUpload2 = sharedUpload2.edit();
                        editText2 = new EditText[]{et_ccd_data0_b, et_ccd_data1_b, et_roll_b, et_pitch_b, et_yaw_b};
                        final AlertDialog alertDialogUpload2 = builderUpload2.create();
                        ccd_data0_b = sharedUpload2.getInt("ccd_data0_b", 0);
                        Log.i(TAG, "onClick: " + ccd_data0_b);
                        if (ccd_data0_b != 0) {
                            UITools.setEditFocusable(editText2, false);
                            et_ccd_data0_b.setText(ccd_data0_b + "");
                            et_ccd_data1_b.setText(sharedUpload2.getInt("ccd_data1_b", 0) + "");
                            et_roll_b.setText(sharedUpload2.getInt("roll_b", 0) + "");
                            Log.i(TAG, "onClick: " + sharedUpload2.getInt("roll_b", 0));
                            et_pitch_b.setText(sharedUpload2.getInt("pitch_b", 0) + "");
                            et_yaw_b.setText(sharedUpload2.getInt("yaw_b", 0) + "");
                        } else {
                            UITools.setEditFocusable(editText2, true);
                        }
                        alertDialogUpload2.create();
                        alertDialogUpload2.setCanceledOnTouchOutside(false);
                        alertDialogUpload2.show();
                        btnSave2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (UITools.isNotEmpty(et_ccd_data0_b.getText().toString().trim()) && UITools.isNotEmpty(et_ccd_data1_b.getText().toString().trim())
                                        && UITools.isNotEmpty(et_roll_b.getText().toString().trim()) && UITools.isNotEmpty(et_pitch_b.getText().toString().trim())
                                        && UITools.isNotEmpty(et_yaw_b.getText().toString().trim())) {
                                    ccd_data0_b = Integer.valueOf(et_ccd_data0_b.getText().toString().trim()).intValue();
                                    ccd_data1_b = Integer.valueOf(et_ccd_data1_b.getText().toString().trim()).intValue();
                                    roll_b = Integer.valueOf(et_roll_b.getText().toString().trim()).intValue();
                                    pitch_b = Integer.valueOf(et_pitch_b.getText().toString().trim()).intValue();
                                    yaw_b = Integer.valueOf(et_yaw_b.getText().toString().trim()).intValue();
                                    editorUpload2.putInt("ccd_data0_b", ccd_data0_b);
                                    editorUpload2.putInt("ccd_data1_b", ccd_data1_b);
                                    editorUpload2.putInt("roll_b", roll_b);
                                    editorUpload2.putInt("pitch_b", pitch_b);
                                    editorUpload2.putInt("yaw_b", yaw_b);
                                    UITools.setEditFocusable(editText2, false);
                                    byte[] Upload2 = Constants.upload2((byte) UITools.Low(ccd_data0_b), (byte) UITools.High(ccd_data0_b),
                                            (byte) UITools.Low(ccd_data1_b), (byte) UITools.High(ccd_data1_b),
                                            (byte) UITools.Low(roll_b), (byte) UITools.High(roll_b),
                                            (byte) UITools.Low(pitch_b), (byte) UITools.High(pitch_b),
                                            (byte) UITools.Low(yaw_b), (byte) UITools.High(yaw_b));
                                    sendCommand(Upload2);
                                    Log.i(TAG, "onClick: " + UITools.byteToHex(Upload2[14]) + "-" + UITools.byteToHex(Upload2[15])
                                            + UITools.byteToHex(Upload2[16]) + "-" + UITools.byteToHex(Upload2[17])
                                            + UITools.byteToHex(Upload2[18]) + "-" + UITools.byteToHex(Upload2[19])
                                            + UITools.byteToHex(Upload2[20]) + "-" + UITools.byteToHex(Upload2[21])
                                            + UITools.byteToHex(Upload2[22]) + "-" + UITools.byteToHex(Upload2[23]));
                                    alertDialogUpload2.dismiss();
                                    editorUpload2.commit();
                                    UITools.showToast(CalibrationActivity.this, R.string.str_upload_success);
                                } else {
                                    UITools.showToast(CalibrationActivity.this, R.string.str_data_cannot_empty);
                                }
                            }
                        });
                        btnModify2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UITools.setEditFocusable(editText2, true);
                            }
                        });
                    }
                });
                AlertDialog alertDialogUpload = builderUpload.create();
                alertDialogUpload.setCanceledOnTouchOutside(false);
                alertDialogUpload.create();
                alertDialogUpload.show();
                break;
            case R.id.ll_read_back://回读
                AlertDialog.Builder builderRead = new AlertDialog.Builder(CalibrationActivity.this);
                builderRead.setTitle(getResources().getString(R.string.str_device_selection));
                builderRead.setMessage(getResources().getString(R.string.str_please_select_device));
                builderRead.setPositiveButton(getResources().getString(R.string.str_device2), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendCommand(Constants.reading_standard2());
                        AlertDialog.Builder builderRead2 = new AlertDialog.Builder(CalibrationActivity.this);
                        View viewReadBack2 = View.inflate(CalibrationActivity.this, R.layout.dialog_read_back2, null);
                        builderRead2.setView(viewReadBack2);
                        tv_ccd_data0_b = viewReadBack2.findViewById(R.id.tv2_ccd_data0_b);
                        tv_ccd_data1_b = viewReadBack2.findViewById(R.id.tv2_ccd_data1_b);
                        tv_roll_b = viewReadBack2.findViewById(R.id.tv2_roll_b);
                        tv_pitch_b = viewReadBack2.findViewById(R.id.tv2_pitch_b);
                        tv_yaw_b = viewReadBack2.findViewById(R.id.tv2_yaw_b);
                        final AlertDialog alertDialogRead2 = builderRead2.create();
                        alertDialogRead2.create();
                        alertDialogRead2.show();
                        tv_ccd_data0_b.setText(readb1 + "");
                        tv_ccd_data1_b.setText(readb2 + "");
                        tv_roll_b.setText(readb3 + "");
                        tv_pitch_b.setText(readb4 + "");
                        tv_yaw_b.setText(readb5 + "");
                    }
                });
                builderRead.setNegativeButton(getResources().getString(R.string.str_device1), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendCommand(Constants.reading_standard());
                        AlertDialog.Builder builderRead1 = new AlertDialog.Builder(CalibrationActivity.this);
                        View viewReadBack1 = View.inflate(CalibrationActivity.this, R.layout.dialog_read_back1, null);
                        builderRead1.setView(viewReadBack1);
                        tv_ccd_data0_a = viewReadBack1.findViewById(R.id.tv1_ccd_data0_a);
                        tv_ccd_data1_a = viewReadBack1.findViewById(R.id.tv1_ccd_data1_a);
                        tv_roll_a = viewReadBack1.findViewById(R.id.tv1_roll_a);
                        tv_pitch_a = viewReadBack1.findViewById(R.id.tv1_pitch_a);
                        tv_yaw_a = viewReadBack1.findViewById(R.id.tv1_yaw_a);
                        final AlertDialog alertDialogRead1 = builderRead1.create();
                        alertDialogRead1.create();
                        alertDialogRead1.show();
                        Log.i(TAG, "onClick: ----" + reada1);
                        tv_ccd_data0_a.setText(reada1 + "");
                        tv_ccd_data1_a.setText(reada2 + "");
                        tv_roll_a.setText(reada3 + "");
                        tv_pitch_a.setText(reada4 + "");
                        tv_yaw_a.setText(reada5 + "");
                    }
                });
                AlertDialog alertDialogRead = builderRead.create();
                alertDialogRead.create();
                alertDialogRead.setCanceledOnTouchOutside(false);
                alertDialogRead.show();
                break;
            case R.id.ll_administration:
                Intent intent3 = new Intent(CalibrationActivity.this, DeviceManagerActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
    }

    private void sendCommand(byte[] command) {
        if (socket) {
            socketUitls.sendOrder(command);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(CalibrationActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (socket) {
            socketUitls.closeSocket();
        }
    }
}
