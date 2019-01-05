package com.example.hany.wechat.JavaBean;

/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2019/1/1 16:24
 * @filName Contract
 * @describe ...
 */
public class Contract {

    private int id;
    private String contractId;
    private String contractName;
    private String addTime;
    private int imgId;

    public Contract(String contractId, String contractName, String addTime, int imgPath) {
        this.contractId = contractId;
        this.contractName = contractName;
        this.addTime = addTime;
        this.imgId = imgPath;
    }

    public Contract() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
