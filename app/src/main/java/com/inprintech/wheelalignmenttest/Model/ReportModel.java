package com.inprintech.wheelalignmenttest.Model;

import java.io.Serializable;

/**
 * Created by xiayundong on 2020/4/8.
 */
public class ReportModel implements Serializable {

    // #左侧设备
    /**
     * 外倾角
     */
    public ReportValue leftCamberAngle;
    /**
     * 前束角
     */
    public ReportValue leftToeinAngle;
    /**
     * 主销后倾角
     */
    public ReportValue leftKingpinCasterAngle;
    /**
     * 主销内倾角
     */
    public ReportValue leftKingpinInAngle;

    // #右侧设备
    /**
     * 外倾角
     */
    public ReportValue rightCamberAngle;
    /**
     * 前束角
     */
    public ReportValue rightToeinAngle;
    /**
     * 主销后倾角
     */
    public ReportValue rightKingpinCasterAngle;
    /**
     * 主销后倾角
     */
    public ReportValue rightKingpinInAngle;

    // #汇总数据
    /**
     * 总前束
     */
    public ReportValue totalToein;

    /**
     * 同步值
     */
    public ReportValue syncValue;

    /**
     * 车桥测量
     */
    public ReportValue axleValue;


    public ReportValue getLeftCamberAngle() {
        return leftCamberAngle;
    }

    public void setLeftCamberAngle(ReportValue leftCamberAngle) {
        this.leftCamberAngle = leftCamberAngle;
    }

    public ReportValue getLeftToeinAngle() {
        return leftToeinAngle;
    }

    public void setLeftToeinAngle(ReportValue leftToeinAngle) {
        this.leftToeinAngle = leftToeinAngle;
    }

    public ReportValue getLeftKingpinCasterAngle() {
        return leftKingpinCasterAngle;
    }

    public void setLeftKingpinCasterAngle(ReportValue leftKingpinCasterAngle) {
        this.leftKingpinCasterAngle = leftKingpinCasterAngle;
    }

    public ReportValue getLeftKingpinInAngle() {
        return leftKingpinInAngle;
    }

    public void setLeftKingpinInAngle(ReportValue leftKingpinInAngle) {
        this.leftKingpinInAngle = leftKingpinInAngle;
    }

    public ReportValue getRightCamberAngle() {
        return rightCamberAngle;
    }

    public void setRightCamberAngle(ReportValue rightCamberAngle) {
        this.rightCamberAngle = rightCamberAngle;
    }

    public ReportValue getRightToeinAngle() {
        return rightToeinAngle;
    }

    public void setRightToeinAngle(ReportValue rightToeinAngle) {
        this.rightToeinAngle = rightToeinAngle;
    }

    public ReportValue getRightKingpinCasterAngle() {
        return rightKingpinCasterAngle;
    }

    public void setRightKingpinCasterAngle(ReportValue rightKingpinCasterAngle) {
        this.rightKingpinCasterAngle = rightKingpinCasterAngle;
    }

    public ReportValue getRightKingpinInAngle() {
        return rightKingpinInAngle;
    }

    public void setRightKingpinInAngle(ReportValue rightKingpinInAngle) {
        this.rightKingpinInAngle = rightKingpinInAngle;
    }

    public ReportValue getTotalToein() {
        return totalToein;
    }

    public void setTotalToein(ReportValue totalToein) {
        this.totalToein = totalToein;
    }

    public ReportValue getSyncValue() {
        return syncValue;
    }

    public void setSyncValue(ReportValue syncValue) {
        this.syncValue = syncValue;
    }

    public ReportValue getAxleValue() {
        return axleValue;
    }

    public void setAxleValue(ReportValue axleValue) {
        this.axleValue = axleValue;
    }
}
