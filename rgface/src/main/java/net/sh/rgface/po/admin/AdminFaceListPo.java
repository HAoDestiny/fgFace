package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/5/30.
 */


public class AdminFaceListPo {

    private Object adminFacePo;
    private int entriesTotal; //条目总数
    private int pageCode;
    private int pageSize;
    private String deviceCode;

    public Object getAdminFacePo() {
        return adminFacePo;
    }

    public void setAdminFacePo(Object adminFacePo) {
        this.adminFacePo = adminFacePo;
    }

    public int getEntriesTotal() {
        return entriesTotal;
    }

    public void setEntriesTotal(int entriesTotal) {
        this.entriesTotal = entriesTotal;
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

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
