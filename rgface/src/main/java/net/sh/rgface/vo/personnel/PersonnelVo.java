package net.sh.rgface.vo.personnel;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/9.
 */
public class PersonnelVo {

    @NotNull(message = "真实姓名")
    private String truename;

    @NotNull(message = "身份证")
    private String icCard;

    private String password;

    @NotNull(message = "手机号")
    private String mobile;

    @NotNull(message = "班级")
    private String personnelClassCode;

    @NotNull(message = "编号")
    private String personnelCode;

    @NotNull(message = "年级")
    private String personnelGrade;

//    @NotNull(message = "所属帐户名")
    private String personnelByAccountName;

    private int personnelId = 0;

    private int fileId = 1;
    private int faceId = 1;

    private int sex = 0;
    private int studyType = 0;
    private int personnelType = 0;
    private int personnelStart = 0;

    public int getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
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

    public String getPersonnelByAccountName() {
        return personnelByAccountName;
    }

    public void setPersonnelByAccountName(String personnelByAccountName) {
        this.personnelByAccountName = personnelByAccountName;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getFileId() {
        return fileId;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getStudyType() {
        return studyType;
    }

    public void setStudyType(int studyType) {
        this.studyType = studyType;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getPersonnelType() {
        return personnelType;
    }

    public void setPersonnelType(int personnelType) {
        this.personnelType = personnelType;
    }

    public int getPersonnelStart() {
        return personnelStart;
    }

    public void setPersonnelStart(int personnelStart) {
        this.personnelStart = personnelStart;
    }
}
