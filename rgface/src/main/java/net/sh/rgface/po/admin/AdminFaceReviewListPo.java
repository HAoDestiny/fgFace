package net.sh.rgface.po.admin;

import net.sh.rgface.po.file.FilePo;

/**
 * Created by DESTINY on 2018/6/22.
 */
public class AdminFaceReviewListPo {

    private FilePo filePo;
    private AdminFaceReviewPo adminFaceReviewPo;
    private AdminPersonnelPo adminPersonnelPo;

    public FilePo getFilePo() {
        return filePo;
    }

    public void setFilePo(FilePo filePo) {
        this.filePo = filePo;
    }

    public AdminFaceReviewPo getAdminFaceReviewPo() {
        return adminFaceReviewPo;
    }

    public void setAdminFaceReviewPo(AdminFaceReviewPo adminFaceReviewPo) {
        this.adminFaceReviewPo = adminFaceReviewPo;
    }

    public AdminPersonnelPo getAdminPersonnelPo() {
        return adminPersonnelPo;
    }

    public void setAdminPersonnelPo(AdminPersonnelPo adminPersonnelPo) {
        this.adminPersonnelPo = adminPersonnelPo;
    }
}
