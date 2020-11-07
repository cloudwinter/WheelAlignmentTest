package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.inprintech.wheelalignmenttest.Adapter.SettingsAdapter;
import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置
 */
public class SettingsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView listSetup;
    private ImageView imgBack;

    private SettingsAdapter adapter;
    private List<Map<String, ?>> mapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    private void initView() {
        imgBack = findViewById(R.id.img_back_main);
        listSetup = findViewById(R.id.list_setup);

        imgBack.setOnClickListener(this);

        mapList = getMapList();
        adapter = new SettingsAdapter(this, mapList);
        listSetup.setAdapter(adapter);
        listSetup.setOnItemClickListener(this);
    }

    private List<Map<String, ?>> getMapList() {
        List<Map<String, ?>> mapArrayList = new ArrayList<>();

        Map<String, Object> hashMap0 = new HashMap<>();
        hashMap0.put("imgIcon", R.mipmap.store_setup);
        hashMap0.put("tvTitle", getResources().getString(R.string.str_store_setup));
        mapArrayList.add(hashMap0);

        Map<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("imgIcon", R.mipmap.language_setup);
        hashMap1.put("tvTitle", getResources().getString(R.string.str_language));
        mapArrayList.add(hashMap1);

        Map<String, Object> hashMap2 = new HashMap<>();
        hashMap2.put("imgIcon", R.mipmap.diagnosis);
        hashMap2.put("tvTitle", getResources().getString(R.string.str_diagnosis));
        mapArrayList.add(hashMap2);

        Map<String, Object> hashMap3 = new HashMap<>();
        hashMap3.put("imgIcon", R.mipmap.calibration);
        hashMap3.put("tvTitle", getResources().getString(R.string.str_calibration));
        mapArrayList.add(hashMap3);


        Map<String, Object> hashMap4 = new HashMap<>();
        hashMap4.put("imgIcon", R.mipmap.store_setup);
        hashMap4.put("tvTitle", getResources().getString(R.string.str_test_setup));
        mapArrayList.add(hashMap4);

        return mapArrayList;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                Intent intent0 = new Intent(SettingsActivity.this, StoreSetupActivity.class);
                startActivity(intent0);
                finish();
                break;
            case 1:
                Intent intent1 = new Intent(SettingsActivity.this, LanguageSetActivity.class);
                startActivity(intent1);
                finish();
                break;
            case 2:
                Intent intent2 = new Intent(SettingsActivity.this, DiagnosisActivity.class);
                startActivity(intent2);
                finish();
                break;
            case 3:
                Intent intent3 = new Intent(SettingsActivity.this, CalibrationActivity.class);
                startActivity(intent3);
                finish();
                break;
            case 4:
                Intent intent4 = new Intent(SettingsActivity.this, TestSetupActivity.class);
                startActivity(intent4);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
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
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
