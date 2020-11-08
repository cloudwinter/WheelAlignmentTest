package com.inprintech.wheelalignmenttest.Utils;

public class Constants {

    public static final String IP = "192.168.4.1";
    public static final String PORT = "1001";

    /* 测量*/
    public static final byte[] measure() {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x00;
        heartbeatOrder[4] = (byte) 0x00;
        heartbeatOrder[5] = (byte) 0x00;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xA0;
        heartbeatOrder[8] = (byte) 0x01;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = (byte) 0x00;
        heartbeatOrder[15] = (byte) 0x00;
        heartbeatOrder[16] = (byte) 0x00;
        heartbeatOrder[17] = (byte) 0x00;
        heartbeatOrder[18] = (byte) 0x00;
        heartbeatOrder[19] = (byte) 0x00;
        heartbeatOrder[20] = (byte) 0x00;
        heartbeatOrder[21] = (byte) 0x00;
        heartbeatOrder[22] = (byte) 0x00;
        heartbeatOrder[23] = (byte) 0x00;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

    public static final byte[] measure2() {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x00;
        heartbeatOrder[4] = (byte) 0x00;
        heartbeatOrder[5] = (byte) 0x00;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xA0;
        heartbeatOrder[8] = (byte) 0x02;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = (byte) 0x00;
        heartbeatOrder[15] = (byte) 0x00;
        heartbeatOrder[16] = (byte) 0x00;
        heartbeatOrder[17] = (byte) 0x00;
        heartbeatOrder[18] = (byte) 0x00;
        heartbeatOrder[19] = (byte) 0x00;
        heartbeatOrder[20] = (byte) 0x00;
        heartbeatOrder[21] = (byte) 0x00;
        heartbeatOrder[22] = (byte) 0x00;
        heartbeatOrder[23] = (byte) 0x00;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

    /* 读取*/
    public static final byte[] reading_standard() {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x00;
        heartbeatOrder[4] = (byte) 0x00;
        heartbeatOrder[5] = (byte) 0x00;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xB0;
        heartbeatOrder[8] = (byte) 0x01;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = (byte) 0x00;
        heartbeatOrder[15] = (byte) 0x00;
        heartbeatOrder[16] = (byte) 0x00;
        heartbeatOrder[17] = (byte) 0x00;
        heartbeatOrder[18] = (byte) 0x00;
        heartbeatOrder[19] = (byte) 0x00;
        heartbeatOrder[20] = (byte) 0x00;
        heartbeatOrder[21] = (byte) 0x00;
        heartbeatOrder[22] = (byte) 0x00;
        heartbeatOrder[23] = (byte) 0x00;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

    public static final byte[] reading_standard2() {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x00;
        heartbeatOrder[4] = (byte) 0x00;
        heartbeatOrder[5] = (byte) 0x00;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xB0;
        heartbeatOrder[8] = (byte) 0x02;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = (byte) 0x00;
        heartbeatOrder[15] = (byte) 0x00;
        heartbeatOrder[16] = (byte) 0x00;
        heartbeatOrder[17] = (byte) 0x00;
        heartbeatOrder[18] = (byte) 0x00;
        heartbeatOrder[19] = (byte) 0x00;
        heartbeatOrder[20] = (byte) 0x00;
        heartbeatOrder[21] = (byte) 0x00;
        heartbeatOrder[22] = (byte) 0x00;
        heartbeatOrder[23] = (byte) 0x00;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

    /* 上传*/
    public static final byte[] upload(byte b14, byte b15, byte b16, byte b17, byte b18, byte b19, byte b20, byte b21, byte b22, byte b23) {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x01;
        heartbeatOrder[4] = (byte) 0x02;
        heartbeatOrder[5] = (byte) 0x03;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xD0;
        heartbeatOrder[8] = (byte) 0x01;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = b14;
        heartbeatOrder[15] = b15;
        heartbeatOrder[16] = b16;
        heartbeatOrder[17] = b17;
        heartbeatOrder[18] = b18;
        heartbeatOrder[19] = b19;
        heartbeatOrder[20] = b20;
        heartbeatOrder[21] = b21;
        heartbeatOrder[22] = b22;
        heartbeatOrder[23] = b23;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

    public static final byte[] upload2(byte b14, byte b15, byte b16, byte b17, byte b18, byte b19, byte b20, byte b21, byte b22, byte b23) {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x01;
        heartbeatOrder[4] = (byte) 0x02;
        heartbeatOrder[5] = (byte) 0x03;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xD0;
        heartbeatOrder[8] = (byte) 0x02;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = b14;
        heartbeatOrder[15] = b15;
        heartbeatOrder[16] = b16;
        heartbeatOrder[17] = b17;
        heartbeatOrder[18] = b18;
        heartbeatOrder[19] = b19;
        heartbeatOrder[20] = b20;
        heartbeatOrder[21] = b21;
        heartbeatOrder[22] = b22;
        heartbeatOrder[23] = b23;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

    /* 标定*/
    public static final byte[] calibration(byte selectT) {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x01;
        heartbeatOrder[4] = (byte) 0x02;
        heartbeatOrder[5] = selectT;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xC0;
        heartbeatOrder[8] = (byte) 0x01;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = (byte) 0x10;
        heartbeatOrder[15] = (byte) 0x10;
        heartbeatOrder[16] = (byte) 0x11;
        heartbeatOrder[17] = (byte) 0x11;
        heartbeatOrder[18] = (byte) 0x12;
        heartbeatOrder[19] = (byte) 0x12;
        heartbeatOrder[20] = (byte) 0x13;
        heartbeatOrder[21] = (byte) 0x13;
        heartbeatOrder[22] = (byte) 0x14;
        heartbeatOrder[23] = (byte) 0x14;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

    public static final byte[] calibration2(byte selectT) {
        byte[] heartbeatOrder = new byte[26];
        heartbeatOrder[0] = (byte) 0x55;
        heartbeatOrder[1] = (byte) 0xAA;
        heartbeatOrder[2] = (byte) 0x00;
        heartbeatOrder[3] = (byte) 0x01;
        heartbeatOrder[4] = (byte) 0x02;
        heartbeatOrder[5] = selectT;
        heartbeatOrder[6] = (byte) 0x1A;
        heartbeatOrder[7] = (byte) 0xC0;
        heartbeatOrder[8] = (byte) 0x02;
        heartbeatOrder[9] = (byte) 0x00;
        heartbeatOrder[10] = (byte) 0x01;
        heartbeatOrder[11] = (byte) 0x02;
        heartbeatOrder[12] = (byte) 0x03;
        heartbeatOrder[13] = (byte) 0x04;
        heartbeatOrder[14] = (byte) 0x10;
        heartbeatOrder[15] = (byte) 0x10;
        heartbeatOrder[16] = (byte) 0x11;
        heartbeatOrder[17] = (byte) 0x11;
        heartbeatOrder[18] = (byte) 0x12;
        heartbeatOrder[19] = (byte) 0x12;
        heartbeatOrder[20] = (byte) 0x13;
        heartbeatOrder[21] = (byte) 0x13;
        heartbeatOrder[22] = (byte) 0x14;
        heartbeatOrder[23] = (byte) 0x14;
        heartbeatOrder[24] = (byte) 0x0D;
        heartbeatOrder[25] = (byte) 0x0A;
        return heartbeatOrder;
    }

}
