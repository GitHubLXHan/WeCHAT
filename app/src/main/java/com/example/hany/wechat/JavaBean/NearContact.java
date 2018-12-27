package com.example.hany.wechat.JavaBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/14 20:47
 * @filName NearContact
 * @describe ...
 */
public class NearContact implements Parcelable{

    private int imgId;
    private String name;
    private String summary;
    private String time;
    private int id;

    public NearContact(int imgId, String name, String summary, String time, int id) {
        this.imgId = imgId;
        this.name = name;
        this.summary = summary;
        this.time = time;
        this.id = id;
    }

    public NearContact() {}

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
        parcel.writeInt(id);
    }

    public static final Parcelable.Creator<NearContact> CREATOR = new Parcelable.Creator<NearContact>(){

        @Override
        public NearContact createFromParcel(Parcel parcel) {
            NearContact nearContact = new NearContact();
            nearContact.imgId = parcel.readInt();
            nearContact.name = parcel.readString();
            nearContact.summary = parcel.readString();
            nearContact.time = parcel.readString();
            nearContact.id = parcel.readInt();
            return nearContact;
        }

        @Override
        public NearContact[] newArray(int i) {
            return new NearContact[i];
        }
    };

}
