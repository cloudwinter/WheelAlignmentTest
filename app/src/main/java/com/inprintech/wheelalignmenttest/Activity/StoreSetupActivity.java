package com.inprintech.wheelalignmenttest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.inprintech.wheelalignmenttest.BaseActivity;
import com.inprintech.wheelalignmenttest.R;
import com.inprintech.wheelalignmenttest.Utils.UITools;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 店家设置
 */
public class StoreSetupActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "StoreSetupActivity";

    private ImageView imgBack, imgLayout;
    private TextView tvEdit;
    private LinearLayout llConfirmMsg, llShareWechat;
    private EditText etShopName, etAddress, etTelephone, etFax, etZipCode, etOperator;
    private LinearLayout llStoreSetup;

    private SharedPreferences sharedStore;
    private SharedPreferences.Editor editorStore;

    private EditText[] editTexts;
    private String ShopName, Address, Telephone, Fax, ZipCode, Operator;
    private static final int THUMB_SIZE = 150;

    IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setup);

        initView();

        wxapi = WXAPIFactory.createWXAPI(this, "你的微信appid", true);
        wxapi.registerApp("你的微信appid");
    }

    private void initView() {
        imgLayout = findViewById(R.id.img_layout);
        imgBack = findViewById(R.id.img_back_main);
        tvEdit = findViewById(R.id.tv_edit);
        llConfirmMsg = findViewById(R.id.ll_confirm_msg);
        llShareWechat = findViewById(R.id.ll_share_wechat);
        etShopName = findViewById(R.id.et_shop_name);
        etAddress = findViewById(R.id.et_address);
        etTelephone = findViewById(R.id.et_telephone);
        etFax = findViewById(R.id.et_fax);
        etZipCode = findViewById(R.id.et_zip_code);
        etOperator = findViewById(R.id.et_operator);
        llStoreSetup = findViewById(R.id.ll_stor_setup);

        tvEdit.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        llConfirmMsg.setOnClickListener(this);
        llShareWechat.setOnClickListener(this);

        editTexts = new EditText[]{etShopName, etAddress, etTelephone, etFax, etZipCode, etOperator};
        sharedStore = getSharedPreferences("Store_Setup", MODE_PRIVATE);
        ShopName = sharedStore.getString("shopname", "");
        Address = sharedStore.getString("address", "");
        Telephone = sharedStore.getString("telephone", "");
        Fax = sharedStore.getString("fax", "");
        ZipCode = sharedStore.getString("zipcode", "");
        Operator = sharedStore.getString("operator", "");
        if (UITools.isNotEmpty(ShopName)){
            UITools.setEditFocusable(editTexts, false);
            etShopName.setText(ShopName);
            etAddress.setText(Address);
            etTelephone.setText(Telephone);
            etFax.setText(Fax);
            etZipCode.setText(ZipCode);
            etOperator.setText(Operator);
        } else {
            UITools.setEditFocusable(editTexts, true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_confirm_msg:
                sharedStore = getSharedPreferences("Store_Setup", MODE_PRIVATE);
                editorStore = sharedStore.edit();
                ShopName = etShopName.getText().toString().trim();
                Address = etAddress.getText().toString().trim();
                Telephone = etTelephone.getText().toString().trim();
                Fax = etFax.getText().toString().trim();
                ZipCode = etZipCode.getText().toString().trim();
                Operator = etOperator.getText().toString().trim();
                editorStore.putString("shopname", ShopName);
                editorStore.putString("address", Address);
                editorStore.putString("telephone", Telephone);
                editorStore.putString("fax", Fax);
                editorStore.putString("zipcode", ZipCode);
                editorStore.putString("operator", Operator);
                editorStore.commit();
                Intent intent = new Intent(StoreSetupActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                UITools.showToast(StoreSetupActivity.this,R.string.str_save_successfully);
                break;
            case R.id.ll_share_wechat:
//                llStoreSetup.setDrawingCacheEnabled(true);
//                llStoreSetup.buildDrawingCache();
//                Bitmap bitmap = Bitmap.createBitmap(llStoreSetup.getDrawingCache());
//                llStoreSetup.setDrawingCacheEnabled(false);
                shareText("店名：" + ShopName + "\n地址：" + Address + "\n电话：" + Telephone
                        + "\n传真：" + Fax + "\n邮编：" + ZipCode + "\n操作员：" + Operator);
//                sharePicture(bitmap, 0);
                break;
            case R.id.img_back_main:
                Intent intentBack = new Intent(StoreSetupActivity.this, SettingsActivity.class);
                startActivity(intentBack);
                finish();
                break;
            case R.id.tv_edit:
                UITools.setEditFocusable(editTexts, true);
                break;
        }
    }

    private void shareText(String shareTxt) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareTxt);
        shareIntent.setType("text/plain");
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        llStoreSetup.destroyDrawingCache();
    }

    private void sharePicture(Bitmap bitmap, int shareType) {
        WXImageObject imgObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = UITools.bmpToByteArray(thumbBitmap);  //设置缩略图

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("imgshareappdata");
        req.message = msg;
        req.scene = shareType == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        wxapi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(StoreSetupActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
