package net.sh.rgface.po.base;

import net.sh.rgface.po.file.FilePo;

/**
 * Created by DESTINY on 2018/5/10.
 */
public class UploadFilePo {

    private int fileId;
    private FilePo filePo;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public FilePo getFilePo() {
        return filePo;
    }

    public void setFilePo(FilePo filePo) {
        this.filePo = filePo;
    }
}
