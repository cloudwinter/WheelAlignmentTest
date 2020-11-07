package com.inprintech.wheelalignmenttest.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 承重桥数据库
 */
public class SQLiteBurdenWheelHelpter extends SQLiteOpenHelper {
    public static final String DB_NAME = "burdenwheeldb";
    public static final String TB_NAME1 = "burdenwheel1";
    public static final String TB_NAME2 = "burdenwheel2";
    public static final String TB_NAME3 = "burdenwheel3";
    public static final String TB_NAME4 = "burdenwheel4";

    private String tbName;
    public SQLiteBurdenWheelHelpter(Context context, String dbNname,String tbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbNname, factory, version);
        this.tbName = tbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                tbName + " ( _id integer primary key autoincrement," +
                "register_num varchar," +   // 登记号
                "measure_state varchar," +  //调整前、调整后
                "camber_angle1 double," +   //左外倾角
                "toe_in1 double," +         //左前束
                "total_toe_in double," +    //总前束
                "toe_in2 double," +         //右前束
                "camber_angle2 double," +   //右外倾角
                "axle_value double," +      //车桥值
                "sync_value double" +       //同步值
                ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tbName);
        onCreate(db);
    }
}
