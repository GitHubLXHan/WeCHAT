package com.example.hany.wechat.Fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hany.wechat.Adapter.ContractAdapter;
import com.example.hany.wechat.Adapter.NearAdapter;
import com.example.hany.wechat.JavaBean.Contract;
import com.example.hany.wechat.JavaBean.Near;
import com.example.hany.wechat.R;
import com.example.hany.wechat.Util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ContractFragment extends Fragment {

    private List<Contract> mContractList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContractAdapter adapter;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contract, container, false);
        // 获取此时登录的账号的ID
        userId = getArguments().getString("userId");
        // 初始化数据
        initListData();
        // 加载recyclerView
        recyclerView = view.findViewById(R.id.recycle_view_contract);
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
        adapter = new ContractAdapter(mContractList, userId);
        // 设置适配器
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initListData() {
        MyDatabaseHelper helper = new MyDatabaseHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from Contract where userId=?", new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                String imgName = cursor.getString(cursor.getColumnIndex("imgName"));
                String contractName = cursor.getString(cursor.getColumnIndex("contractName"));
                String time = cursor.getString(cursor.getColumnIndex("addTime"));
                String contractId = cursor.getString(cursor.getColumnIndex("contractId"));
                Contract contract = new Contract();
                contract.setImgId(this.getResources().getIdentifier(imgName, "drawable", "com.example.hany.wechat"));
                contract.setContractName(contractName);
                contract.setAddTime(time);
                contract.setContractId(contractId);
                mContractList.add(contract);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getActivity(), "没有好友", Toast.LENGTH_SHORT).show();
        }
    }



}
