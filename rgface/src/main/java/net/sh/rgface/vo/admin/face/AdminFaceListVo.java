package net.sh.rgface.vo.admin.face;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/30.
 */
public class AdminFaceListVo {

    @NotNull(message = "设备编号")
    private String deviceCode;

    private int pageCode = 1;
    private int pageSize = 10;

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
