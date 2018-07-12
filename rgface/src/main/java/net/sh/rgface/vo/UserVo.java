package net.sh.rgface.vo;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/4/12.
 */
public class UserVo {

    @NotNull(message = "账号")
    private String account;

    @NotNull(message = "密码")
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
