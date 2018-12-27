package com.example.hany.wechat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.hany.wechat.Collector.ActivityCollector;
import com.example.hany.wechat.Util.StatusBarUtil;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout regAdmitLayout;
    private LinearLayout regPasLayout;
    private LinearLayout regOncePasLayout;
    private EditText regAdmitEdt;
    private EditText regPasEdt;
    private EditText regOncePasEdt;
    private Button regBtn;
    private Button toLoginBtn;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 设置状态栏文字颜色为深色
        setStatusTextDark(this);
        // 去除标题栏阴影，Android 5.0以上需要在java文件中设置
        if(Build.VERSION.SDK_INT>=21){
            getSupportActionBar().setElevation(0);
        }
        // 取消标题文字
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        // 设置标题栏左边按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_cancel);

        // 初始化所有控件
        initView();

        // 设置所有监听事件
        setAllListener();

    }

    /**
     * 设置状态栏文字颜色为深色
     * @param activity
     */
    private void setStatusTextDark(Activity activity) {
        String systemType = StatusBarUtil.tryLightStatus(activity);
        if (!TextUtils.isEmpty(systemType) && systemType != null) {
            switch (systemType) {
                case "MIUI":
                    StatusBarUtil.MIUISetStatusBarLightMode(this, true);
                    break;
                case "Flyme":
                    StatusBarUtil.FlymeSetStatusBarLightMode(this, true);
                    break;
                case "AndroidM" :
                    StatusBarUtil.AndroidMSetStatusBarLightMode(this, true);
                    break;
            }
        }
    }

    /**
     * 初始化所有子控件
     */
    private void initView() {
        // 因为两个输入框是自定义LinearLayout布局，所以加载两个输入框的布局
        regAdmitLayout = findViewById(R.id.register_edt_admit);
        regPasLayout = findViewById(R.id.register_edt_pas);
        regOncePasLayout = findViewById(R.id.register_edt_once_pas);
        // 获取两个布局之下的EditText控件
        regAdmitEdt = (EditText) regAdmitLayout.getChildAt(1);
        regPasEdt = (EditText) regPasLayout.getChildAt(1);
        regOncePasEdt = (EditText) regOncePasLayout.getChildAt(1);
        // 设置密码输入框为密码类型及内容不可见
        regPasEdt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        regOncePasEdt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        //  登录按钮
        regBtn = findViewById(R.id.register_register_btn);
        // 去登录
        toLoginBtn = findViewById(R.id.register_to_login_btn);
    }

    /**
     * 设置所有监听事件
     */
    private void setAllListener() {
        // 设置登录按钮点击监听器
//        loginBtn.setEnabled(false); // 将按钮设置为不可点击
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 若勾上记住密码选项框则保存账号密码，否则清除SharedPreference文件的数据
//                editor = preferences.edit();
//                if (mRememberCb.isChecked()) {
//                    editor.putBoolean("remember_password", true);
//                    editor.putString("admit", loginAdmitEdt.getText().toString());
//                    editor.putString("password", loginPasEdt.getText().toString());
//                } else {
//                    editor.clear();
//                }
//                editor.apply();

//                Intent intent = new Intent(LoginActivity.this, NearListActivity.class);
//                startActivity(intent);
//            }
//        });

        // 设置账号输入框TextWatcher监听器，监听输入款输入状态，在这里用到afterTextChanged函数即可
        final boolean[] haveText = {false, false}; // 设置一个标记，当haveText为true，表示输入框里有内容
        regAdmitEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                if (!TextUtils.isEmpty(editable) && haveText[1]) {
                    haveText[0] = true;
                    regBtn.setEnabled(true);
                    regBtn.setBackgroundColor(0xf51abc28);
                } else if (!TextUtils.isEmpty(editable) && !haveText[1]) {
                    haveText[0] = true;
                    regBtn.setEnabled(false);
                    regBtn.setBackgroundColor(0x701abc28);
                } else {
                    haveText[0] = false;
                    regBtn.setEnabled(false);
                    regBtn.setBackgroundColor(0x701abc28);
                }
            }
        });
        regPasEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                if (!TextUtils.isEmpty(editable) && haveText[0]) {
                    haveText[1] = true;
                    regBtn.setEnabled(true);
                    regBtn.setBackgroundColor(0xf51abc28);
                } else if (!TextUtils.isEmpty(editable) && !haveText[0]) {
                    haveText[1] = true;
                    regBtn.setEnabled(false);
                    regBtn.setBackgroundColor(0x701abc28);
                } else {
                    haveText[1] = false;
                    regBtn.setEnabled(false);
                    regBtn.setBackgroundColor(0x701abc28);
                }
            }
        });
        regOncePasEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                if (!TextUtils.isEmpty(editable) && haveText[0]) {
                    haveText[1] = true;
                    regBtn.setEnabled(true);
                    regBtn.setBackgroundColor(0xf51abc28);
                } else if (!TextUtils.isEmpty(editable) && !haveText[0]) {
                    haveText[1] = true;
                    regBtn.setEnabled(false);
                    regBtn.setBackgroundColor(0x701abc28);
                } else {
                    haveText[1] = false;
                    regBtn.setEnabled(false);
                    regBtn.setBackgroundColor(0x701abc28);
                }
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        ActivityCollector.finishAll();
//    }

    /**
     * 设置标题栏的按钮监听事件
     * @param item
     * @return
     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed(); // 相当于点击手机的返回按钮
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
