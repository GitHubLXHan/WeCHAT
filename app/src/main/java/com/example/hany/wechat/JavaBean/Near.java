package com.example.hany.wechat.JavaBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/14 20:47
 * @filName Near
 * @describe ...
 */
public class Near implements Parcelable{

    private int imgId;
    private String name;
    private String summary;
    private String time;
    private String contractId;
    private String userId;
    private int id;

    public Near(int imgId, String name, String summary, String time, String contractId, String userId) {
        this.imgId = imgId;
        this.name = name;
        this.summary = summary;
        this.time = time;
        this.contractId = contractId;
        this.userId = userId;
    }

    public Near() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(imgId);
        parcel.writeString(name);
        parcel.writeString(summary);
        parcel.writeString(time);
        parcel.writeString(contractId);
        parcel.writeString(userId);
        parcel.writeInt(id);
    }

    public static final Parcelable.Creator<Near> CREATOR = new Parcelable.Creator<Near>(){

        @Override
        public Near createFromParcel(Parcel parcel) {
            Near near = new Near();
            near.imgId = parcel.readInt();
            near.name = parcel.readString();
            near.summary = parcel.readString();
            near.time = parcel.readString();
            near.contractId = parcel.readString();
            near.userId = parcel.readString();
            near.id = parcel.readInt();
            return near;
        }

        @Override
        public Near[] newArray(int i) {
            return new Near[i];
        }
    };

}
