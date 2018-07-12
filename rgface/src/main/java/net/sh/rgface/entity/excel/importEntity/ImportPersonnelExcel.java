package net.sh.rgface.entity.excel.importEntity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/6/15.
 */

public class ImportPersonnelExcel {

    @Excel(name = "id")
    private int id;

    @NotNull
    @Excel(name = "手机号码")
    private String mobile;

    @NotNull
    @Excel(name = "真实姓名")
    private String truename;

    @Excel(name = "性别", replace = {"男_1", "女_0"})
    private int sex;

    @NotNull
    @Excel(name = "身份证")
    private String icCard;

    @NotNull
    @Excel(name = "密码")
    private String password;

    @NotNull
    @Excel(name = "班级")
    private String personnelClassCode;

    @NotNull
    @Excel(name = "学号")
    private String personnelCode;

    @NotNull
    @Excel(name = "年级")
    private String personnelGrade;

    @Excel(name = "学生类型", replace = {"学生_0", "教职工_1", "外来人员_2", "其他_3"})
    private int personnelType;

    @Excel(name = "出入状态", replace = {"正常_0", "外出_1", "请假_2", "其他_3"})
    private int personnelStatus;

    @Excel(name = "就读类型", replace = {"住读生_0", "走读生_1", "其他_2"})
    private int studyType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getIcCard() {
        return icCard;
    }

    public void setIcCard(String icCard) {
        this.icCard = icCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getPersonnelGrade() {
        return personnelGrade;
    }

    public void setPersonnelGrade(String personnelGrade) {
        this.personnelGrade = personnelGrade;
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
}
