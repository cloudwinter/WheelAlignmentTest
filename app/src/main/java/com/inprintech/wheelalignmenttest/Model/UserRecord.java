package com.inprintech.wheelalignmenttest.Model;

import java.io.Serializable;

public class UserRecord implements Serializable {

    private String time;
    private String registerNum;
    private byte[] autograph;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRegisterNum() {
        return registerNum;
    }

    public void setRegisterNum(String registerNum) {
        this.registerNum = registerNum;
    }

    public byte[] getAutograph() {
        return autograph;
    }

    public void setAutograph(byte[] autograph) {
        this.autograph = autograph;
    }
}
