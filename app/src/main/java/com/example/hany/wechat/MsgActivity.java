package com.example.hany.wechat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hany.wechat.Adapter.MsgAdapter;
import com.example.hany.wechat.Collector.ActivityCollector;
import com.example.hany.wechat.JavaBean.Msg;
import com.example.hany.wechat.JavaBean.NearContact;
import com.example.hany.wechat.Util.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MsgActivity extends BaseActivity implements View.OnClickListener{

    private List<Msg> mMsgList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EditText mInputEdt;
    private Button mSendBtn;
    private MsgAdapter mAdapter;
    private String withWhoName;
    private int withWhoId;
    private int withWhoImg;
    private int position;
    private boolean isRecordUpgrade = false;
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;

    private Button mReceiveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        initData();
        setWithWhoTitle();

        mRecyclerView = findViewById(R.id.recycle_view_msg);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MsgAdapter(mMsgList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.scrollToPosition(mMsgList.size() - 1);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if (helper == null) {
                    Toast.makeText(this, "helper为空", Toast.LENGTH_SHORT).show();
                }
                if (db == null) {
                    Toast.makeText(this, "数据库未打开", Toast.LENGTH_SHORT).show();
                    return;
                }

                String sendContent = mInputEdt.getText().toString();
                if (!TextUtils.isEmpty(sendContent)) {
                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("yyyy/MM/dd/EEEE HH:mm");
                    String sendTime = simpleDateFormat.format(new Date());
                    ContentValues values = new ContentValues();
                    values.put("userId", withWhoId);
                    values.put("time", sendTime);
                    values.put("content", sendContent);
                    values.put("type", Msg.TYPE_SEND);
                    db.insert("Record", null, values);
                    values.clear();
//                    db.execSQL("insert into Record (userId, time, content, type) values (?, ?, ?, ?)", new String[] {});

                    /** 将发送的数据添加道消息列表数组中 */
                    Msg msg = new Msg(sendContent, sendTime, 1, withWhoId, withWhoImg);
                    mMsgList.add(msg);
                    // 通知recyclerView有新消息添加到数组中
                    mAdapter.notifyItemInserted(mMsgList.size() - 1);
                    // 将recyclerView定位到最后一行
                    mRecyclerView.scrollToPosition(mMsgList.size() - 1);
                    // 清空输入框
                    mInputEdt.setText("");

                    isRecordUpgrade = true; // 标记说明聊天记录有更新
                }
                break;

            case R.id.btn_receive:
                if (helper == null) {
                    Toast.makeText(this, "helper为空", Toast.LENGTH_SHORT).show();
                }
                if (db == null) {
                    Toast.makeText(this, "数据库未打开", Toast.LENGTH_SHORT).show();
                    return;
                }

                String receiveContent = mInputEdt.getText().toString();
                if (!TextUtils.isEmpty(receiveContent)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/EEEE HH:mm");
                    String receiveTime = simpleDateFormat.format(new Date());
                    ContentValues values = new ContentValues();
                    values.put("userId", withWhoId);
                    values.put("time", receiveTime);
                    values.put("content", receiveContent);
                    values.put("type", Msg.TYPE_RECEIVED);
                    db.insert("Record", null, values);
                    values.clear();

                    /** 将接收的数据添加道消息列表数组中 */
                    Msg msg = new Msg(receiveContent, receiveTime, 0, withWhoId, withWhoImg);
                    mMsgList.add(msg);
                    // 通知recyclerView有新消息添加到数组中
                    mAdapter.notifyItemInserted(mMsgList.size() - 1);
                    // 将recyclerView定位到最后一行
                    mRecyclerView.scrollToPosition(mMsgList.size() - 1);
                    // 清空输入框
                    mInputEdt.setText("");
                    isRecordUpgrade = true; // 标记说明聊天记录有更新
                }
                break;
        }
    }

    /**
     * 初始化所有数据
     */
    private void initData() {
        // 获取Helper对象并且打开数据库
        helper = new MyDatabaseHelper(this, "WeChat.db", null, 2);
        db = helper.getWritableDatabase();

        // 获取从NearActivity传递过来的数据，包括聊天对象的名称、id
        NearContact nearContact = getIntent().getParcelableExtra("NearContract");
        withWhoName = nearContact.getName();
        withWhoId = nearContact.getId();
        withWhoImg = nearContact.getImgId();
        position = getIntent().getIntExtra("position", -1);

        Cursor cursor = db.rawQuery("select * from Record where userId = ?",
                new String[]{String.valueOf(withWhoId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String contend = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int userId = cursor.getInt(cursor.getColumnIndex("userId"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                Msg msg = new Msg(contend, time, type, userId, withWhoImg);
                mMsgList.add(msg);

            }while (cursor.moveToNext());
        }
//        Msg msg1 = new Msg("Hello", 0);
//        mMsgList.add(msg1);
//        Msg msg2 = new Msg("Hello", 1);
//        mMsgList.add(msg2);
//        Msg msg3 = new Msg("Hello", 0);
//        mMsgList.add(msg3);





        // 加载控件
        mInputEdt = findViewById(R.id.txt_edt_input);
        mSendBtn = findViewById(R.id.btn_send);
        mSendBtn.setOnClickListener(this);
        mReceiveBtn = findViewById(R.id.btn_receive);
        mReceiveBtn.setOnClickListener(this);


        String TAG = "initData";
        Log.d(TAG, "initData: " + withWhoName);
        Log.d(TAG, "initData: " + String.valueOf(withWhoId));
    }

    /**
     * 设置正在和谁聊天的标题
     */
    private void setWithWhoTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(withWhoName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
            case android.R.id.home:
//                ActivityCollector.removeActivity(this);
//                Intent intent1 = new Intent(MsgActivity.this, NearListActivity.class);
//                intent1.putExtra("withWhoId", withWhoId);
//                setResult(RESULT_OK, intent1);
                if (isRecordUpgrade) {
                    // 当聊天记录有更新时，返回该聊天对象原本在NearList列表中的位置
                    // 以及聊天内容列表中最后一个Msg对象（即最后一条聊天记录）
                    // 和聊天对象的名字
                    Intent intent1 = new Intent();
                    intent1.putExtra("Msg", mMsgList.get(mMsgList.size()-1));
                    intent1.putExtra("position", position);
                    intent1.putExtra("name", withWhoName);
                    setResult(RESULT_OK, intent1);
                    ActivityCollector.removeActivity(this);
                    Toast.makeText(this, "数据", Toast.LENGTH_SHORT).show();
                } else {
                    // 当聊天记录没有更新时，不回传数据
                    Intent intents = new Intent(MsgActivity.this, NearListActivity.class);
                    startActivity(intents);
                }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isRecordUpgrade) {
            // 当聊天记录有更新时，返回该聊天对象原本在NearList列表中的位置
            // 以及聊天内容列表中最后一个Msg对象（即最后一条聊天记录）
            // 和聊天对象的名字
            Intent intent1 = new Intent();
            intent1.putExtra("Msg", mMsgList.get(mMsgList.size()-1));
            intent1.putExtra("position", position);
            intent1.putExtra("name", withWhoName);
            setResult(RESULT_OK, intent1);
            ActivityCollector.removeActivity(this);
        } else {
            // 当聊天记录没有更新时，不回传数据
            Intent intents = new Intent(MsgActivity.this, NearListActivity.class);
            startActivity(intents);
        }
        super.onBackPressed();
    }
}
