package com.inprintech.wheelalignmenttest.Model;

import java.io.Serializable;

/**
 * Created by xiayundong on 2020/4/10.
 */
public class ReportValue implements Serializable {


    /**
     * 调整前数据
     */
    public String adjustBefore;

    /**
     * 调整后数据
     */
    public String adjustAfter;

    public String getAdjustBefore() {
        return adjustBefore;
    }

    public void setAdjustBefore(String adjustBefore) {
        this.adjustBefore = adjustBefore;
    }

    public String getAdjustAfter() {
        return adjustAfter;
    }

    public void setAdjustAfter(String adjustAfter) {
        this.adjustAfter = adjustAfter;
    }
}
