package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/2.
 */
@Entity
@Table(name = "recording", schema = "rgface", catalog = "")
public class RecordingEntity {
    private int id;
    private int personnelId;
    private int deviceId;
    private int createTime;
    private int deleteTag;
    private int environmentId;
    private PersonnelEntity personnelByPersonnelId;
    private DeviceEntity deviceByDeviceId;
    private EnvironmentImgEntity environmentImgByEnvironmentImgId;

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
    @Column(name = "personnel_id")
    public int getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    @Basic
    @Column(name = "device_id")
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
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

    @Basic
    @Column(name = "environment_id")
    public int getEnvironmentId() {
        return environmentId;
    }

    public void setEnvironmentId(int environmentId) {
        this.environmentId = environmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordingEntity that = (RecordingEntity) o;
        return id == that.id &&
                personnelId == that.personnelId &&
                deviceId == that.deviceId &&
                createTime == that.createTime &&
                deleteTag == that.deleteTag &&
                environmentId == that.environmentId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, personnelId, deviceId, createTime, deleteTag, environmentId);
    }

    @ManyToOne
    @JoinColumn(name = "personnel_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public PersonnelEntity getPersonnelByPersonnelId() {
        return personnelByPersonnelId;
    }

    public void setPersonnelByPersonnelId(PersonnelEntity personnelByPersonnelId) {
        this.personnelByPersonnelId = personnelByPersonnelId;
    }

    @ManyToOne
    @JoinColumn(name = "device_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public DeviceEntity getDeviceByDeviceId() {
        return deviceByDeviceId;
    }

    public void setDeviceByDeviceId(DeviceEntity deviceByDeviceId) {
        this.deviceByDeviceId = deviceByDeviceId;
    }

    @OneToOne
    @JoinColumn(name = "environment_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public EnvironmentImgEntity getEnvironmentImgByEnvironmentImgId() {
        return environmentImgByEnvironmentImgId;
    }

    public void setEnvironmentImgByEnvironmentImgId(EnvironmentImgEntity environmentImgByEnvironmentImgId) {
        this.environmentImgByEnvironmentImgId = environmentImgByEnvironmentImgId;
    }
}
