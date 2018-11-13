package com.example.hany.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hany.wechat.Util.StatusBarUtil;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout admitLayout;
    private LinearLayout pasLayout;
    private EditText admitEdt;
    private EditText pasEdt;
    private Button loginBtn;
    private Button usePhoneNumBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 设置状态栏文字颜色为深色
        setStatusTextDark(this);

        initView(); // 初始化所有子控件
        setAllListener(); // 设置子控件的监听事件

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
        admitLayout = findViewById(R.id.edt_admit);
        pasLayout = findViewById(R.id.edt_pas);
        // 获取两个布局之下的EditText控件
        admitEdt = (EditText) admitLayout.getChildAt(1);
        pasEdt = (EditText) pasLayout.getChildAt(1);
        //  登录按钮
        loginBtn = findViewById(R.id.login_login_btn);
        // 使用手机号码登录
        usePhoneNumBtn = findViewById(R.id.use_phone_number_btn);
    }

    /**
     * 设置所有监听事件
     */
    private void setAllListener() {
        // 设置登录按钮点击监听器
        loginBtn.setEnabled(false); // 将按钮设置为不可点击
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NearListActivity.class);
                intent.putExtra("admit", String.valueOf(admitEdt.getText()));
                intent.putExtra("pas", String.valueOf(pasEdt.getText()));
                startActivity(intent);
            }
        });

        // 设置使用手机号码按钮点击监听器
        usePhoneNumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "此功能暂未开通", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置账号输入框TextWatcher监听器，监听输入款输入状态，在这里用到afterTextChanged函数即可
        final boolean[] haveText = {false, false}; // 设置一个标记，当haveText为true，表示输入框里有内容
        admitEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                if (!TextUtils.isEmpty(editable) && haveText[1]) {
                    haveText[0] = true;
                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundColor(0xf51abc28);
                } else if (!TextUtils.isEmpty(editable) && !haveText[1]) {
                    haveText[0] = true;
                    loginBtn.setEnabled(false);
                    loginBtn.setBackgroundColor(0x701abc28);
                } else {
                    haveText[0] = false;
                    loginBtn.setEnabled(false);
                    loginBtn.setBackgroundColor(0x701abc28);
                }
            }
        });
        pasEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                if (!TextUtils.isEmpty(editable) && haveText[0]) {
                    haveText[1] = true;
                    loginBtn.setEnabled(true);
                    loginBtn.setBackgroundColor(0xf51abc28);
                } else if (!TextUtils.isEmpty(editable) && !haveText[0]) {
                    haveText[1] = true;
                    loginBtn.setEnabled(false);
                    loginBtn.setBackgroundColor(0x701abc28);
                } else {
                    haveText[1] = false;
                    loginBtn.setEnabled(false);
                    loginBtn.setBackgroundColor(0x701abc28);
                }
            }
        });

    }

    /**
     * 设置标题栏的按钮监听事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // 相当于点击手机的返回按钮
        }

        return super.onOptionsItemSelected(item);
    }


    //    private void addStatusViewWithColor(Activity activity, int color) {
//        ViewGroup contentView = activity.findViewById(android.R.id.content);
//        View statusBarView = new View(activity);
//        int height = getStatusBarHeight();
//        Log.d("statusHeight", String.valueOf(height));
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, height);
//        statusBarView.setBackgroundColor(color);
//        contentView.addView(statusBarView, layoutParams);
//    }
//
//    /**
//     * 利用反射获取状态栏高度
//     * @return
//     */
//    private int getStatusBarHeight() {
//        int result = 0;
//        // 获取状态栏高度资源id
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelOffset(resourceId);
//        }
//        Log.d("statusHeight", String.valueOf(result));
//        return result;
//    }


}
