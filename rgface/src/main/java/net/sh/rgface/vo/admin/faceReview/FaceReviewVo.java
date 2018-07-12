package net.sh.rgface.vo.admin.faceReview;

/**
 * Created by DESTINY on 2018/6/22.
 */
public class FaceReviewVo {

    //0资料审核 1下发
    private int type = -1;

    //1通过 2重新审核/拒绝 3撤销
    private int status = 2;
    private int faceReviewId = -1;
    private int personnelId = -1;
    private String deviceId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFaceReviewId() {
        return faceReviewId;
    }

    public void setFaceReviewId(int faceReviewId) {
        this.faceReviewId = faceReviewId;
    }

    public int getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
