package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;

import java.util.Locale;

/**
 * 语言设置
 */
public class LanguageSetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgBack;
    private ImageView imgChinese, imgEnglish;
    private RelativeLayout rlEnglish, rlChinese;

    private String isCNorEN;
    SharedPreferences spLanguage;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_set);

        initView();
    }

    private void initView() {
        imgChinese = findViewById(R.id.img_chinese);
        imgEnglish = findViewById(R.id.img_english);
        imgBack = findViewById(R.id.img_back_main);
        rlChinese = findViewById(R.id.rl_selection_chinese);
        rlEnglish = findViewById(R.id.rl_selection_english);

        imgBack.setOnClickListener(this);
        rlEnglish.setOnClickListener(this);
        rlChinese.setOnClickListener(this);

        SharedPreferences spGetLanguage = getSharedPreferences("Language", MODE_PRIVATE);
        isCNorEN = spGetLanguage.getString("isLanguage", "");
        isLanguage(isCNorEN);
    }

    @Override
    public void onClick(View v) {
        spLanguage = getSharedPreferences("Language", MODE_PRIVATE);
        editor = spLanguage.edit();
        switch (v.getId()) {
            case R.id.img_back_main:
                Intent intent = new Intent(LanguageSetActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_selection_chinese:
                isLanguage("zh");
                editor.putString("isLanguage", "zh");
                editor.commit();
                UITools.shiftLanguage("zh", LanguageSetActivity.this, true);
                break;
            case R.id.rl_selection_english:
                isLanguage("en");
                editor.putString("isLanguage", "en");
                editor.commit();
                UITools.shiftLanguage("en", LanguageSetActivity.this, true);
                break;
        }
    }


    private void isLanguage(String isCNorEN) {
        if (isCNorEN.equals("zh")) {
            imgChinese.setVisibility(View.VISIBLE);
            imgEnglish.setVisibility(View.GONE);
        } else if (isCNorEN.equals("en")) {
            imgEnglish.setVisibility(View.VISIBLE);
            imgChinese.setVisibility(View.GONE);
        } else {
            imgEnglish.setVisibility(View.GONE);
            imgChinese.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(LanguageSetActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
