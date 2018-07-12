package net.sh.rgface.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/2.
 */
@Entity
@Table(name = "device", schema = "rgface", catalog = "")
public class DeviceEntity {
    private int id;
    private String deviceName;
    private String devicePointNumber;
    private String deviceCode;
    private String devicePointName;
    private String deviceNote;
    private String dataServerIp;
    private String dataServerPort;
    private String configServerIp;
    private String configServerPort;
    private String deviceFirmwareVersion;
    private int deviceType;
    private int accountId;
    private int createTime;
    private int deleteTag;
    private int contrastStatus; //开启对比 0开启 1禁用
    private int contrastLike; //相似度
    private int repeatStatus; //去除重复 0去除 1不去除
    private int repeatTimeInterval; //重复识别间隔(s) 最大255
    private AccountEntity accountByAccountId;
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
    @Column(name = "device_name")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Basic
    @Column(name = "device_point_number")
    public String getDevicePointNumber() {
        return devicePointNumber;
    }

    public void setDevicePointNumber(String devicePointNumber) {
        this.devicePointNumber = devicePointNumber;
    }

    @Basic
    @Column(name = "device_code")
    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    @Basic
    @Column(name = "device_point_name")
    public String getDevicePointName() {
        return devicePointName;
    }

    public void setDevicePointName(String devicePointName) {
        this.devicePointName = devicePointName;
    }

    @Basic
    @Column(name = "device_note")
    public String getDeviceNote() {
        return deviceNote;
    }

    public void setDeviceNote(String deviceNote) {
        this.deviceNote = deviceNote;
    }

    @Basic
    @Column(name = "data_server_ip")
    public String getDataServerIp() {
        return dataServerIp;
    }

    public void setDataServerIp(String dataServerIp) {
        this.dataServerIp = dataServerIp;
    }

    @Basic
    @Column(name = "data_server_port")
    public String getDataServerPort() {
        return dataServerPort;
    }

    public void setDataServerPort(String dataServerPort) {
        this.dataServerPort = dataServerPort;
    }

    @Basic
    @Column(name = "config_server_ip")
    public String getConfigServerIp() {
        return configServerIp;
    }

    public void setConfigServerIp(String configServerIp) {
        this.configServerIp = configServerIp;
    }

    @Basic
    @Column(name = "config_server_port")
    public String getConfigServerPort() {
        return configServerPort;
    }

    public void setConfigServerPort(String configServerPort) {
        this.configServerPort = configServerPort;
    }

    @Basic
    @Column(name = "device_firmware_version")
    public String getDeviceFirmwareVersion() {
        return deviceFirmwareVersion;
    }

    public void setDeviceFirmwareVersion(String deviceFirmwareVersion) {
        this.deviceFirmwareVersion = deviceFirmwareVersion;
    }

    @Basic
    @Column(name = "device_type")
    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Basic
    @Column(name = "contrast_status")
    public int getContrastStatus() {
        return contrastStatus;
    }

    public void setContrastStatus(int contrastStatus) {
        this.contrastStatus = contrastStatus;
    }

    @Basic
    @Column(name = "contrast_like")
    public int getContrastLike() {
        return contrastLike;
    }

    public void setContrastLike(int contrastLike) {
        this.contrastLike = contrastLike;
    }

    @Basic
    @Column(name = "repeat_status")
    public int getRepeatStatus() {
        return repeatStatus;
    }

    public void setRepeatStatus(int repeatStatus) {
        this.repeatStatus = repeatStatus;
    }

    @Basic
    @Column(name = "repeat_time_interval")
    public int getRepeatTimeInterval() {
        return repeatTimeInterval;
    }

    public void setRepeatTimeInterval(int repeatTimeInterval) {
        this.repeatTimeInterval = repeatTimeInterval;
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
        DeviceEntity that = (DeviceEntity) o;
        return id == that.id &&
                deviceType == that.deviceType &&
                accountId == that.accountId &&
                createTime == that.createTime &&
                contrastStatus == that.contrastStatus &&
                contrastLike == that.contrastLike &&
                repeatStatus == that.repeatStatus &&
                repeatTimeInterval == that.repeatTimeInterval &&
                deleteTag == that.deleteTag &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(devicePointNumber, that.devicePointNumber) &&
                Objects.equals(deviceCode, that.deviceCode) &&
                Objects.equals(devicePointName, that.devicePointName) &&
                Objects.equals(deviceNote, that.deviceNote) &&
                Objects.equals(dataServerIp, that.dataServerIp) &&
                Objects.equals(dataServerPort, that.dataServerPort) &&
                Objects.equals(configServerIp, that.configServerIp) &&
                Objects.equals(configServerPort, that.configServerPort) &&
                Objects.equals(deviceFirmwareVersion, that.deviceFirmwareVersion);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, deviceName, devicePointNumber, deviceCode, devicePointName, deviceNote, dataServerIp, dataServerPort, configServerIp, configServerPort, deviceFirmwareVersion, deviceType, accountId, contrastStatus, contrastLike, repeatStatus, repeatTimeInterval, createTime, deleteTag);
    }

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public AccountEntity getAccountByAccountId() {
        return accountByAccountId;
    }

    public void setAccountByAccountId(AccountEntity accountByAccountId) {
        this.accountByAccountId = accountByAccountId;
    }

    @OneToMany(mappedBy = "deviceByDeviceId")
    public Collection<RecordingEntity> getRecordingsById() {
        return recordingsById;
    }

    public void setRecordingsById(Collection<RecordingEntity> recordingsById) {
        this.recordingsById = recordingsById;
    }
}
