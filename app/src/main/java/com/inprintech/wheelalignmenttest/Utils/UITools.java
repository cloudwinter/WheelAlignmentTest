package com.inprintech.wheelalignmenttest.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.inprintech.wheelalignmenttest.Activity.MainActivity;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class UITools {

    /**
     * 显示Toast
     *
     * @param paramContext
     * @param stringId
     */
    public static void showToast(Context paramContext, int stringId) {
        Toast.makeText(paramContext, paramContext.getResources().getString(stringId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param v
     */
    public static void displaykeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return 不为空返回true
     */
    public static boolean isNotEmpty(String str) {
        boolean bool = false;
        if (str != null && !str.equals("")) {
            bool = true;
        }
        return bool;
    }

    /**
     * 输入框焦点状态
     *
     * @param editText
     * @param flag
     */
    public static void setEditFocusable(EditText editText[], boolean flag) {
        for (int i = 0; i < editText.length; i++) {
            if (flag) {
                editText[i].setFocusable(flag);
                editText[i].setFocusableInTouchMode(flag);
            } else {
                editText[i].setFocusable(false);
                editText[i].setFocusableInTouchMode(false);
            }
        }
    }

    /**
     * 判断字符串中包含几个-字符
     *
     * @param string
     * @return
     */
    public static int judgeStrAppearNum(String string) {
        int count = 0;
        String M = "-";
        while (string.indexOf(M) >= 0) {
            string = string.substring(string.indexOf(M) + M.length());
            count++;
        }
        return count;
    }

    /**
     * 中英文切换
     *
     * @param sta
     * @param context
     */
    public static void shiftLanguage(String sta, Context context,boolean refreshSelf) {
        if (sta.equals("en")) {
            Locale.setDefault(Locale.ENGLISH);
            Configuration config = context.getResources().getConfiguration();
            config.locale = Locale.ENGLISH;
            context.getResources().updateConfiguration
                    (config, context.getResources().getDisplayMetrics());

        } else {
            Locale.setDefault(Locale.CHINESE);
            Configuration config = context.getResources().getConfiguration();
            config.locale = Locale.CHINESE;
            context.getResources().updateConfiguration
                    (config, context.getResources().getDisplayMetrics());
        }
        if (refreshSelf) {
            refreshSelf(context);
        }
    }

    /**
     * 回到首页
     *
     * @param context
     */
    private static void refreshSelf(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 二进制转图片
     *
     * @param imaData
     * @return
     */
    public static Bitmap byteTobitmap(byte[] imaData) {
        Bitmap imagebitmap = BitmapFactory.decodeByteArray(imaData, 0, imaData.length);
        return imagebitmap;
    }

    /**
     * desc:将数组转为16进制
     *
     * @param bArray
     * @return String
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * desc:将16进制的数据转为数组
     *
     * @param data
     * @return byte[]
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); ////两位16进制数中的第一位(高位*16)
            int int_ch3;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch3 = (hex_char1 - 48) * 16;   //// 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch3 = (hex_char1 - 55) * 16; //// A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); ///两位16进制数中的第二位(低位)
            int int_ch4;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch4 = (hex_char2 - 48); //// 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch4 = hex_char2 - 55; //// A 的Ascll - 65
            else
                return null;
            int_ch = int_ch3 + int_ch4;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }

    public static byte[] bmpToByteArray(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 16进制字节转字符串
     *
     * @param b
     * @return
     */
    public static String byteToHex(byte b) {
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    //将data字节型数据转换为0~255 (0xFF 即BYTE)。
    public static int getUnsignedByte(byte data) {
        return data & 0x0FF;
    }

    public static int M(byte a, byte b) {
        int aa = getUnsignedByte(a);
        int bb = getUnsignedByte(b);
        if ((0x80 & b) == 0) {
            return bb * 256 + aa;
        } else {
            int i = getUnsignedByte(b) - 128;
            return -(i * 16 * 16 + aa);
        }
    }

    /**
     * 计算Ccd_data的值
     *
     * @return
     */
    public static double Ccd_data(int M) {
        double a = 1024.0, b = 14.0, c = 20000.0;
        double values = (M - a) * b / c;
        double arctanValue = Math.toDegrees(Math.atan(values));
        return arctanValue;
    }

    /**
     * 计算航向角
     *
     * @param RollH
     * @param RollL
     * @return
     */
    public static double Roll(byte RollL, byte RollH) {
        int m = M(RollL, RollH);
        double m32768 = (m / 32768.0);
        double roll = m32768 * 180.0;
        return roll;
    }

    /**
     * 计算俯仰角
     *
     * @param PitchH
     * @param PitchL
     * @return
     */
    public static double Pitch(byte PitchL, byte PitchH) {
        return M(PitchL, PitchH) / 32768.0 * 180.0;
    }

    /**
     * 计算横滚角
     *
     * @param YawH
     * @param YawL
     * @return
     */
    public static double Yaw(byte YawL, byte YawH) {
        return M(YawL, YawH) / 32768.0 * 180.0;
    }



    public static int Low(int l) {
        return (l / 256);
    }

    public static int High(int h) {
        return (h % 256);
    }

    /**
     * 后倾角
     *
     * @param w2
     * @param w1
     * @param p1
     * @return
     */
    public static double a(double w2, double w1, double p1) {
        double result = (w2 - w1) / (2 * Math.sin(p1));
        return result;
    }

    /**
     * 内倾角
     *
     * @param y2
     * @param y1
     * @param p1
     * @return
     */
    public static double b(double y2, double y1, double p1) {
        double result = 1.4619 * (y2 - y1) / (2 / Math.sin(p1));
        return result;
    }


    /**
     * 转向角
     * @param roll
     * @param yaw
     * @return
     */
    public static double I(double roll,double yaw){
        double Y = Math.cos(roll) * Math.cos(roll);
        double Z = Math.cos(yaw) * Math.cos(yaw);
        double X = Math.sqrt(Y+Z);
        double result = Math.acos(X);
        return result;
    }


    /**
     * 后倾角
     * @param Y1
     * @param Y2
     * @param I
     * @return
     */
    public static double A(double Y1,double Y2,double I){
        double tanI = Math.tan(I);
        double sin = Math.sin((Y1-Y2)/2);
        double X = sin / tanI;
        double result = Math.atan(X);
        return result;
    }


    /**
     * 内倾角
     * @param P1
     * @param P2
     * @param I
     * @return
     */
    public static double B(double P1,double P2,double I){
        double tanI = Math.tan(I);
        double sin = Math.sin((P1-P2)/2);
        double X = sin / tanI;
        double result = Math.atan(X);
        return result;
    }


    /**
     * 睡眠时间
     *
     * @param time
     */
    public static void sleeoTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
