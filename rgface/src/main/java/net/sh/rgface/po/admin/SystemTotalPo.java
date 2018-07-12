package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/5/7.
 */
public class SystemTotalPo {

    private int adminAccountCount;
    private int personnelCount;
    private int userCount;
    private int deviceCount;
    private int recordingCount;

    public int getAdminAccountCount() {
        return adminAccountCount;
    }

    public void setAdminAccountCount(int adminAccountCount) {
        this.adminAccountCount = adminAccountCount;
    }

    public int getPersonnelCount() {
        return personnelCount;
    }

    public void setPersonnelCount(int personnelCount) {
        this.personnelCount = personnelCount;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public int getRecordingCount() {
        return recordingCount;
    }

    public void setRecordingCount(int recordingCount) {
        this.recordingCount = recordingCount;
    }
}
