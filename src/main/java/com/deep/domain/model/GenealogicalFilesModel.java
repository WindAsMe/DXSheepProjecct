package com.deep.domain.model;


import javax.validation.constraints.*;
/**
 * create by zhongrui on 2018/2/1
 **/
public class GenealogicalFilesModel {

    private int id;
    @NotEmpty
    @NotNull
    private String nativeEartag;  //原耳牌
    @NotEmpty
    @NotNull
    private String immuneEartag;  //免疫耳牌
    @NotEmpty
    @NotNull
    @Size(min = 15, max = 15, message = "trademarkEartag need size:15 ")
    @Pattern(regexp = "^[0-9]+$", message = "商标耳牌由数字组成15位")
    private String tradeMarkEartag;  //商标耳牌
    @NotEmpty
    @NotNull
    private String type;     //品种名

    private String brief;    //对该品种的简介 查询时返回
    @NotEmpty
    @NotNull
    private String breedingSheepBase;  //种羊基地
    @NotEmpty
    @NotNull
    private String birthTime;  //出登时间or出生时间
    @Min(1)
    private float birthWeight;  //初登体重
    @NotEmpty
    @NotNull
    private String color;  //颜色
    @NotEmpty
    @NotNull
    private String sex;   //性别
    @NotEmpty
    @NotNull
    private String eartagOfFather;  //父耳牌
    @NotEmpty
    @NotNull
    private String eartagOfMother;  //母耳牌
    @NotEmpty
    @NotNull
    private String eartagOfFathersFather;  //父父耳牌
    @NotEmpty
    @NotNull
    private String eartagOfFathersMother;  //父母耳牌
    @NotEmpty
    @NotNull
    private String eartagOfMothersFather;  //母父耳牌
    @NotEmpty
    @NotNull
    private String eartagOfMothersMother;  //母母耳牌
    @NotEmpty
    @NotNull
    private String remark;   //备注

    private String gmtCreate;     //建立时间

    private String gmtModified;   //修改时间



    public GenealogicalFilesModel() {

    }

    public GenealogicalFilesModel(GenealogicalFilesModel genealogicalFilesModel) {

      this.id = genealogicalFilesModel.getId();
      this.immuneEartag = genealogicalFilesModel.getImmuneEartag();
      this.nativeEartag = genealogicalFilesModel.getNativeEartag();
      this.tradeMarkEartag = genealogicalFilesModel.getTradeMarkEartag();
      this.breedingSheepBase = genealogicalFilesModel.getBreedingSheepBase();
      this.birthTime = genealogicalFilesModel.getBirthTime();
      this.birthWeight = genealogicalFilesModel.getBirthWeight();
      this.color = genealogicalFilesModel.getColor();
      this.sex = genealogicalFilesModel.getSex();
      this.eartagOfFather = genealogicalFilesModel.getEartagOfFather();
      this.eartagOfMother = genealogicalFilesModel.getEartagOfMother();
      this.eartagOfFathersFather = genealogicalFilesModel.getEartagOfFathersFather();
      this.eartagOfFathersMother = genealogicalFilesModel.getEartagOfFathersMother();
      this.eartagOfMothersFather = genealogicalFilesModel.getEartagOfMothersFather();
      this.eartagOfMothersMother = genealogicalFilesModel.getEartagOfMothersMother();
      this.remark = genealogicalFilesModel.getRemark();
      this.gmtCreate = genealogicalFilesModel.getGmtCreate();
      this.gmtModified = genealogicalFilesModel.getGmtModified();


    }

    public GenealogicalFilesModel(String nativeEartag, String immuneEartag, String tradeMarkEartag, String breedingSheepBase, String birthTime, float birthWeight, String color, String sex, String eartagOfFather, String eartagOfMother, String eartagOfFathersFather, String eartagOfFathersMother, String eartagOfMothersFather, String eartagOfMothersMother, String remark) {

        this.nativeEartag = nativeEartag;
        this.immuneEartag = immuneEartag;
        this.tradeMarkEartag = tradeMarkEartag;
        this.breedingSheepBase = breedingSheepBase;
        this.birthTime = birthTime;
        this.birthWeight = birthWeight;
        this.color = color;
        this.sex = sex;
        this.eartagOfFather = eartagOfFather;
        this.eartagOfMother = eartagOfMother;
        this.eartagOfFathersFather = eartagOfFathersFather;
        this.eartagOfFathersMother = eartagOfFathersMother;
        this.eartagOfMothersFather = eartagOfMothersFather;
        this.eartagOfMothersMother = eartagOfMothersMother;
        this.remark = remark;
    }
    public GenealogicalFilesModel( String nativeEartag, String immuneEartag, String tradeMarkEartag, String breedingSheepBase, String birthTime, float birthWeight, String color, String sex, String eartagOfFather, String eartagOfMother, String eartagOfFathersFather, String eartagOfFathersMother, String eartagOfMothersFather, String eartagOfMothersMother, String remark,String gmtCreate) {

        this.nativeEartag = nativeEartag;
        this.immuneEartag = immuneEartag;
        this.tradeMarkEartag = tradeMarkEartag;
        this.breedingSheepBase = breedingSheepBase;
        this.birthTime = birthTime;
        this.birthWeight = birthWeight;
        this.color = color;
        this.sex = sex;
        this.eartagOfFather = eartagOfFather;
        this.eartagOfMother = eartagOfMother;
        this.eartagOfFathersFather = eartagOfFathersFather;
        this.eartagOfFathersMother = eartagOfFathersMother;
        this.eartagOfMothersFather = eartagOfMothersFather;
        this.eartagOfMothersMother = eartagOfMothersMother;
        this.remark = remark;
        this.gmtCreate = gmtCreate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNativeEartag() {
        return nativeEartag;
    }

    public void setNativeEartag(String nativeEartag) {
        this.nativeEartag = nativeEartag;
    }

    public String getImmuneEartag() {
        return immuneEartag;
    }

    public void setImmuneEartag(String immuneEartag) {
        this.immuneEartag = immuneEartag;
    }

    public String getTradeMarkEartag() {
        return tradeMarkEartag;
    }

    public void setTradeMarkEartag(String tradeMarkEartag) {
        this.tradeMarkEartag = tradeMarkEartag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getBreedingSheepBase() {
        return breedingSheepBase;
    }

    public void setBreedingSheepBase(String breedingSheepBase) {
        this.breedingSheepBase = breedingSheepBase;
    }

    public String getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(String birthTime) {
        this.birthTime = birthTime;
    }

    public float getBirthWeight() {
        return birthWeight;
    }

    public void setBirthWeight(float birthWeight) {
        this.birthWeight = birthWeight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEartagOfFather() {
        return eartagOfFather;
    }

    public void setEartagOfFather(String eartagOfFather) {
        this.eartagOfFather = eartagOfFather;
    }

    public String getEartagOfMother() {
        return eartagOfMother;
    }

    public void setEartagOfMother(String eartagOfMother) {
        this.eartagOfMother = eartagOfMother;
    }

    public String getEartagOfFathersFather() {
        return eartagOfFathersFather;
    }

    public void setEartagOfFathersFather(String eartagOfFathersFather) {
        this.eartagOfFathersFather = eartagOfFathersFather;
    }

    public String getEartagOfFathersMother() {
        return eartagOfFathersMother;
    }

    public void setEartagOfFathersMother(String eartagOfFathersMother) {
        this.eartagOfFathersMother = eartagOfFathersMother;
    }

    public String getEartagOfMothersFather() {
        return eartagOfMothersFather;
    }

    public void setEartagOfMothersFather(String eartagOfMothersFather) {
        this.eartagOfMothersFather = eartagOfMothersFather;
    }

    public String getEartagOfMothersMother() {
        return eartagOfMothersMother;
    }

    public void setEartagOfMothersMother(String eartagOfMothersMother) {
        this.eartagOfMothersMother = eartagOfMothersMother;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }


    @Override
    public String toString() {
        return "GenealogicalFilesModel{" +
            "id=" + id +
            ", nativeEartag='" + nativeEartag + '\'' +
            ", immuneEartag='" + immuneEartag + '\'' +
            ", tradeMarkEartag='" + tradeMarkEartag + '\'' +
            ", breedingSheepBase='" + breedingSheepBase + '\'' +
            ", birthTime='" + birthTime + '\'' +
            ", birthWeight=" + birthWeight +
            ", color='" + color + '\'' +
            ", sex='" + sex + '\'' +
            ", eartagOfFather='" + eartagOfFather + '\'' +
            ", eartagOfMother='" + eartagOfMother + '\'' +
            ", eartagOfFathersFather='" + eartagOfFathersFather + '\'' +
            ", eartagOfFathersMother='" + eartagOfFathersMother + '\'' +
            ", eartagOfMothersFather='" + eartagOfMothersFather + '\'' +
            ", eartagOfMothersMother='" + eartagOfMothersMother + '\'' +
            ", remark='" + remark + '\'' +
            ", gmtCreate='" + gmtCreate + '\'' +
            ", gmtModified='" + gmtModified + '\'' +
            '}';
    }
}