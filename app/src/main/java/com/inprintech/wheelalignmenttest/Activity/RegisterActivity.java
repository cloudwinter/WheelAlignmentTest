package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.Model.UserAccount;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText etPhone, etPwd;
    private Button btnRegister;

    private String phone, pwd;

    private List<UserAccount> accountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {

        etPhone = findViewById(R.id.et_input_phone);
        etPwd = findViewById(R.id.et_input_pwd);
        btnRegister = findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        phone = etPhone.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        if (UITools.isNotEmpty(phone) && UITools.isNotEmpty(pwd)) {
            switch (view.getId()) {
                case R.id.btn_register:
                    SharedPreferences shared = getSharedPreferences("isRegister", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("phone", phone);
                    editor.putString("pwd", pwd);
                    editor.commit();

                    UserAccount userAccount = new UserAccount();
                    userAccount.setAccount(phone);
                    userAccount.setPassword(pwd);
                    accountList.add(userAccount);
                    SharedPreferences sharedList = getSharedPreferences("UserList", MODE_PRIVATE);
                     SharedPreferences.Editor editorList = sharedList.edit();
                    if (null == accountList || accountList.size() <= 0)
                        return;
                    Gson gson = new Gson();
                    //转换成json数据，再保存
                    String strJson = gson.toJson(accountList);
                    editorList.clear();
                    editorList.putString("accountList", strJson);
                    editorList.commit();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    UITools.showToast(RegisterActivity.this, R.string.str_register_success);
                    break;
            }
        } else {
            UITools.showToast(RegisterActivity.this, R.string.str_edit_phone_pwd);
        }
    }
}
