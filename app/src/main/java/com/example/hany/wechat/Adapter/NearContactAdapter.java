package com.example.hany.wechat.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hany.wechat.JavaBean.NearContact;
import com.example.hany.wechat.R;

import java.util.List;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/14 20:51
 * @filName NearContactAdapter
 * @describe ...
 */
public class NearContactAdapter extends RecyclerView.Adapter<NearContactAdapter.ViewHolder> {

    private List<NearContact> mNearContactList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton nearContactImage;
        TextView nearContactName;
        TextView nearContactSummary;
        TextView nearContactTime;
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

    public NearContactAdapter(List<NearContact> list) {
        this.mNearContactList = list;
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
                Toast.makeText(view.getContext(), "你点击了头像", Toast.LENGTH_SHORT).show();
            }
        });
        holder.nearListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
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
        NearContact nearContact = mNearContactList.get(position);
        holder.nearContactImage.setImageResource(nearContact.getImgId());
        holder.nearContactName.setText(nearContact.getName());
        holder.nearContactSummary.setText(nearContact.getSummary());
        holder.nearContactTime.setText(nearContact.getTime());
    }

    /**
     * 获取列表子项总数
     * @return
     */
    @Override
    public int getItemCount() {
        return mNearContactList.size();
    }
}
