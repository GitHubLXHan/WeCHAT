package com.example.hany.wechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.hany.wechat.Adapter.NearContactAdapter;
import com.example.hany.wechat.JavaBean.NearContact;

import java.util.ArrayList;
import java.util.List;

public class NearListActivity extends AppCompatActivity {

    private TextView mAdmitTxt;
    private TextView mPasTxt;
    private List<NearContact> mNearContactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_list);
        // 初始化列表数据
        initListData(30);
        // 加载recyclerView列表
        RecyclerView recyclerView = findViewById(R.id.recycle_view_nearList);
        // 给recyclerView列表设置布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 创建一个适配器
        NearContactAdapter adapter = new NearContactAdapter(mNearContactList);
        // 设置适配器
        recyclerView.setAdapter(adapter);
//        mAdmitTxt = findViewById(R.id.txt_send_admit);
//        mPasTxt = findViewById(R.id.txt_send_pas);
//
//
//        Intent intent = getIntent();
//        String admit = intent.getStringExtra("admit");
//        String pas = intent.getStringExtra("pas");
//        admit = "你输入的账号为：" + admit;
//        pas = "你输入的密码为：" + pas;
//
//        mAdmitTxt.setText(admit);
//        mPasTxt.setText(pas);
    }


    /**
     * 初始化列表数据
     * @param number 数据子项数目
     */
    private void initListData(int number) {
        for (int i = 0; i < number; i++) {
            NearContact nearContact = new NearContact();
            nearContact.setImgId(R.drawable.china);
            nearContact.setName("刘晓瀚");
            nearContact.setTime("21:55");
            nearContact.setSummary("在干嘛");
            mNearContactList.add(nearContact);
        }
    }


}
