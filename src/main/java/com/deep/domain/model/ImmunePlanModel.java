package com.deep.domain.model;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class ImmunePlanModel {
    //private BigInteger id;      //id
    private BigInteger factoryNum;  //工厂编号
    private String crowdNum;     //羊群号
    private String immuneEartag;    //耳牌附件号
    private String immuneTime;     //接种时间
    private String immuneKind;     //免疫种类
    private String immuneWay;       //免疫方法
    private String immuneQuality;    //免疫剂量
    private String immuneDuring;    //免疫期
    private String operator;     //操作员
    private String professor;    //技术审核
    private String supervisor;    //监督员
    private String remark;      //备注
    private String isPass1;      //是否通过审核 默认 未审核 0
    private String unpassReason1;   //未通过审核原因
    private String isPass2;      //是否通过监督 默认 未监督 0
    private String unpassReason2;   //未通过监督原因
    private Timestamp gmtCreate;    //创建时间
    private Timestamp gmtModified;  //修改时间
    private Timestamp gmtProfessor;    //技术审核时间 可空
    private Timestamp gmtSupervise;    //监督审核时间 可空

    public ImmunePlanModel() {
    }

    public ImmunePlanModel(BigInteger factoryNum, String crowdNum, String immuneEartag, String immuneTime, String immuneKind, String immuneWay, String immuneQuality, String immuneDuring, String operator, String remark, String isPass1, String isPass2,Timestamp gmtCreate, Timestamp gmtModified) {
        this.factoryNum = factoryNum;
        this.crowdNum = crowdNum;
        this.immuneEartag = immuneEartag;
        this.immuneTime = immuneTime;
        this.immuneKind = immuneKind;
        this.immuneWay = immuneWay;
        this.immuneQuality = immuneQuality;
        this.immuneDuring = immuneDuring;
        this.operator = operator;
        this.remark = remark;
        this.isPass1 = isPass1;
        this.isPass2 = isPass2;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
    }

    public ImmunePlanModel(BigInteger factoryNum, String crowdNum, String immuneEartag, String immuneTime, String immuneKind, String immuneWay, String immuneQuality, String immuneDuring, String operator, String professor, String supervisor, String remark, String isPass1,  String isPass2, Timestamp gmtCreate, Timestamp gmtModified, Timestamp gmtProfessor, Timestamp gmtSupervise) {
        this.factoryNum = factoryNum;
        this.crowdNum = crowdNum;
        this.immuneEartag = immuneEartag;
        this.immuneTime = immuneTime;
        this.immuneKind = immuneKind;
        this.immuneWay = immuneWay;
        this.immuneQuality = immuneQuality;
        this.immuneDuring = immuneDuring;
        this.operator = operator;
        this.professor = professor;
        this.supervisor = supervisor;
        this.remark = remark;
        this.isPass1 = isPass1;
        this.isPass2 = isPass2;
        this.gmtCreate = gmtCreate;
        this.gmtModified = gmtModified;
        this.gmtProfessor = gmtProfessor;
        this.gmtSupervise = gmtSupervise;

    }

    public BigInteger getFactoryNum() {
        return factoryNum;
    }

    public void setFactoryNum(BigInteger factoryNum) {
        this.factoryNum = factoryNum;
    }

    public String getCrowdNum() {
        return crowdNum;
    }

    public void setCrowdNum(String crowdNum) {
        this.crowdNum = crowdNum;
    }

    public String getImmuneEartag() {
        return immuneEartag;
    }

    public void setImmuneEartag(String immuneEartag) {
        this.immuneEartag = immuneEartag;
    }

    public String getImmuneTime() {
        return immuneTime;
    }

    public void setImmuneTime(String immuneTime) {
        this.immuneTime = immuneTime;
    }

    public String getImmuneKind() {
        return immuneKind;
    }

    public void setImmuneKind(String immuneKind) {
        this.immuneKind = immuneKind;
    }

    public String getImmuneWay() {
        return immuneWay;
    }

    public void setImmuneWay(String immuneWay) {
        this.immuneWay = immuneWay;
    }

    public String getImmuneQuality() {
        return immuneQuality;
    }

    public void setImmuneQuality(String immuneQuality) {
        this.immuneQuality = immuneQuality;
    }

    public String getImmuneDuring() {
        return immuneDuring;
    }

    public void setImmuneDuring(String immuneDuring) {
        this.immuneDuring = immuneDuring;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsPass1() {
        return isPass1;
    }

    public void setIsPass1(String isPass1) {
        this.isPass1 = isPass1;
    }

    public String getUnpassReason1() {
        return unpassReason1;
    }

    public void setUnpassReason1(String unpassReason1) {
        this.unpassReason1 = unpassReason1;
    }

    public String getIsPass2() {
        return isPass2;
    }

    public void setIsPass2(String isPass2) {
        this.isPass2 = isPass2;
    }

    public String getUnpassReason2() {
        return unpassReason2;
    }

    public void setUnpassReason2(String unpassReason2) {
        this.unpassReason2 = unpassReason2;
    }

    public Timestamp getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Timestamp gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Timestamp getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Timestamp gmtModified) {
        this.gmtModified = gmtModified;
    }
    public Timestamp getGmtProfessor() {
        return gmtProfessor;
    }

    public void setGmtProfessor(Timestamp gmtProfessor) {
        this.gmtProfessor = gmtProfessor;
    }

    public Timestamp getGmtSupervise() {
        return gmtSupervise;
    }

    public void setGmtSupervise(Timestamp gmtSupervise) {
        this.gmtSupervise = gmtSupervise;
    }
}
