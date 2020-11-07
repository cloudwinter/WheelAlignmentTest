package com.inprintech.wheelalignmenttest.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.Adapter.UserRecordAdapter;
import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.DataBase.SQLiteHelpter;
import com.inprintech.wheelalignmenttest.Model.UserRecord;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.ActivityCollector;
import com.inprintech.wheelalignmenttest.Utils.FilterListener;
import com.inprintech.wheelalignmenttest.Utils.UITools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户记录列表
 */
public class UserRecordActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "UserRecordActivity";

    private ImageView imgBack;
    private ListView lvRecord;
    private EditText etRegisterNum;
    private TextView tvEdit;
    private LinearLayout llDel;
    private Button btnDel;
    private CheckBox cbAllSel;

    List<UserRecord> recordList = new ArrayList<>();
    private boolean seeOrdel = false;
    private UserRecordAdapter recordAdapter;
    SQLiteHelpter dbHelper;
    SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"-----onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_record);

        initView();
    }

    private void initView() {
        btnDel = findViewById(R.id.btn_del);
        cbAllSel = findViewById(R.id.cb_all_election);
        llDel = findViewById(R.id.ll_del);
        tvEdit = findViewById(R.id.tv_edit);
        imgBack = findViewById(R.id.img_back_main);
        etRegisterNum = findViewById(R.id.et_register_number);
        lvRecord = findViewById(R.id.lv_record);

        cbAllSel.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        dbHelper = new SQLiteHelpter(this, dbHelper.DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();// 打开数据库
        db = dbHelper.getReadableDatabase();

        setData();
        setListeners();
    }

    /**
     * 设置Adapter
     */
    private void setData() {
        initData();
        recordAdapter = new UserRecordAdapter(UserRecordActivity.this, recordList, new FilterListener() {
            @Override
            public void getFilterData(List<UserRecord> list) {
                Log.i(TAG, "getFilterData: --" + list.toArray().toString());
            }
        });
        lvRecord.setAdapter(recordAdapter);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        recordList.clear();
        cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String date = cursor.getString(2);
            String registerNum = cursor.getString(1);
            byte[] autograph = cursor.getBlob(7);
            UserRecord userRecord = new UserRecord();
            userRecord.setRegisterNum(registerNum);
            userRecord.setTime(date);
            userRecord.setAutograph(autograph);
            recordList.add(userRecord);
            cursor.moveToNext();
        }
    }

    /**
     * 输入框监听事件
     */
    private void setListeners() {
        etRegisterNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //编辑框内容改变的时候会执行该方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recordAdapter != null) {
                    recordAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 全选
     */
    public void selectAllList() {
        if (recordAdapter.seeOrdel) {
            for (int i = 0; i < recordList.size(); i++) {
                recordList.get(i).setCheck(true);
            }
            recordAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 全不选
     */
    public void selNoList() {
        if (recordAdapter.seeOrdel) {
            for (int i = 0; i < recordList.size(); i++) {
                recordList.get(i).setCheck(false);
            }
            recordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intentBack = new Intent(UserRecordActivity.this, MainActivity.class);
                startActivity(intentBack);
                finish();
                break;
            case R.id.tv_edit:
                llDel.setVisibility(View.VISIBLE);
                recordAdapter.seeOrdel = !recordAdapter.seeOrdel;
                recordAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_del:
                llDel.setVisibility(View.GONE);
                recordAdapter.seeOrdel = !recordAdapter.seeOrdel;
                List<String> selList = new ArrayList<>();
                for (int i = 0; i < recordList.size(); i++) {
                    if (recordList.get(i).isCheck()) {
                        selList.add(recordList.get(i).getRegisterNum());
                    }
                }
                for (int k = 0; k < selList.size(); k++) {
                    for (int j = 0; j < recordList.size(); j++) {
                        if (selList.get(k).equals(recordList.get(j).getRegisterNum())){
                            Log.i(TAG, "选中的值: " + selList.get(k));
                            Log.i(TAG, "所有的值: " + recordList.get(j).getRegisterNum());
                            recordList.remove(j);
                            db.execSQL("delete from " + dbHelper.TB_NAME + " where register_num=?", new String[]{recordList.get(j).getRegisterNum()});
                        }
                    }
                }
                selList.clear();
                setData();
                break;
            case R.id.cb_all_election:
                if (cbAllSel.isChecked()) {
                    selectAllList();
                } else {
                    selNoList();
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        Log.d(TAG,"-----onRestart");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"-----onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(UserRecordActivity.this, MainActivity.class);
            startActivity(intent);
            Log.d(TAG,"-----onKeyDown  finish UserRecordActivity To MainActivity");
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
