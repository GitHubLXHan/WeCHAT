package com.example.hany.wechat.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hany.wechat.Adapter.NearAdapter;
import com.example.hany.wechat.JavaBean.Msg;
import com.example.hany.wechat.JavaBean.Near;
import com.example.hany.wechat.R;
import com.example.hany.wechat.Util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;


public class NearFragment extends Fragment {

    String TAG = "NearFragmentTAG";

    private List<Near> mNearList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NearAdapter adapter;
    private String userId;
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near, container, false);
        // 获取此时登录的账号的ID
        userId = getArguments().getString("userId");
        // 初始化列表数据
        initListData();
        // 加载recyclerView列表
        recyclerView = view.findViewById(R.id.recycle_view_nearList);
        // 给recyclerView列表设置布局方式
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        // 添加分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        //第一种方式：添加Android默认分割线
//        recyclerView.addItemDecoration(decoration);
        //第二种方式：添加自定义分割线
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.divider_line));
        recyclerView.addItemDecoration(decoration);
        // 创建一个适配器
        adapter = new NearAdapter(mNearList, userId);
        // 设置适配器
        recyclerView.setAdapter(adapter);


        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode:" + String.valueOf(requestCode));
        Log.d(TAG, "resultCode:" + String.valueOf(requestCode));
        switch (requestCode) {
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    Msg msg = data.getParcelableExtra("Msg");
                    String name = data.getStringExtra("name");
                    String whereFrom = data.getStringExtra("whereFrom");
                    int position = data.getIntExtra("position", 0);
                    String contractId = msg.getContractId();
                    String summary = msg.getContent();
                    String[] time = msg.getTime().split("/");
                    int imgId = msg.getImgId();
                    if (whereFrom.equals("ContractFragment")) {
                        // 如果是从ContractFragment中传递过来的，
                        // 则判断是否已经存在NearFragment数据数组中（即原先是否已经有聊天过），
                        // 若有则获取其位置，否则默认位置为数据数组的第一位（即第零为）
                        for (Near near : mNearList) {
                            if (near.getContractId().equals(contractId)) {
                                // 找到对象位置后并且删除对象
                                position = mNearList.indexOf(near);
                                mNearList.remove(position);
                                adapter.notifyItemRemoved(position);
                            }
                        }
                    } else {
                        // 这是从NearFragment中传递过来的
                        mNearList.remove(position);
                        adapter.notifyItemRemoved(position);
                    }

                    Near near =
                            new Near(imgId, name, summary, time[3], contractId, userId);
                    mNearList.add(0, near);
                    // 通知recyclerView有新消息添加到数组中
//                    adapter.notifyItemInserted(0);
//                    adapter.notifyItemChanged(0);

                    adapter.notifyDataSetChanged();

                    // 将recyclerView定位到第一行
                    recyclerView.scrollToPosition(0);
                    // 先将数据库里原本该聊天人删除后再添加此聊天人到数据库中，
                    // 这样做是为了让该聊天人早每次初始化数据时处于顶端
                    db.execSQL("delete from Near where userId = ? and contractId = ?", new String[]{userId, contractId});
                    ContentValues values = new ContentValues();
                    values.put("imgId",imgId);
                    values.put("name", name);
                    values.put("time", time[3]);
                    values.put("summary", summary);
                    values.put("userId", userId);
                    values.put("contractId", contractId);
                    db.insert("Near", null, values);
                    values.clear();
                }
        }
    }

    /**
     * 初始化列表数据
     */
    private void initListData() {
        helper = new MyDatabaseHelper(getActivity());
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Near where userId=?", new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                int imgId = cursor.getInt(cursor.getColumnIndex("imgId"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String contractId = cursor.getString(cursor.getColumnIndex("contractId"));
                Near near = new Near();
                near.setImgId(imgId);
                near.setName(name);
                near.setTime(time);
                near.setSummary(summary);
                near.setContractId(contractId);
                mNearList.add(near);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getActivity(), "没有最近连联系人", Toast.LENGTH_SHORT).show();
        }
    }


}
