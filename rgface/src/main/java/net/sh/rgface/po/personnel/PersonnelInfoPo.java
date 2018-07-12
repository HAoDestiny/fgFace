package net.sh.rgface.po.personnel;

import net.sh.rgface.po.file.FilePo;

/**
 * Created by DESTINY on 2018/5/14.
 */
public class PersonnelInfoPo {

    private int personnelId;
    private String mobile;
    private String icCard;
    private String truename;
    private String personnelClassCode;
    private String personnelCode;
    private String personnelGrade;
    private String personnelByAccountName;

    private int avatarId;
    private int faceId;
    private int passTag;
    private int sex = 0; //0女 1男
    private int studyType = 0; //0住读 1 走读
    private int personnelType = 0;
    private int personnelStart = 0;
    private int faceReviewStatus = 4; //0审核中 1通过 2失败 3删除 4未提交

    private FilePo avatar;
    private FilePo face;

    public int getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
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

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public int getPassTag() {
        return passTag;
    }

    public void setPassTag(int passTag) {
        this.passTag = passTag;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStudyType() {
        return studyType;
    }

    public void setStudyType(int studyType) {
        this.studyType = studyType;
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

    public FilePo getAvatar() {
        return avatar;
    }

    public void setAvatar(FilePo avatar) {
        this.avatar = avatar;
    }

    public FilePo getFace() {
        return face;
    }

    public void setFace(FilePo face) {
        this.face = face;
    }

    public int getFaceReviewStatus() {
        return faceReviewStatus;
    }

    public void setFaceReviewStatus(int faceReviewStatus) {
        this.faceReviewStatus = faceReviewStatus;
    }
}
