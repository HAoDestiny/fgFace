package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/6/1.
 */
@Entity
@Table(name = "environment_img", schema = "rgface", catalog = "")
public class EnvironmentImgEntity {
    private int id;
    private String environmentUri;
    private int createTime;
    private int deleteTag;
    private RecordingEntity recordingById;

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
    @Column(name = "environment_uri")
    public String getEnvironmentUri() {
        return environmentUri;
    }

    public void setEnvironmentUri(String environmentUri) {
        this.environmentUri = environmentUri;
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
        EnvironmentImgEntity that = (EnvironmentImgEntity) o;
        return id == that.id &&
                createTime == that.createTime &&
                deleteTag == that.deleteTag &&
                Objects.equals(environmentUri, that.environmentUri);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, environmentUri, createTime, deleteTag);
    }

    @OneToOne(mappedBy = "environmentImgByEnvironmentImgId")
    public RecordingEntity getRecordingById() {
        return recordingById;
    }

    public void setRecordingById(RecordingEntity recordingById) {
        this.recordingById = recordingById;
    }
}
