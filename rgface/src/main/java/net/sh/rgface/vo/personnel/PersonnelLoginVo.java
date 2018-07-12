package net.sh.rgface.vo.personnel;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/14.
 */
public class PersonnelLoginVo {

    @NotNull(message = "用户名")
    private String personnelIcCard;

    @NotNull(message = "密码")
    private String password;

    @NotNull(message = "验证码")
    private String code;

    public String getPersonnelIcCard() {
        return personnelIcCard;
    }

    public void setPersonnelIcCard(String personnelIcCard) {
        this.personnelIcCard = personnelIcCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
