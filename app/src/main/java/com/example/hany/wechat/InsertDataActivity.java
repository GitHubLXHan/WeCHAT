package com.example.hany.wechat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hany.wechat.Util.MyDatabaseHelper;

public class InsertDataActivity extends BaseActivity implements View.OnClickListener{

    private MyDatabaseHelper mHelper;
    private Button mCreateDatabase;
    private Button mInsertDataBtn;
    private Button mQueryDataBtn;
    private Button mDeleteAllDataBtn;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        initView(); // 初始化控件
        mHelper = new MyDatabaseHelper(this); // 初始化数据库帮助类
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_database:
                openDatabase();
                break;
            case R.id.btn_insert_data:
                insertData();
                break;
            case R.id.btn_query_data:
                queryData();
                break;
            case R.id.btn_delete_data:
                deleteAllData();
                break;
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mCreateDatabase = findViewById(R.id.btn_create_database);
        mCreateDatabase.setOnClickListener(this);

        mInsertDataBtn = findViewById(R.id.btn_insert_data);
        mInsertDataBtn.setOnClickListener(this);

        mQueryDataBtn = findViewById(R.id.btn_query_data);
        mQueryDataBtn.setOnClickListener(this);

        mDeleteAllDataBtn = findViewById(R.id.btn_delete_data);
        mDeleteAllDataBtn.setOnClickListener(this);
        // 通过图片名字获取图片id
//        int imgId = this.getResources().getIdentifier("china", "drawable", "com.example.hany.wechat");
//        mImg.setImageResource(imgId);
    }


    /**
     * 打开数据库
     */
    private void openDatabase() {
        db = mHelper.getWritableDatabase(); // 创建或打开数据库
        if (db != null) {
            Toast.makeText(this, "打开数据库", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 插入数据
     */
    private void insertData() {
        if (db == null) {
            Toast.makeText(this, "请先打开数据库", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put("imgName", "head_sculpture_1");
        values.put("name", "美女");
        values.put("time", "22:55");
        values.put("summary", "Hello 美女！");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "head_sculpture_2");
        values.put("name", "上午10:21");
        values.put("time", "25:56");
        values.put("summary", "吃啊未");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "head_sculpture_3");
        values.put("name", "耳机");
        values.put("time", "昨天");
        values.put("summary", "你在听什么歌？");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "head_sculpture_4");
        values.put("name", "手机");
        values.put("time", "星期二");
        values.put("summary", "手机？？？？？？？？");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "head_sculpture_5");
        values.put("name", "腾讯新闻");
        values.put("time", "星期一");
        values.put("summary", "呵呵呵呵呵呵呵呵呵");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "china");
        values.put("name", "中国");
        values.put("time", "星期一");
        values.put("summary", "中国牛逼！！！！");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "unitedstate");
        values.put("name", "美国");
        values.put("time", "星期一");
        values.put("summary", "美国也挺牛逼的。。。");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "pakistan");
        values.put("name", "巴基斯坦");
        values.put("time", "星期日");
        values.put("summary", "这个我就不知道了。");
        db.insert("Near", null, values);
        values.clear();
        values.put("imgName", "argentina");
        values.put("name", "哪个国家来着？？");
        values.put("time", "星期一");
        values.put("summary", "不认识。。。");
        db.insert("Near", null, values);
        values.clear();
        Toast.makeText(this, "插入成功", Toast.LENGTH_SHORT).show();
    }

    private void queryData() {
        if (db == null) {
            Toast.makeText(this, "请先打开数据库", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = db.rawQuery("select * from Near",new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String TAG = "DATABASECONTENT";
                Log.d(TAG, "imgName is " + imgName);
                Log.d(TAG, "name is " + name);
                Log.d(TAG, "name is " + time);
                Log.d(TAG, "name is " + summary);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAllData(){
        if (db == null) {
            Toast.makeText(this, "请先打开数据库", Toast.LENGTH_SHORT).show();
            return;
        }
        db.execSQL("delete from Near");
        Toast.makeText(this, "删除数据成功", Toast.LENGTH_SHORT).show();
    }

}

