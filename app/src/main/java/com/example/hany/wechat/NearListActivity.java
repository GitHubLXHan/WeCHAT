package com.example.hany.wechat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hany.wechat.Adapter.NearContactAdapter;
import com.example.hany.wechat.JavaBean.Msg;
import com.example.hany.wechat.JavaBean.NearContact;
import com.example.hany.wechat.Util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NearListActivity extends BaseActivity {

    private List<NearContact> mNearContactList = new ArrayList<>();
    RecyclerView recyclerView;
    NearContactAdapter adapter;

    private String TAG = "NearListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_list);
        // 初始化列表数据
        initListData();
        // 加载recyclerView列表
        recyclerView = findViewById(R.id.recycle_view_nearList);
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
        adapter = new NearContactAdapter(mNearContactList);
        // 设置适配器
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    /**
     * 添加标题栏右侧菜单栏
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.force_offline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 标题栏右侧菜单栏监听事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_force_offline:
                Toast.makeText(this, "发送强制下线广播", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("com.example.hany.wechat.FORCE_OFFLINE");
                sendBroadcast(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化列表数据
     */
    private void initListData() {
        MyDatabaseHelper helper = new MyDatabaseHelper(this, "WeChat.db", null, 2);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Near", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                NearContact nearContact = new NearContact();
                nearContact.setId(id);
                nearContact.setImgId(this.getResources().getIdentifier(imgName, "drawable", "com.example.hany.wechat"));
                nearContact.setName(name);
                nearContact.setTime(time);
                nearContact.setSummary(summary);
                mNearContactList.add(nearContact);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    int position = data.getIntExtra("position", -1);
                    mNearContactList.remove(position);
                    adapter.notifyItemRemoved(position);
//                    adapter.notifyDataSetChanged();
                    Msg msg = data.getParcelableExtra("Msg");
                    String[] time = msg.getTime().split("/");
                    NearContact nearContact =
                            new NearContact(msg.getImgId(), name, msg.getContent(), time[3], msg.getId());
                    mNearContactList.add(0, nearContact);
                    // 通知recyclerView有新消息添加到数组中
                    adapter.notifyItemInserted(0);
                    // 将recyclerView定位到第一行
                    recyclerView.scrollToPosition(0);
                }
        }
    }
}
