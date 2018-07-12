package net.sh.rgface.po.admin;

/**
 * Created by DESTINY on 2018/6/22.
 */
public class AdminFaceReviewPo {

    private int id;
    private int status;
    private int deleteTag;
    private int createTime;
    private int lssuedStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDeleteTag() {
        return deleteTag;
    }

    public void setDeleteTag(int deleteTag) {
        this.deleteTag = deleteTag;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getLssuedStatus() {
        return lssuedStatus;
    }

    public void setLssuedStatus(int lssuedStatus) {
        this.lssuedStatus = lssuedStatus;
    }
}
