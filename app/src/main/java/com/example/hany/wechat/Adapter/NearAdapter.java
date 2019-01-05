package com.example.hany.wechat.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hany.wechat.JavaBean.Near;
import com.example.hany.wechat.MsgActivity;
import com.example.hany.wechat.R;

import java.util.List;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/14 20:51
 * @filName NearAdapter
 * @describe ...
 */
public class NearAdapter extends RecyclerView.Adapter<NearAdapter.ViewHolder> {

    private List<Near> mNearList;     // 列表数据链表
    private String userId;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton nearContactImage;   // 显示头像的ImageButton控件
        TextView nearContactName;       // 显示名字的TextViwe控件
        TextView nearContactSummary;    // 显示消息摘要的TextView控件
        TextView nearContactTime;       // 显示消息接受/发送时间的TextView控件
        LinearLayout nearListLayout;

        public ViewHolder(View view) {
            super(view);
            nearContactImage = view.findViewById(R.id.ibtn_item_near);
            nearContactName = view.findViewById(R.id.txt_near_contact_name);
            nearContactSummary = view.findViewById(R.id.txt_near_contact_summary);
            nearContactTime = view.findViewById(R.id.txt_near_contact_time);
            nearListLayout = view.findViewById(R.id.layout_item_content);
        }
    }

    public NearAdapter(List<Near> list, String userId) {
        this.mNearList = list;
        this.userId = userId;
    }

    /**
     * 缓存列表数据
     * @param parent
     * @param viewType
     * @return
     */
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_near, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.nearContactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(), "你点击了" + mNearList.get(position).getName() + "的头像", Toast.LENGTH_SHORT).show();
            }
        });
        holder.nearListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Near near = mNearList.get(position);
                Intent intent= new Intent(parent.getContext(), MsgActivity.class);
                intent.putExtra("near", near);
                intent.putExtra("position", position);
                intent.putExtra("userId", userId);
                intent.putExtra("whereFrom", "NearFragment");
//                parent.getContext().startActivity(intent);
                ((Activity)parent.getContext()).startActivityForResult(intent, 1); // 在Adapter中调用startActivityForResult()方法
                Toast.makeText(parent.getContext(), "你点击了第" + position + "项", Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    /**
     * 当子项进入屏幕中时触动此函数
     * @param holder
     * @param position
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        Near near = mNearList.get(position);
        holder.nearContactImage.setImageResource(near.getImgId());
        holder.nearContactName.setText(near.getName());
        holder.nearContactSummary.setText(near.getSummary());
        holder.nearContactTime.setText(near.getTime());
    }

    /**
     * 获取列表子项总数
     * @return
     */
    @Override
    public int getItemCount() {
        return mNearList.size();
    }
}
