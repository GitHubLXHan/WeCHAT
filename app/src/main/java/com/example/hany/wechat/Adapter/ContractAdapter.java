package com.example.hany.wechat.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hany.wechat.JavaBean.Contract;
import com.example.hany.wechat.JavaBean.Near;
import com.example.hany.wechat.MsgActivity;
import com.example.hany.wechat.R;

import java.util.List;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2019/1/1 16:20
 * @filName ContractAdapter
 * @describe ...
 */
public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder>{

    private List<Contract> mContractList;
    private String userId;

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout contractLayout;
        private ImageView contractProfileImg;
        private TextView contractNameTxt;

        public ViewHolder(View view) {
            super(view);
            contractLayout = view.findViewById(R.id.layout_item_contract);
            contractProfileImg = view.findViewById(R.id.img_item_contract_profile);
            contractNameTxt = view.findViewById(R.id.txt_item_user_name);
        }
    }

    public ContractAdapter(List<Contract> contractList, String userId) {
        this.mContractList = contractList;
        this.userId = userId;
    }

    /**
     * 缓存列表数据
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.contractLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Contract contract = mContractList.get(position);
                Intent intent = new Intent(parent.getContext(), MsgActivity.class);
                Near near = new Near(contract.getImgId(), contract.getContractName(), "", contract.getAddTime(), contract.getContractId(), userId);
                intent.putExtra("userId", userId);
                intent.putExtra("near", near);
                intent.putExtra("whereFrom", "ContractFragment");
                ((Activity)parent.getContext()).startActivityForResult(intent, 1); // 在Adapter中调用startActivityForResult()方法
            }
        });
        return holder;
    }

    /**
     * 当子项进入屏幕中时触动此函数
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contract contract = mContractList.get(position);
        holder.contractProfileImg.setImageResource(contract.getImgId());
        holder.contractNameTxt.setText(contract.getContractName());
    }

    /**
     * 获取列表子项总数
     * @return
     */
    @Override
    public int getItemCount() {
        return mContractList.size();
    }
}
