package com.example.hany.wechat.JavaBean;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/16 9:23
 * @filName Msg
 * @describe ...
 */
public class Msg {

    private static int TYPE_RECEIVED = 0;

    private static int TYPE_SEND = 0;

    private String content;

    private int type;

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
}
