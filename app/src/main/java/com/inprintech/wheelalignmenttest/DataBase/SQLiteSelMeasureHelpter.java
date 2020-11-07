package com.inprintech.wheelalignmenttest.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteSelMeasureHelpter extends SQLiteOpenHelper {
    public static final String DB_NAME = "selmeasuredb";
    public static final String TB_NAME = "selmeasure";

    public SQLiteSelMeasureHelpter(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + " ( _id integer primary key autoincrement," +//
                "register_num varchar," +
                "selects_1 varchar," +
                "selects_2 varchar," +
                "selects_3 varchar," +
                "selects_4 varchar" +
                ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
        onCreate(db);
    }
}
