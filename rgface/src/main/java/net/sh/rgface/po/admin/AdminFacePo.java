package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/5/30.
 */
public class AdminFacePo {

    private String faceId;
    private String faceName;
    private String expireDate; //有效时间
    private int faceRole; //角色
    private long faceWiegandNo; //韦根号
    private Object facePo;

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getFaceName() {
        return faceName;
    }

    public void setFaceName(String faceName) {
        this.faceName = faceName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getFaceRole() {
        return faceRole;
    }

    public void setFaceRole(int faceRole) {
        this.faceRole = faceRole;
    }

    public long getFaceWiegandNo() {
        return faceWiegandNo;
    }

    public void setFaceWiegandNo(long faceWiegandNo) {
        this.faceWiegandNo = faceWiegandNo;
    }

    public Object getFacePo() {
        return facePo;
    }

    public void setFacePo(Object facePo) {
        this.facePo = facePo;
    }
}
