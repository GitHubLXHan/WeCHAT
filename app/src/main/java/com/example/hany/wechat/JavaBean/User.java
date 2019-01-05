package com.example.hany.wechat.JavaBean;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/12/28 11:42
 * @filName User
 * @describe ...
 */
public class User {
    private int id;
    private String userId;
    private String userName;
    private String password;
    private String createTime;
    private int imgId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
