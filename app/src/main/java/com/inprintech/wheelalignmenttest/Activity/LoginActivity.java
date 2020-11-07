package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";

    private EditText etPhone, etPwd;
    private Button btnRegister;
    private CheckBox cbRememberPwd;

    private String phone, pwd;
    private String userPhone, userPwd;

    SharedPreferences spRemember;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        cbRememberPwd = findViewById(R.id.cb_remember_pwd);
        etPhone = findViewById(R.id.et_input_phone);
        etPwd = findViewById(R.id.et_input_pwd);
        btnRegister = findViewById(R.id.btn_login);

        SharedPreferences spRegister = getSharedPreferences("isRegister", MODE_PRIVATE);
        userPhone = spRegister.getString("phone", "");
        userPwd = spRegister.getString("pwd", "");
        Log.i(TAG, "initView: phone:" + userPhone + "--pwd--" + userPwd);

        cbRememberPwd.setSelected(false);
        spRemember = getSharedPreferences("Remember", MODE_PRIVATE);
        editor = spRemember.edit();
        editor.putString("isRemember", "no_remember");
        editor.commit();
        btnRegister.setOnClickListener(this);
        cbRememberPwd.setOnCheckedChangeListener(changeListener);
    }

    CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                editor.putString("isRemember", "yes_remember");
            } else {
                editor.putString("isRemember", "no_remember");
            }
            editor.commit();
        }
    };

    @Override
    public void onClick(View view) {
        phone = etPhone.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if (UITools.isNotEmpty(phone) && UITools.isNotEmpty(pwd)) {
            if (phone.equals(userPhone) && pwd.equals(userPwd)) {
                switch (view.getId()) {
                    case R.id.btn_login:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        UITools.showToast(LoginActivity.this, R.string.str_login_success);
                        break;
                }
            } else {
                UITools.showToast(LoginActivity.this, R.string.str_account_pwd_wrong);
            }
        } else {
            UITools.showToast(LoginActivity.this, R.string.str_account_pwd_null);
        }
    }
}
