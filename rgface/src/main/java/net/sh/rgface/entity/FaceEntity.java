package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/10.
 */
@Entity
@Table(name = "face", schema = "rgface", catalog = "")
public class FaceEntity {
    private int id;
    private String faceUri;
    private int createTime;
    private int deleteTag;
//    private PersonnelEntity personnelById;

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
    @Column(name = "face_uri")
    public String getFaceUri() {
        return faceUri;
    }

    public void setFaceUri(String faceUri) {
        this.faceUri = faceUri;
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
        FaceEntity that = (FaceEntity) o;
        return id == that.id &&
                createTime == that.createTime &&
                deleteTag == that.deleteTag &&
                Objects.equals(faceUri, that.faceUri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, faceUri, createTime, deleteTag);
    }

//    @OneToOne(mappedBy = "faceByFaceId")
//    public PersonnelEntity getPersonnelById() {
//        return personnelById;
//    }
//
//    public void setPersonnelById(PersonnelEntity personnelById) {
//        this.personnelById = personnelById;
//    }
}
