package com.inprintech.wheelalignmenttest.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDeviceHelpter extends SQLiteOpenHelper {
    public static final String DB_NAME = "jmchendb";
    public static final String TB_NAME = "device";

    public SQLiteDeviceHelpter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + " ( _id integer primary key autoincrement," +//
                "device_id varchar," +
                "reada1 double," +
                "reada2 double," +
                "reada3 double," +
                "reada4 double," +
                "reada5 double," +
                "readb1 double," +
                "readb2 double," +
                "readb3 double," +
                "readb4 double," +
                "readb5 double" +
                ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}
