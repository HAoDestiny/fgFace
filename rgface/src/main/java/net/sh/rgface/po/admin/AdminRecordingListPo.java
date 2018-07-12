package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/5/21.
 */
public class AdminRecordingListPo {

    private AdminRecordingPo adminRecordingPo;
    private Object devicePo;
    private Object personnelPo;
    private Object facePo;
    private Object environmentImgPo;

    public AdminRecordingPo getAdminRecordingPo() {
        return adminRecordingPo;
    }

    public void setAdminRecordingPo(AdminRecordingPo adminRecordingPo) {
        this.adminRecordingPo = adminRecordingPo;
    }

    public Object getDevicePo() {
        return devicePo;
    }

    public void setDevicePo(Object devicePo) {
        this.devicePo = devicePo;
    }

    public Object getPersonnelPo() {
        return personnelPo;
    }

    public void setPersonnelPo(Object personnelPo) {
        this.personnelPo = personnelPo;
    }

    public Object getFacePo() {
        return facePo;
    }

    public void setFacePo(Object facePo) {
        this.facePo = facePo;
    }

    public Object getEnvironmentImgPo() {
        return environmentImgPo;
    }

    public void setEnvironmentImgPo(Object environmentImgPo) {
        this.environmentImgPo = environmentImgPo;
    }
}
