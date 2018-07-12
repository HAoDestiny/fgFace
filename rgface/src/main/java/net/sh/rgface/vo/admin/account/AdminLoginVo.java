package net.sh.rgface.vo.admin.account;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/5.
 */
public class AdminLoginVo {

    @NotNull(message = "用户名")
    private String accountName;

    @NotNull(message = "密码")
    private String password;

    private String verifyCode;

    private int time = (int) (System.currentTimeMillis() / 1000);

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
