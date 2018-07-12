package net.sh.rgface.vo.personnel;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/14.
 */
public class PersonnelUpdatePasswordVo {

    @NotNull(message = "新密码")
    private String password;

    @NotNull(message = "密码")
    private String oldPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
