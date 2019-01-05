package com.example.hany.wechat.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hany.wechat.Collector.ActivityCollector;
import com.example.hany.wechat.MainActivity;
import com.example.hany.wechat.R;
import com.example.hany.wechat.Util.MyDatabaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener{

    private LogoutOfflineReceiver receiver;
    private ImageView profileImg;
    private EditText userNameEdt;
    private Button logoutBtn;
    private TextView userIdTxt;
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set, container, false);
        // 获取Helper对象及打开数据库
        helper = new MyDatabaseHelper(getContext());
        db = helper.getWritableDatabase();
        // 获取此时登录的账号的ID
        userId = getArguments().getString("userId");
        // 初始化控件
        view = initView(view);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("警告");
                builder.setMessage("确定注销此账号吗？");
                builder.setCancelable(false);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent("com.example.hany.wechat.LOGOUT_OFFLINE");
                        getContext().sendBroadcast(intent);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.edt_set_name:
                if (b) {
                    // 当获得焦点时
                } else {
                    // 当失去焦点时
                    if (!TextUtils.isEmpty(userNameEdt.getText().toString())) {
                        // 当名字输入框不为空时，更新账号名字
                        String newUserName = userNameEdt.getText().toString();
                        db.execSQL("update User set userName = ? where userId = ?", new String[]{newUserName, userId});
                    } else {
                        // 当名字输入框为空时
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("警告");
                        builder.setMessage("名字不能为空！");
                        builder.setCancelable(false);
                        builder.setPositiveButton("返回", null);
                        builder.show();
                    }
                }
                break;
        }
    }


    /**
     * 注册注销登录的广播
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.example.hany.wechat.LOGOUT_OFFLINE");
        receiver = new LogoutOfflineReceiver();
        getContext().registerReceiver(receiver, filter);
    }

    /**
     * 取消注册注销登录的广播
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(receiver);
    }

    /**
     * 加载控件并注册监听事件
     * @param view
     * @return
     */
    private View initView(View view) {
        profileImg = view.findViewById(R.id.img_set_profile);
        userNameEdt = view.findViewById(R.id.edt_set_name);
        logoutBtn= view.findViewById(R.id.btn_set_logout);
        userIdTxt = view.findViewById(R.id.txt_set_userId);
        Cursor cursor = db.rawQuery("select * from User where userId = ?", new String[]{userId});
        cursor.moveToFirst();
        String userName = cursor.getString(cursor.getColumnIndex("userName"));
        String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
        profileImg.setImageResource(getActivity().getResources().getIdentifier(imgName, "drawable", "com.example.hany.wechat"));
        userNameEdt.setText(userName);
        userIdTxt.setText(userId);
        profileImg.setOnClickListener(this);
        userNameEdt.setOnFocusChangeListener(this);
        logoutBtn.setOnClickListener(this);


        return view;
    }


    class LogoutOfflineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            ActivityCollector.finishAll();
            Intent intent1 = new Intent(context, MainActivity.class);
            context.startActivity(intent1);
        }
    }



}
