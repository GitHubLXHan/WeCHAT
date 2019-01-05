package com.example.hany.wechat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hany.wechat.Collector.ActivityCollector;
import com.example.hany.wechat.Util.MyDatabaseHelper;
import com.example.hany.wechat.Util.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    String TAG = "RegisterActivitytTAG";

    private LinearLayout regAdmitLayout;
    private LinearLayout regPasLayout;
    private LinearLayout regOncePasLayout;
    private EditText regAdmitEdt;
    private EditText regPasEdt;
    private EditText regOncePasEdt;
    private Button regBtn;
    private Button toLoginBtn;
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;
    final boolean[] haveText = {false, false, false}; // 设置一个标记，当haveText为true，表示输入框里有内容



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

        // 获取DatabaseHelper对象并且打开数据库
        helper = new MyDatabaseHelper(this);
        db = helper.getWritableDatabase();

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
        regBtn.setEnabled(false); // 将按钮设置为不可点击
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (regPasEdt.getText().toString().equals(regOncePasEdt.getText().toString())) {
                    // 先判断该账号是否存在
                    if (!isExistUser()) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/EEEE HH:mm");
                        String createTime = simpleDateFormat.format(new Date());
                        ContentValues values = new ContentValues();
                        values.put("userId", regAdmitEdt.getText().toString());
                        values.put("userName", regAdmitEdt.getText().toString());
                        values.put("password", regOncePasEdt.getText().toString());
                        values.put("imgName", "head_sculpture_2");
                        values.put("createTime", createTime);
                        long row = db.insert("User", null, values);

                        // 当数据插入成功时，insert()函数返回所插入数据在数据表中所在的行数，
                        // 若插入失败则返回-1
                        if (row != -1) {
                            Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                            intent.putExtra("open", "reg");
                            intent.putExtra("userId", regAdmitEdt.getText().toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, "不知错误", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("警告");
                        builder.setMessage("该账号已经存在");
                        builder.setCancelable(false);
                        builder.setPositiveButton("重新输入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder.show();
                    }
                }

            }
        });

        toLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        // 设置账号输入框TextWatcher监听器，监听输入款输入状态，在这里用到afterTextChanged函数即可
        regAdmitEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                testRegAdmitEdt(editable);
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
                testRegPasEdt(editable);
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
                testRegOncePasEdt(editable);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

    /**
     * 设置标题栏的按钮监听事件
//     * @param item
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

    /**
     * 判断预注册的账号是否存在
     * @return
     */
    private boolean isExistUser() {
        Cursor cursor = db.rawQuery("select * from User where userId = ?", new String[]{regAdmitEdt.getText().toString()});
        int count = cursor.getCount();
        cursor.close();
        return count > 0 ? true : false;
    }

    private void testRegAdmitEdt(Editable editable) {
        if (!TextUtils.isEmpty(editable) && haveText[1] && haveText[2]) {
            haveText[0] = true;
            regBtn.setEnabled(true);
            regBtn.setBackgroundColor(0xf51abc28);
        } else if (!TextUtils.isEmpty(editable) && (!haveText[1] || !haveText[2])) {
            haveText[0] = true;
            regBtn.setEnabled(false);
            regBtn.setBackgroundColor(0x701abc28);
        } else {
            haveText[0] = false;
            regBtn.setEnabled(false);
            regBtn.setBackgroundColor(0x701abc28);
        }
    }

    private void testRegPasEdt(Editable editable) {
        if (!TextUtils.isEmpty(editable) && haveText[0] && haveText[2]) {
            haveText[1] = true;
            regBtn.setEnabled(true);
            regBtn.setBackgroundColor(0xf51abc28);
        } else if (!TextUtils.isEmpty(editable) && (!haveText[0] || !haveText[2])) {
            haveText[1] = true;
            regBtn.setEnabled(false);
            regBtn.setBackgroundColor(0x701abc28);
        } else {
            haveText[1] = false;
            regBtn.setEnabled(false);
            regBtn.setBackgroundColor(0x701abc28);
        }
    }

    private void testRegOncePasEdt(Editable editable) {
        if (!TextUtils.isEmpty(editable) && haveText[0] && haveText[1]) {
            haveText[2] = true;
            regBtn.setEnabled(true);
            regBtn.setBackgroundColor(0xf51abc28);
        } else if (!TextUtils.isEmpty(editable) && (!haveText[0] || !haveText[1])) {
            haveText[2] = true;
            regBtn.setEnabled(false);
            regBtn.setBackgroundColor(0x701abc28);
        } else {
            haveText[2] = false;
            regBtn.setEnabled(false);
            regBtn.setBackgroundColor(0x701abc28);
        }
    }


}
