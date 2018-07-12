package net.sh.rgface.vo.admin.device;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/21.
 */
public class DeviceVo {

    @NotNull(message = "设备名称")
    private String deviceName;

    @NotNull(message = "设备编号")
    private String deviceCode;

    private String devicePointName;

    private String devicePointCode;

    @NotNull(message = "数据服务IP")
    private String dataServerIp;

    private short dataServerPort = 0;

    @NotNull(message = "配置服务IP")
    private String configServerIp;

    private short configServerPort = 0;

    private String deviceFirmwareVersion;

    private String deviceNote;

    private int contrastStatus = -1;

    private int repeatStatus = -1;

    private int deviceType = 0;

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

    public String getDevicePointName() {
        return devicePointName;
    }

    public void setDevicePointName(String devicePointName) {
        this.devicePointName = devicePointName;
    }

    public String getDevicePointCode() {
        return devicePointCode;
    }

    public void setDevicePointCode(String devicePointCode) {
        this.devicePointCode = devicePointCode;
    }

    public String getDataServerIp() {
        return dataServerIp;
    }

    public void setDataServerIp(String dataServerIp) {
        this.dataServerIp = dataServerIp;
    }

    public short getDataServerPort() {
        return dataServerPort;
    }

    public void setDataServerPort(short dataServerPort) {
        this.dataServerPort = dataServerPort;
    }

    public String getConfigServerIp() {
        return configServerIp;
    }

    public void setConfigServerIp(String configServerIp) {
        this.configServerIp = configServerIp;
    }

    public short getConfigServerPort() {
        return configServerPort;
    }

    public void setConfigServerPort(short configServerPort) {
        this.configServerPort = configServerPort;
    }

    public String getDeviceFirmwareVersion() {
        return deviceFirmwareVersion;
    }

    public void setDeviceFirmwareVersion(String deviceFirmwareVersion) {
        this.deviceFirmwareVersion = deviceFirmwareVersion;
    }

    public String getDeviceNote() {
        return deviceNote;
    }

    public void setDeviceNote(String deviceNote) {
        this.deviceNote = deviceNote;
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

    public void setRepeatStatus(int repeatStatus) {
        this.repeatStatus = repeatStatus;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
