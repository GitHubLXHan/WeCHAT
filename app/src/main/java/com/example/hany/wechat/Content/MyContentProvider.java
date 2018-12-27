package com.example.hany.wechat.Content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.hany.wechat.Util.MyDatabaseHelper;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/12/11 21:21
 * @filName MyContentProvider
 * @describe 自定义内容提供器。
 */
public class MyContentProvider extends ContentProvider {

    private MyDatabaseHelper helper;
    private SQLiteDatabase db;
    private static final int NEAR_DIR = 0;
//    private static final int NEAR_ITEM = 1;
    private static final String AUTHORITY = "com.example.hany.wechat.Content.MyContentProvider";
    private static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "Near", NEAR_DIR);
//        uriMatcher.addURI(AUTHORITY, "Near/#", NEAR_ITEM); // #表示可以匹配id号
    }

    public boolean onCreate() {
        helper = new MyDatabaseHelper(getContext(), "WeChat.db",null, 3 );
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = helper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case 0:
                cursor = db.query("Near", projection, selection, selectionArgs, null, null, sortOrder);
            break;
        }
        return cursor;
    }

    public Uri insert(Uri uri, ContentValues values) {
        db = helper.getWritableDatabase();
        Uri uriReturn = null;
        long newNearId;
        switch (uriMatcher.match(uri)){
            case 0:
                newNearId = db.insert("Near", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/Near/" + newNearId);
                break;
        }
        return uriReturn;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = helper.getWritableDatabase();
        int updateRow = 0;
        switch (uriMatcher.match(uri)) {
            case 0:
                updateRow = db.update("Near", values, selection, selectionArgs);
                break;
        }
        return updateRow;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = helper.getWritableDatabase();
        int deleteRow = 0;
        switch (uriMatcher.match(uri)) {
            case 0:
                deleteRow = db.delete("Near", selection, selectionArgs);
                break;
        }
        return deleteRow;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 0:
                return "vnd.android.dir/van.com.example.hany.wechat.Content.MyContentProvider.Near";
            case 1:
                return "vnd.android.item/van.com.example.hany.wechat.Content.MyContentProvider.Near";
        }
        return null;
    }


}
