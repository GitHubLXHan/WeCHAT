package com.example.hany.wechat.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hany.wechat.JavaBean.Msg;
import com.example.hany.wechat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/12/19 16:59
 * @filName MsgAdapter
 * @describe ...
 */
public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {

    private List<Msg> mMsgList;

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout letfLayout;
        private LinearLayout rightLayout;
        private ImageView leftImg;
        private ImageView rightImg;
        private TextView leftMsg;
        private TextView rightMsg;

        public ViewHolder(View view) {
            super(view);
            letfLayout = view.findViewById(R.id.layout_left);
            rightLayout = view.findViewById(R.id.layout_right);
            leftImg = view.findViewById(R.id.img_left);
            rightImg = view.findViewById(R.id.img_right);
            leftMsg = view.findViewById(R.id.txt_left_msg);
            rightMsg = view.findViewById(R.id.txt_right_msg);
        }
    }


    public MsgAdapter(List<Msg> mMsgList) {
        this.mMsgList = mMsgList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg = mMsgList.get(position);
        if (msg.getType() ==Msg.TYPE_RECEIVED) {
            holder.rightLayout.setVisibility(View.GONE);
            holder.letfLayout.setVisibility(View.VISIBLE);
            holder.leftMsg.setText(msg.getContent());
            holder.leftImg.setImageResource(msg.getImgId());
        } else if (msg.getType() == Msg.TYPE_SEND) {
            holder.letfLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

}
