package com.example.hany.wechat.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/27 16:43
 * @filName MyDatabaseHelper
 * @describe ...
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_NEAR =
            "create table Near("
            + "id integer primary key autoincrement, "
            + "imgName text, "
            + "name text, "
            + "time text, "
            + "summary text)";

    private static final String CREATE_RECORD =
            "create table Record("
            + "id integer primary key autoincrement ,"
            + "userId integer, "
            + "time text, "
            + "content text, "
            + "type)";

//    private static final String CREATE_USER =
//            "create table User("
//            + "id integer primary key autoincrement, "
//            + "";


    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEAR);
        Toast.makeText(mContext, "成功创建数据库", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table Near");
//        db.execSQL(CREATE_NEAR);
//        db.execSQL("drop table Record");
        db.execSQL(CREATE_RECORD);
        String TAG = "版本";
        Log.d(TAG, "onUpgrade: " + String.valueOf(oldVersion) + "  " + String.valueOf(newVersion));
    }



}
