package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/5/21.
 */
public class AdminDevicePo {

    private int deviceId;
    private String deviceName;
    private String deviceCode;
    private String dataServerIp;
    private String dataServerPort;
    private String configServerIp;
    private String configServerPort;
    private String deviceNote;
    private String deviceFirmwareVersion;
    private int createTime;
    private int deleteTag;
    private int deviceStatus = 0; //当前设备状态 0离线 1在线
    private int contrastStatus;
    private int contrastLike;
    private int repeatStatus;
    private int repeatTimeInterval;
    private int deviceType;
    private Object accountInfo;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public String getDataServerIp() {
        return dataServerIp;
    }

    public void setDataServerIp(String dataServerIp) {
        this.dataServerIp = dataServerIp;
    }

    public String getDataServerPort() {
        return dataServerPort;
    }

    public void setDataServerPort(String dataServerPort) {
        this.dataServerPort = dataServerPort;
    }

    public String getConfigServerIp() {
        return configServerIp;
    }

    public void setConfigServerIp(String configServerIp) {
        this.configServerIp = configServerIp;
    }

    public String getConfigServerPort() {
        return configServerPort;
    }

    public void setConfigServerPort(String configServerPort) {
        this.configServerPort = configServerPort;
    }

    public String getDeviceNote() {
        return deviceNote;
    }

    public void setDeviceNote(String deviceNote) {
        this.deviceNote = deviceNote;
    }

    public String getDeviceFirmwareVersion() {
        return deviceFirmwareVersion;
    }

    public void setDeviceFirmwareVersion(String deviceFirmwareVersion) {
        this.deviceFirmwareVersion = deviceFirmwareVersion;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(int deleteTag) {
        this.deleteTag = deleteTag;
    }

    public int getContrastStatus() {
        return contrastStatus;
    }

    public void setContrastStatus(int contrastStatus) {
        this.contrastStatus = contrastStatus;
    }

    public int getRepeatStatus() {
        return repeatStatus;
    }

    public int getContrastLike() {
        return contrastLike;
    }

    public void setContrastLike(int contrastLike) {
        this.contrastLike = contrastLike;
    }

    public void setRepeatStatus(int repeatStatus) {
        this.repeatStatus = repeatStatus;
    }

    public int getRepeatTimeInterval() {
        return repeatTimeInterval;
    }

    public void setRepeatTimeInterval(int repeatTimeInterval) {
        this.repeatTimeInterval = repeatTimeInterval;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public Object getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(Object accountInfo) {
        this.accountInfo = accountInfo;
    }
}
