package net.sh.rgface.vo.admin.account;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/7.
 */
public class AdminUpdatePasswordVo {

    @NotNull(message = "原密码")
    private String oldPassword;

    @NotNull(message = "新密码")
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
