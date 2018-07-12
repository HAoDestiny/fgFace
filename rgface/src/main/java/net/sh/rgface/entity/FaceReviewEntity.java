package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/6/22.
 */
@Entity
@Table(name = "face_review", schema = "rgface", catalog = "")
public class FaceReviewEntity {
    private int id;
    private int personnelId;
    private String faceReviewUri;
    private Integer status;
    private Integer createTime;
    private Integer lssuedStatus;
    private Integer deleteTag;
    private PersonnelEntity personnelByPersonnelId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "personnel_id")
    public int getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    @Basic
    @Column(name = "face_review_uri")
    public String getFaceReviewUri() {
        return faceReviewUri;
    }

    public void setFaceReviewUri(String faceReviewUri) {
        this.faceReviewUri = faceReviewUri;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "lssued_status")
    public Integer getLssuedStatus() {
        return lssuedStatus;
    }

    public void setLssuedStatus(Integer lssuedStatus) {
        this.lssuedStatus = lssuedStatus;
    }

    @Basic
    @Column(name = "create_time")
    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "delete_tag")
    public Integer getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(Integer deleteTag) {
        this.deleteTag = deleteTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FaceReviewEntity that = (FaceReviewEntity) o;
        return id == that.id &&
                personnelId == that.personnelId &&
                Objects.equals(faceReviewUri, that.faceReviewUri) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(deleteTag, that.deleteTag);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, personnelId, faceReviewUri, status, createTime, deleteTag);
    }

    @OneToOne
    @JoinColumn(name = "personnel_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public PersonnelEntity getPersonnelByPersonnelId() {
        return personnelByPersonnelId;
    }

    public void setPersonnelByPersonnelId(PersonnelEntity personnelByPersonnelId) {
        this.personnelByPersonnelId = personnelByPersonnelId;
    }
}
