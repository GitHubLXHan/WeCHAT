package com.example.hany.wechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NearListActivity extends AppCompatActivity {

    private TextView mAdmitTxt;
    private TextView mPasTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_list);

        mAdmitTxt = findViewById(R.id.txt_send_admit);
        mPasTxt = findViewById(R.id.txt_send_pas);


        Intent intent = getIntent();
        String admit = intent.getStringExtra("admit");
        String pas = intent.getStringExtra("pas");
        admit = "你输入的账号为：" + admit;
        pas = "你输入的密码为：" + pas;

        mAdmitTxt.setText(admit);
        mPasTxt.setText(pas);
    }
}
