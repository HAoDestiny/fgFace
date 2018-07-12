package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/10.
 */
@Entity
@Table(name = "personnel", schema = "rgface", catalog = "")
public class PersonnelEntity {
    private int id;
    private String mobile;
    private String truename;
    private String icCard;
    private String password;
    private String personnelClassCode;
    private String personnelCode;
    private String personnelGrade;
    private String lastLoginIp;
    private int sex;
    private int personnelType;
    private int personnelStatus;
    private int lastLoginTime;
    private int studyType;
    private int accountId;
    private int fileId;
    private int faceId;
    private int createTime;
    private int passTag;
    private int deleteTag;
    private AccountEntity accountByAccountId;
    private FaceReviewEntity faceReviewByFaceReviewId;
//    private FileEntity fileByFileId;
//    private FaceEntity faceByFaceId;
    private Collection<RecordingEntity> recordingsById;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "truename")
    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    @Basic
    @Column(name = "sex")
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    @Basic
    @Column(name = "ic_card")
    public String getIcCard() {
        return icCard;
    }

    public void setIcCard(String icCard) {
        this.icCard = icCard;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "personnel_class_code")
    public String getPersonnelClassCode() {
        return personnelClassCode;
    }

    public void setPersonnelClassCode(String personnelClassCode) {
        this.personnelClassCode = personnelClassCode;
    }

    @Basic
    @Column(name = "personnel_code")
    public String getPersonnelCode() {
        return personnelCode;
    }

    public void setPersonnelCode(String personnelCode) {
        this.personnelCode = personnelCode;
    }

    @Basic
    @Column(name = "personnel_grade")
    public String getPersonnelGrade() {
        return personnelGrade;
    }

    public void setPersonnelGrade(String personnelGrade) {
        this.personnelGrade = personnelGrade;
    }

    @Basic
    @Column(name = "personnel_type")
    public int getPersonnelType() {
        return personnelType;
    }

    public void setPersonnelType(int personnelType) {
        this.personnelType = personnelType;
    }

    @Basic
    @Column(name = "personnel_status")
    public int getPersonnelStatus() {
        return personnelStatus;
    }

    public void setPersonnelStatus(int personnelStatus) {
        this.personnelStatus = personnelStatus;
    }

    @Basic
    @Column(name = "study_type")
    public int getStudyType() {
        return studyType;
    }

    public void setStudyType(int studyType) {
        this.studyType = studyType;
    }

    @Basic
    @Column(name = "account_id")
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "file_id")
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Basic
    @Column(name = "face_id")
    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    @Basic
    @Column(name = "create_time")
    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "last_login_time")
    public int getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(int lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Basic
    @Column(name = "last_login_ip")
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    @Basic
    @Column(name = "pass_tag")
    public int getPassTag() {
        return passTag;
    }

    public void setPassTag(int passTag) {
        this.passTag = passTag;
    }

    @Basic
    @Column(name = "delete_tag")
    public int getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(int deleteTag) {
        this.deleteTag = deleteTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonnelEntity that = (PersonnelEntity) o;
        return id == that.id &&
                sex == that.sex &&
                personnelType == that.personnelType &&
                personnelStatus == that.personnelStatus &&
                studyType == that.studyType &&
                accountId == that.accountId &&
                fileId == that.fileId &&
                faceId == that.faceId &&
                createTime == that.createTime &&
                lastLoginTime == that.lastLoginTime &&
                passTag == that.passTag &&
                deleteTag == that.deleteTag &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(truename, that.truename) &&
                Objects.equals(icCard, that.icCard) &&
                Objects.equals(password, that.password) &&
                Objects.equals(lastLoginIp, that.lastLoginIp) &&
                Objects.equals(personnelClassCode, that.personnelClassCode) &&
                Objects.equals(personnelCode, that.personnelCode) &&
                Objects.equals(personnelGrade, that.personnelGrade);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, mobile, truename, sex, icCard, password, personnelClassCode, personnelCode, personnelGrade, personnelType, personnelStatus, studyType, accountId, fileId, faceId, createTime, lastLoginTime, lastLoginIp, passTag, deleteTag);
    }

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public AccountEntity getAccountByAccountId() {
        return accountByAccountId;
    }

    public void setAccountByAccountId(AccountEntity accountByAccountId) {
        this.accountByAccountId = accountByAccountId;
    }

    @OneToOne(mappedBy = "personnelByPersonnelId")
    public FaceReviewEntity getFaceReviewByFaceReviewId() {
        return faceReviewByFaceReviewId;
    }

    public void setFaceReviewByFaceReviewId(FaceReviewEntity faceReviewByFaceReviewId) {
        this.faceReviewByFaceReviewId = faceReviewByFaceReviewId;
    }

    //    @OneToOne
//    @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
//    public FileEntity getFileByFileId() {
//        return fileByFileId;
//    }
//
//    public void setFileByFileId(FileEntity fileByFileId) {
//        this.fileByFileId = fileByFileId;
//    }
//
//    @OneToOne
//    @JoinColumn(name = "face_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
//    public FaceEntity getFaceByFaceId() {
//        return faceByFaceId;
//    }
//
//    public void setFaceByFaceId(FaceEntity faceByFaceId) {
//        this.faceByFaceId = faceByFaceId;
//    }

    @OneToMany(mappedBy = "personnelByPersonnelId")
    public Collection<RecordingEntity> getRecordingsById() {
        return recordingsById;
    }

    public void setRecordingsById(Collection<RecordingEntity> recordingsById) {
        this.recordingsById = recordingsById;
    }
}
