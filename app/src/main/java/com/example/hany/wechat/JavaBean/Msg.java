package com.example.hany.wechat.JavaBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/16 9:23
 * @filName Msg
 * @describe ...
 */
public class Msg implements Parcelable{

    public static int TYPE_RECEIVED = 0;

    public static int TYPE_SEND = 1;

    private int id;

    private int imgId;

    private String content;

    private String time;

    private int userId;

    private int type;

    public Msg(String content,String time, int type, int userId, int imgId) {
//        this.id = id;
        this.content = content;
        this.time = time;
        this.type = type;
        this.userId = userId;
        this.imgId = imgId;
    }

    public Msg() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(imgId);
        parcel.writeString(content);
        parcel.writeString(time);
        parcel.writeInt(userId);
        parcel.writeInt(type);
    }

    public static final Parcelable.Creator<Msg> CREATOR = new Parcelable.Creator<Msg>() {

        @Override
        public Msg createFromParcel(Parcel parcel) {
            Msg msg = new Msg();
            msg.id = parcel.readInt();
            msg.imgId = parcel.readInt();
            msg.content = parcel.readString();
            msg.time = parcel.readString();
            msg.userId = parcel.readInt();
            msg.type = parcel.readInt();
            return msg;
        }

        @Override
        public Msg[] newArray(int i) {
            return new Msg[i];
        }
    };

}
