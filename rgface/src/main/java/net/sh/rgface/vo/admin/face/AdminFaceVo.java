package net.sh.rgface.vo.admin.face;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/31.
 */
public class AdminFaceVo {

    private int role; //角色

    @NotNull(message = "人脸编号")
    private String faceCode;

    @NotNull(message = "设备编号")
    private String deviceCode;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFaceCode() {
        return faceCode;
    }

    public void setFaceCode(String faceCode) {
        this.faceCode = faceCode;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

}
