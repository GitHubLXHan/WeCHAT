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
            + "imgId integer, "
            + "name text, "
            + "time text, "
            + "summary text,"
            + "userId text, "
            + "contractId text)";

    private static final String CREATE_RECORD =
            "create table Record("
            + "id integer primary key autoincrement ,"
            + "userId text, "
            + "time text, "
            + "content text, "
            + "type integer, "
            + "contractId text)";

    private static final String CREATE_USER =
            "create table User("
            + "id integer primary key autoincrement, "
            + "userId text, "
            + "userName text, "
            + "password text, "
            + "createTime text, "
            + "imgName text)";

    private static final String CREATE_CONTRACT =
            "create table Contract("
            + "id integer primary key autoincrement, "
            + "userId text, "
            + "contractId text, "
            + "contractName text, "
            + "addTime text, "
            + "imgName text)";


    private Context mContext;

    public MyDatabaseHelper(Context context) {
        super(context, "WeChat.db", null, 1);
        this.mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEAR);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_RECORD);
        db.execSQL(CREATE_CONTRACT);
        Toast.makeText(mContext, "成功创建数据库", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table Near");
//        db.execSQL(CREATE_NEAR);
//        db.execSQL("drop table Record");
//        db.execSQL(CREATE_RECORD);
//        db.execSQL("drop table User");
//
//        db.execSQL(CREATE_USER);
        String TAG = "版本";
        Log.d(TAG, "onUpgrade: " + String.valueOf(oldVersion) + "  " + String.valueOf(newVersion));
    }



}
