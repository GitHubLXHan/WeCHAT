package com.example.hany.wechat;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
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
import android.widget.Toast;

import com.example.hany.wechat.Adapter.MsgAdapter;
import com.example.hany.wechat.Collector.ActivityCollector;
import com.example.hany.wechat.JavaBean.Msg;
import com.example.hany.wechat.JavaBean.Near;
import com.example.hany.wechat.Service.MyService;
import com.example.hany.wechat.Util.MyDatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MsgActivity extends BaseActivity implements View.OnClickListener{

    String TAG = "MsgActivityTAG";
    private List<Msg> mMsgList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EditText mInputEdt;
    private Button mSendBtn;
    private MsgAdapter mAdapter;
    private String contractName;
    private String userId;
    private String contractId;
    private int contractImg;
    private int position;
    private String whereFrom;
    private boolean isRecordUpgrade = false;
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;

    MyService.InfoBinder binder;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binder = (MyService.InfoBinder) iBinder;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(TAG, "handleMessage: 接收到");
                    receiveInfo(msg.obj.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        // 开启和绑定服务
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("messenger", new Messenger(mHandler));
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE); // 绑定
        // 初始化数据
        initData();
        // 标题内容设置为聊天对象的名字
        setWithWhoTitle();
        // 加载RecyclerView和设置适配器
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
                binder.sendInfo(sendContent);
                if (!TextUtils.isEmpty(sendContent)) {
                    SimpleDateFormat simpleDateFormat =
                            new SimpleDateFormat("yyyy/MM/dd/EEEE HH:mm");
                    String sendTime = simpleDateFormat.format(new Date());
                    ContentValues values = new ContentValues();
                    values.put("userId", userId);
                    values.put("time", sendTime);
                    values.put("content", sendContent);
                    values.put("type", Msg.TYPE_SEND);
                    values.put("contractId", contractId);
                    db.insert("Record", null, values);
                    values.clear();
//                    db.execSQL("insert into Record (userId, time, content, type) values (?, ?, ?, ?)", new String[] {});

                    /** 将发送的数据添加道消息列表数组中 */
                    Msg msg = new Msg(sendContent, sendTime, 1, userId, contractId, contractImg);
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
     * 当接收到信息时触发此函数
     * @param receiveContent
     */
    private void receiveInfo(String receiveContent) {
        if (helper == null) {
            Toast.makeText(this, "helper为空", Toast.LENGTH_SHORT).show();
        }
        if (db == null) {
            Toast.makeText(this, "数据库未打开", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(receiveContent)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/EEEE HH:mm");
            String receiveTime = simpleDateFormat.format(new Date());
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("time", receiveTime);
            values.put("content", receiveContent);
            values.put("type", Msg.TYPE_RECEIVED);
            values.put("contractId", contractId);
            db.insert("Record", null, values);
            values.clear();

            /** 将接收的数据添加道消息列表数组中 */
            Msg msg = new Msg(receiveContent, receiveTime, 0, userId, contractId, contractImg);
            mMsgList.add(msg);
            // 通知recyclerView有新消息添加到数组中
            mAdapter.notifyItemInserted(mMsgList.size() - 1);
            // 将recyclerView定位到最后一行
            mRecyclerView.scrollToPosition(mMsgList.size() - 1);
            // 清空输入框
            mInputEdt.setText("");
            isRecordUpgrade = true; // 标记说明聊天记录有更新
        }
    }

    /**
     * 初始化所有数据
     */
    private void initData() {
        // 获取Helper对象并且打开数据库
        helper = new MyDatabaseHelper(this);
        db = helper.getWritableDatabase();

        // 先记录是从哪个Activity跳转过来的
        whereFrom = getIntent().getStringExtra("whereFrom");

        // 获取从NearActivity传递过来的数据，包括聊天对象的名称、id
        Near near = getIntent().getParcelableExtra("near");
        contractName = near.getName();
        contractId = near.getContractId();
        contractImg = near.getImgId();
        userId = getIntent().getStringExtra("userId");
        position = getIntent().getIntExtra("position", 0);

        Cursor cursor = db.rawQuery("select * from Record where userId = ? and contractId = ?",
                new String[]{userId, contractId});
        if (cursor.moveToFirst()) {
            do {
                String contend = cursor.getString(cursor.getColumnIndex("content"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));

                Msg msg = new Msg(contend, time, type, userId, contractId, contractImg);
                mMsgList.add(msg);

            }while (cursor.moveToNext());
        }

        // 加载控件
        mInputEdt = findViewById(R.id.txt_edt_input);
        mSendBtn = findViewById(R.id.btn_send);
        mSendBtn.setOnClickListener(this);
    }

    /**
     * 设置正在和谁聊天的标题
     */
    private void setWithWhoTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(contractName);
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
            case R.id.item_receive_info:
                binder.receiveInfo();// 开启接收信息的子线程
                break;
            case android.R.id.home:
                onBackPressed();
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
            if (mMsgList.size() > 0) {
                intent1.putExtra("Msg", mMsgList.get(mMsgList.size()-1));
            }
            intent1.putExtra("open", "near");
            intent1.putExtra("position", position);
            intent1.putExtra("name", contractName);
            intent1.putExtra("whereFrom", whereFrom);
            setResult(RESULT_OK, intent1);
            ActivityCollector.removeActivity(this);
        } else if (!isRecordUpgrade && whereFrom.equals("ContractFragment")){
            // 当聊天记录没有更新时并且是从contractFragment打开的，不回传数据
            Intent intent2 = new Intent(MsgActivity.this, HomeActivity.class);
            intent2.putExtra("open", "contract");
            intent2.putExtra("userId", userId);
            startActivity(intent2);
        } else if (!isRecordUpgrade && whereFrom.equals("ContractFragment")) {
            // 当聊天记录没有更新时，不回传数据
            Intent intent3 = new Intent(MsgActivity.this, HomeActivity.class);
            intent3.putExtra("open", "near");
            intent3.putExtra("userId", userId);
            startActivity(intent3);
        }

        super.onBackPressed();
    }


}
