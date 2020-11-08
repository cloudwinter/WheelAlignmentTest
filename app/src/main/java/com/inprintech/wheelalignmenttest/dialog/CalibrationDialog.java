package com.inprintech.wheelalignmenttest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.inprintech.wheelalignmenttest.R;
import com.xyzlf.share.library.util.ToastUtil;

import androidx.annotation.NonNull;

/**
 * 标定对话框
 * Created by xiayundong on 2020/11/8.
 */
public class CalibrationDialog extends Dialog implements View.OnClickListener {

    private CheckBox t1CheckBox;
    private CheckBox t2CheckBox;
    private Button device1Button;
    private Button device2Button;

    private Context mContext;

    public CalibrationDialog(@NonNull Context context) {
        super(context,R.style.custom_calibration_dialog);
        initView(context);
    }

    public CalibrationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        mButtonClickListener = buttonClickListener;
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_calibration, null);
        t1CheckBox = view.findViewById(R.id.checkbox_t1);
        t1CheckBox.setChecked(true);
        t2CheckBox = view.findViewById(R.id.checkbox_t2);
        device1Button = view.findViewById(R.id.btn_device1);
        device2Button = view.findViewById(R.id.btn_device2);
        device1Button.setOnClickListener(this);
        device2Button.setOnClickListener(this);
        setContentView(view);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        view.setLayoutParams(layoutParams);

        getWindow().setGravity(Gravity.CENTER);
    }


    private ButtonClickListener mButtonClickListener;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_device1:
                if (getSelected() == 0) {
                    ToastUtil.showToast(mContext, "请至少选择一个", true);
                }
                if (mButtonClickListener != null) {
                    dismiss();
                    mButtonClickListener.onClick(1,getSelectByte(getSelected()));
                }
                break;
            case R.id.btn_device2:
                if (getSelected() == 0) {
                    ToastUtil.showToast(mContext, "请至少选择一个", true);
                }
                if (mButtonClickListener != null) {
                    dismiss();
                    mButtonClickListener.onClick(2,getSelectByte(getSelected()));
                }
                break;
            default:
                break;
        }
    }

    private byte getSelectByte(int selectT) {
        if (selectT == 3) {
            return (byte) 0x03;
        }
        if (selectT == 2) {
            return (byte) 0x02;
        }
        if (selectT == 1) {
            return (byte) 0x01;
        }
        return (byte) 0x00;
    }


    private int getSelected(){
        if (t1CheckBox.isChecked()&&t2CheckBox.isChecked()){
            return 3;
        } else if(!t1CheckBox.isChecked()&&t2CheckBox.isChecked()){
            return 2;
        } else if(t1CheckBox.isChecked()&&!t2CheckBox.isChecked()){
            return 1;
        }
        return 0;
    }


    public interface ButtonClickListener {
        // device(1,2) | selectT (1表示选中T1,2表示选中T2,3表示都选中)
        void onClick(int device,byte selectT);
    }
}
