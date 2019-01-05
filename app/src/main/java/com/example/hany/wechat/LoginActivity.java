package com.example.hany.wechat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.hany.wechat.Collector.ActivityCollector;
import com.example.hany.wechat.Util.MyDatabaseHelper;
import com.example.hany.wechat.Util.StatusBarUtil;

public class LoginActivity extends BaseActivity {

    private LinearLayout loginAdmitLayout;
    private LinearLayout loginPasLayout;
    private EditText loginAdmitEdt;
    private EditText loginPasEdt;
    private Button loginBtn;
    private Button toRegisterBtn;
    private CheckBox mRememberCb;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;
    final boolean[] haveText = {false, false}; // 设置一个标记，当haveText为true，表示输入框里有内容



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

        // 设置原先记住的账号密码
        setAdmitPas();

        // 获取DatabaseHelper对象并且打开数据库
        helper = new MyDatabaseHelper(this);
        db = helper.getWritableDatabase();

    }

    /**
     * 设置原先记住的账号密码
     */
    private void setAdmitPas() {
        mRememberCb = findViewById(R.id.cb_remember);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isRemember = preferences.getBoolean("remember_password", false);
        // 当设置了记住密码时
        if (isRemember) {
            // 将账号和密码都设置到文本中
            String admit = preferences.getString("admit", "");
            String password = preferences.getString("password","");
            loginAdmitEdt.setText(admit);
            loginPasEdt.setText(password);
            mRememberCb.setChecked(true);
        }
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
        loginAdmitLayout = findViewById(R.id.edt_admit);
        loginPasLayout = findViewById(R.id.edt_pas);
        // 获取两个布局之下的EditText控件
        loginAdmitEdt = (EditText) loginAdmitLayout.getChildAt(1);
        loginPasEdt = (EditText) loginPasLayout.getChildAt(1);
        loginPasEdt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        //  登录按钮
        loginBtn = findViewById(R.id.login_login_btn);
        // 去注册按钮
        toRegisterBtn = findViewById(R.id.btn_to_register);
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
                // 先判断密码是否正确
                Cursor cursor = db.rawQuery("select * from User where userId = ?", new String[]{loginAdmitEdt.getText().toString()});
                cursor.moveToFirst();
                if (loginPasEdt.getText().toString().equals(cursor.getString(cursor.getColumnIndex("password")))) {
                    // 密码正确时
                    // 若勾上记住密码选项框则保存账号密码，否则清除SharedPreference文件的数据
                    editor = preferences.edit();
                    if (mRememberCb.isChecked()) {
                        editor.putBoolean("remember_password", true);
                        editor.putString("admit", loginAdmitEdt.getText().toString());
                        editor.putString("password", loginPasEdt.getText().toString());
                    } else {
                        editor.clear();
                    }
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("open", "near");
                    intent.putExtra("userId", loginAdmitEdt.getText().toString());
                    startActivity(intent);
                } else {
                    dialog("密码错误");
                }
            }
        });

        // 去登陆按钮监听事件
        // 打开注册界面
        toRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 设置账号输入框TextWatcher监听器，监听输入款输入状态，在这里用到afterTextChanged函数即可
        loginAdmitEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                testLoginAdmitEdt(editable);

            }
        });
        loginPasEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable editable) {
                // 设置按钮是否可以点击
                testLoginPasEdt(editable);
            }
        });

        // 焦点监听事件
        loginAdmitEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 获取焦点时

                } else {
                    // 失去焦点时
                    if (!isExistUser()) {
                        // 当账号不存在时，弹出警示框并置空输入框
                        dialog("该账号不存在");
                        loginAdmitEdt.setText("");
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
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

    /**
     * 判断账号输入框是都有正确的账号而设定登录按钮是否可以点击。
     * 规则是用haveText布尔数组记录两个框是否有值，当有值则为true，
     * 否则为false然后当haveText数组中两个布尔值都为true时，
     * 则设置按钮可点击并且颜色比原颜色深。
     * @param editable
     */
    private void testLoginAdmitEdt(Editable editable) {
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

    /**
     * 判断账号输入框是都有正确的账号而设定登录按钮是否可以点击。
     * 规则是用haveText布尔数组记录两个框是否有值，当有值则为true，
     * 否则为false然后当haveText数组中两个布尔值都为true时，
     * 则设置按钮可点击并且颜色比原颜色深。
     * @param editable
     */
    private void testLoginPasEdt(Editable editable) {
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

    /**
     * 判断预登录的账号是否存在
     * @return
     */
    private boolean isExistUser() {
        Cursor cursor = db.rawQuery("select * from User where userId = ?", new String[]{loginAdmitEdt.getText().toString()});
        int count = cursor.getCount();
        cursor.close();
        return count > 0 ? true : false;
    }

    /**
     * 弹出警示框
     * @param msg
     */
    private void dialog(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("警告");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("重新输入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
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
