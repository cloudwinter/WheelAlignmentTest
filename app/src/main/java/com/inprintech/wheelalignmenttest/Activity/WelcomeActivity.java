package com.inprintech.wheelalignmenttest.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends BaseActivity implements Animation.AnimationListener {
    private static final String TAG = "WelcomeActivity";
    ;
    private LinearLayout llWelcome;

    private Animation alphaAnimation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();

        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                } else {
                    //这里就是权限打开之后自己要操作的逻辑
                }
            }
        }
    }

    private void initView() {
        llWelcome = findViewById(R.id.ll_welcome);

        alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
        alphaAnimation.setFillEnabled(true); //启动Fill保持
        alphaAnimation.setFillAfter(true);  //设置动画的最后一帧是保持在View上面
        llWelcome.setAnimation(alphaAnimation);//给你的父布局设置动画
        alphaAnimation.setAnimationListener(this);  //为动画设置监听

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        SharedPreferences spRegisger = mContext.getSharedPreferences("isRegister", MODE_PRIVATE);
        SharedPreferences spRemember = mContext.getSharedPreferences("Remember", MODE_PRIVATE);

        String phone = spRegisger.getString("phone", "");
        String isLogin = spRemember.getString("isRemember", "");
        Log.i(TAG, "onAnimationEnd: --phone--" + phone + "---isLogin---" + isLogin);
        if (UITools.isNotEmpty(phone) && UITools.isNotEmpty(isLogin) && isLogin.equals("yes_remember")) {
            Log.i(TAG, "onAnimationEnd: --MainActivity--");
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (UITools.isNotEmpty(phone) && UITools.isNotEmpty(isLogin) && isLogin.equals("no_remember")) {
            Log.i(TAG, "onAnimationEnd: --LoginActivity--");
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.i(TAG, "onAnimationEnd: --RegisterActivity--");
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

        SharedPreferences sharedLanguage = getSharedPreferences("Language", MODE_PRIVATE);
        String str = sharedLanguage.getString("isLanguage", "");
        UITools.shiftLanguage(str, WelcomeActivity.this,false);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在欢迎界面屏蔽BACK和HOME键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME)
        {return false; }
        return false;
    }
}
