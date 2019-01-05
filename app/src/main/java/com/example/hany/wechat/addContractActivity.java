package com.example.hany.wechat;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hany.wechat.Util.MyDatabaseHelper;

public class addContractActivity extends AppCompatActivity implements View.OnClickListener{

    private MyDatabaseHelper mHelper;
    private Button mCreateDatabase;
    private Button mInsertDataBtn;
    private Button mQueryDataBtn;
    private Button mDeleteAllDataBtn;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contract);
        initView();


    }

    /**
     * 初始化控件
     */
    private void initView() {
        mCreateDatabase = findViewById(R.id.btn_add_contract_create_database);
        mCreateDatabase.setOnClickListener(this);

        mInsertDataBtn = findViewById(R.id.btn_add_contract_insert_data);
        mInsertDataBtn.setOnClickListener(this);

        mQueryDataBtn = findViewById(R.id.btn_add_contract_query_data);
        mQueryDataBtn.setOnClickListener(this);

        mDeleteAllDataBtn = findViewById(R.id.btn_add_contract_delete_data);
        mDeleteAllDataBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_contract_create_database:
                openDatabase();
                break;
            case R.id.btn_add_contract_insert_data:
                addContract();
                break;
            case R.id.btn_add_contract_query_data:

                break;
            case R.id.btn_add_contract_delete_data:

                break;
        }
    }

    private void openDatabase() {
        mHelper = new MyDatabaseHelper(this);
        db = mHelper.getWritableDatabase();
        if (db != null) {
            Toast.makeText(this, "数据库打开成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void addContract() {
        if (db == null) {
            Toast.makeText(this, "请先打开数据库", Toast.LENGTH_SHORT).show();
        }
        ContentValues values = new ContentValues();
        values.put("userId", "16210120310");
        values.put("contractId", "16210120311");
        values.put("contractName", "刘培");
        values.put("addTime", "2019/01/01/星期二 08:00");
        values.put("imgName", "head_sculpture_1");
        db.insert("Contract", null, values);
        values.clear();

        values.put("userId", "16210120310");
        values.put("contractId", "16210120312");
        values.put("contractName", "黄疸水");
        values.put("addTime", "2019/01/01/星期二 10:00");
        values.put("imgName", "head_sculpture_3");
        db.insert("Contract", null, values);
        values.clear();

        values.put("userId", "16210120310");
        values.put("contractId", "16210120313");
        values.put("contractName", "李玉山");
        values.put("addTime", "2019/01/01/星期二 12:00");
        values.put("imgName", "head_sculpture_4");
        db.insert("Contract", null, values);
        values.clear();

        values.put("userId", "16210120310");
        values.put("contractId", "16210120314");
        values.put("contractName", "腾讯新闻");
        values.put("addTime", "2019/01/01/星期二 14:00");
        values.put("imgName", "head_sculpture_5");
        db.insert("Contract", null, values);
        values.clear();

        values.put("userId", "16210120310");
        values.put("contractId", "16210120315");
        values.put("contractName", "颇为而");
        values.put("addTime", "2019/01/01/星期二 16:00");
        values.put("imgName", "china");
        db.insert("Contract", null, values);
        values.clear();

        values.put("userId", "16210120310");
        values.put("contractId", "16210120316");
        values.put("contractName", "钱我恩");
        values.put("addTime", "2019/01/01/星期二 18:00");
        values.put("imgName", "argentina");
        db.insert("Contract", null, values);
        values.clear();

        values.put("userId", "16210120310");
        values.put("contractId", "16210120317");
        values.put("contractName", "煤矿村");
        values.put("addTime", "2019/01/01/星期二 20:00");
        values.put("imgName", "pakistan");
        db.insert("Contract", null, values);
        values.clear();

        values.put("userId", "16210120310");
        values.put("contractId", "16210120318");
        values.put("contractName", "破玩意");
        values.put("addTime", "2019/01/01/星期二 22:00");
        values.put("imgName", "unitedstate");
        db.insert("Contract", null, values);
        values.clear();
    }


}
