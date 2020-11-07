package com.inprintech.wheelalignmenttest.Model;

import java.io.Serializable;

/**
 * 注销测量实体
 * Created by xiayundong on 2020/4/6.
 */
public class KingpinModel implements Serializable {

    /**
     * 设备1（左侧），设备2（右侧）
     * （1，2）
     */
    public int channel;

    /**
     * 后倾角
     */
    public String casterAngle;
    /**
     * 内倾角
     */
    public String inAngle;

    /**
     * 最大转向角
     */
    public String maxSteeringAngle;

    /**
     * 外倾角
     */
    public String camberAngle;

    public KingpinModel() {
    }

    @Override
    public String toString() {
        return "KingpinModel{" +
                "channel='" + channel + '\'' +
                ", casterAngle='" + casterAngle + '\'' +
                ", inAngle='" + inAngle + '\'' +
                ", maxSteeringAngle='" + maxSteeringAngle + '\'' +
                ", camberAngle='" + camberAngle + '\'' +
                '}';
    }
}
