package net.sh.rgface.entity.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/6/15.
 */

public class PersonnelExcel {

    private int id;

    @Excel(name = "真实姓名")
    private String truename;

    @Excel(name = "手机号码")
    private String mobile;

    @Excel(name = "身份证")
    private String icCard;

    @Excel(name = "性别", replace = {"男_1", "女_0"})
    private int sex;

    @Excel(name = "年级")
    private String personnelGrade;

    @Excel(name = "班级")
    private String personnelClassCode;

    @Excel(name = "学号")
    private String personnelCode;

    @Excel(name = "类型", replace = {"学生_0", "教职工_1", "外来人员_2", "其他_3"})
    private int personnelType;

    @Excel(name = "状态", replace = {"正常_0", "外出_1", "请假_2", "其他_3"})
    private int personnelStatus;

    @Excel(name = "就读类型", replace = {"住读生_0", "走读生_1", "其他_2"})
    private int studyType;

    @Excel(name = "人脸角色", replace = {"白名单_0", "黑名单_1"})
    private int passTag;

    @Excel(name = "账号密码")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIcCard() {
        return icCard;
    }

    public void setIcCard(String icCard) {
        this.icCard = icCard;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPersonnelGrade() {
        return personnelGrade;
    }

    public void setPersonnelGrade(String personnelGrade) {
        this.personnelGrade = personnelGrade;
    }

    public String getPersonnelClassCode() {
        return personnelClassCode;
    }

    public void setPersonnelClassCode(String personnelClassCode) {
        this.personnelClassCode = personnelClassCode;
    }

    public String getPersonnelCode() {
        return personnelCode;
    }

    public void setPersonnelCode(String personnelCode) {
        this.personnelCode = personnelCode;
    }

    public int getPersonnelType() {
        return personnelType;
    }

    public void setPersonnelType(int personnelType) {
        this.personnelType = personnelType;
    }

    public int getPersonnelStatus() {
        return personnelStatus;
    }

    public void setPersonnelStatus(int personnelStatus) {
        this.personnelStatus = personnelStatus;
    }

    public int getStudyType() {
        return studyType;
    }

    public void setStudyType(int studyType) {
        this.studyType = studyType;
    }

    public int getPassTag() {
        return passTag;
    }

    public void setPassTag(int passTag) {
        this.passTag = passTag;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
