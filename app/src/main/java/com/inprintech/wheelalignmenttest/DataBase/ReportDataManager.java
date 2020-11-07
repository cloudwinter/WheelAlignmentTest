package com.inprintech.wheelalignmenttest.DataBase;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.inprintech.wheelalignmenttest.Model.KingpinModel;
import com.inprintech.wheelalignmenttest.Model.ReportModel;
import com.inprintech.wheelalignmenttest.Model.ReportValue;
import com.inprintech.wheelalignmenttest.Utils.ValidateUtils;
import com.inprintech.wheelalignmenttest.core.RunningContext;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by xiayundong on 2020/4/10.
 */
public class ReportDataManager {

    public static final String SHARE_REPORT_NAME = "user_report";


    private static SharedPreferences getReportShare() {
        return RunningContext.sAppContext.getSharedPreferences(SHARE_REPORT_NAME, MODE_PRIVATE);
    }


    public static ReportModel getReportModel(String key) {
        SharedPreferences share = getReportShare();
        String reportJson = share.getString(key, null);
        if (TextUtils.isEmpty(reportJson)) {
            return null;
        }
        return new Gson().fromJson(reportJson, ReportModel.class);
    }

    private static void setReportModel(String key, ReportModel model) {
        if (TextUtils.isEmpty(key) || model == null) {
            return;
        }
        SharedPreferences.Editor editor = getReportShare().edit();
        editor.putString(key, new Gson().toJson(model));
        editor.commit();
    }

    /**
     * 设置真实的数据值
     * @param val
     * @param reportValue
     * @param before
     */
    private static void setReportValue(String val,ReportValue reportValue, boolean before) {
        if (before) {
            reportValue.setAdjustBefore(val);
        } else {
            reportValue.setAdjustAfter(val);
        }
    }

    /**
     * 获取key
     *
     * @param registerNum 登记号
     * @param select      第几个车轮 sel{i}
     * @return
     */
    public static String genKey(String registerNum, String select) {
        if (TextUtils.isEmpty(registerNum) || TextUtils.isEmpty(select)) {
            return null;
        }
        return String.format("%s_%s", registerNum, select);
    }


    /**
     * 设置同步值
     *
     * @param syncValue 同步值
     * @param before    true表示调整前，false表示调整后
     */
    public static void setSyncValue(String key, String syncValue, boolean before) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        ReportModel reportModel = getReportModel(key);
        if (reportModel == null) {
            reportModel = new ReportModel();
        }
        if (reportModel.getSyncValue() == null) {
            reportModel.setSyncValue(new ReportValue());
        }
        setReportValue(syncValue,reportModel.getSyncValue(),before);
        setReportModel(key,reportModel);
    }


    /**
     * 设置车桥测量
     * @param key
     * @param axleValue
     * @param before
     */
    public static void setAxleValue(String key, String axleValue, boolean before) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        ReportModel reportModel = getReportModel(key);
        if (reportModel == null) {
            reportModel = new ReportModel();
        }
        if (reportModel.getAxleValue() == null) {
            reportModel.setAxleValue(new ReportValue());
        }
        setReportValue(axleValue,reportModel.getAxleValue(),before);
        setReportModel(key,reportModel);
    }


    /**
     * 设置前束测量
     * @param key
     * @param leftAngle
     * @param rightAngle
     * @param totalAngle
     * @param before
     */
    public static void setToeiAngle(String key,String leftAngle,String rightAngle,String totalAngle,boolean before) {
        if (ValidateUtils.isEmpty(key, leftAngle, rightAngle, totalAngle)) {
            return;
        }
        ReportModel reportModel = getReportModel(key);
        if (reportModel == null) {
            reportModel = new ReportModel();
        }
        if (reportModel.getLeftToeinAngle() == null) {
            reportModel.setLeftToeinAngle(new ReportValue());
        }
        if (reportModel.getRightToeinAngle() == null) {
            reportModel.setRightToeinAngle(new ReportValue());
        }
        if (reportModel.getTotalToein() == null) {
            reportModel.setTotalToein(new ReportValue());
        }
        setReportValue(leftAngle,reportModel.getLeftToeinAngle(),before);
        setReportValue(rightAngle,reportModel.getRightToeinAngle(),before);
        setReportValue(totalAngle,reportModel.getTotalToein(),before);
        setReportModel(key,reportModel);
    }


    /**
     * 设置主销测量
     * @param model
     * @param before
     */
    public static void setKinpin(String key,KingpinModel model,boolean before) {
        if (TextUtils.isEmpty(key) || model == null) {
            return;
        }
        ReportModel reportModel = getReportModel(key);
        if (reportModel == null) {
            reportModel = new ReportModel();
        }
        if (reportModel.getLeftCamberAngle() == null) {
            reportModel.setLeftCamberAngle(new ReportValue());
        }
        if (reportModel.getLeftKingpinCasterAngle() == null) {
            reportModel.setLeftKingpinCasterAngle(new ReportValue());
        }
        if (reportModel.getLeftKingpinInAngle() == null) {
            reportModel.setLeftKingpinInAngle(new ReportValue());
        }
        if (reportModel.getRightCamberAngle() == null) {
            reportModel.setRightCamberAngle(new ReportValue());
        }
        if (reportModel.getRightKingpinCasterAngle() == null) {
            reportModel.setRightKingpinCasterAngle(new ReportValue());
        }
        if (reportModel.getRightKingpinInAngle() == null) {
            reportModel.setRightKingpinInAngle(new ReportValue());
        }
        if (model.channel == 1) {
            setReportValue(model.camberAngle,reportModel.getLeftCamberAngle(),before);
            setReportValue(model.casterAngle,reportModel.getLeftKingpinCasterAngle(),before);
            setReportValue(model.inAngle,reportModel.getLeftKingpinInAngle(),before);
        } else {
            setReportValue(model.camberAngle,reportModel.getRightCamberAngle(),before);
            setReportValue(model.casterAngle,reportModel.getRightKingpinCasterAngle(),before);
            setReportValue(model.inAngle,reportModel.getRightKingpinInAngle(),before);
        }
        setReportModel(key,reportModel);
    }
}
