package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/10.
 */
@Entity
@Table(name = "file", schema = "rgface", catalog = "")
public class FileEntity {
    private int id;
    private String fileUri;
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
    @Column(name = "file_uri")
    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
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
        FileEntity that = (FileEntity) o;
        return id == that.id &&
                createTime == that.createTime &&
                deleteTag == that.deleteTag &&
                Objects.equals(fileUri, that.fileUri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, fileUri, createTime, deleteTag);
    }

//    @OneToOne(mappedBy = "fileByFileId")
//    public PersonnelEntity getPersonnelById() {
//        return personnelById;
//    }
//
//    public void setPersonnelById(PersonnelEntity personnelById) {
//        this.personnelById = personnelById;
//    }
}
