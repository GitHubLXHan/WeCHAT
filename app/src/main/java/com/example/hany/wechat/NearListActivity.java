package com.example.hany.wechat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hany.wechat.Adapter.NearContactAdapter;
import com.example.hany.wechat.JavaBean.NearContact;
import com.example.hany.wechat.Util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NearListActivity extends AppCompatActivity {

    private List<NearContact> mNearContactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_list);
        // 初始化列表数据
        initListData(1);
        // 加载recyclerView列表
        RecyclerView recyclerView = findViewById(R.id.recycle_view_nearList);
        // 给recyclerView列表设置布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 添加分割线
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            //第一种方式：添加Android默认分割线
//        recyclerView.addItemDecoration(decoration);
            //第二种方式：添加自定义分割线
        decoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider_line));
        recyclerView.addItemDecoration(decoration);
        // 创建一个适配器
        NearContactAdapter adapter = new NearContactAdapter(mNearContactList);
        // 设置适配器
        recyclerView.setAdapter(adapter);
    }


    /**
     * 初始化列表数据
     * @param number 数据子项数目
     */
    private void initListData(int number) {
        MyDatabaseHelper helper = new MyDatabaseHelper(this, "WeChat.db", null, 3);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Near", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                for (int i = 0; i < number; i++) {
                    NearContact nearContact = new NearContact();
                    nearContact.setImgId(this.getResources().getIdentifier(imgName, "drawable", "com.example.hany.wechat"));
                    nearContact.setName(name);
                    nearContact.setTime(time);
                    nearContact.setSummary(summary);
                    mNearContactList.add(nearContact);
                }
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "查询失败", Toast.LENGTH_SHORT).show();
        }
//        for (int i = 0; i < number; i++) {
//            NearContact nearContact = new NearContact();
//            nearContact.setImgId(R.drawable.china);
//            nearContact.setName("刘晓瀚");
//            nearContact.setTime("21:55");
//            nearContact.setSummary("在干嘛");
//            mNearContactList.add(nearContact);
//        }
    }


}
