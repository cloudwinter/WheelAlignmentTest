package com.inprintech.wheelalignmenttest.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelpter extends SQLiteOpenHelper {
    public static final String DB_NAME = "jmchendb";
    public static final String TB_NAME = "jmchen";

    public SQLiteHelpter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + " ( _id integer primary key autoincrement," +//
                "register_num varchar," +
                "date varchar," +
                "customer_info varchar," +
                "manufacturer varchar," +
                "vehicl_teype varchar," +
                "mileage integer," +
                "autograph BLOB" +
                ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}
