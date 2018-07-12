package net.sh.rgface.vo.admin.account;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/4.
 */
public class AdminUpdateAccountInfoVo {

    private int accountId = 0;

    @NotNull(message = "账号")
    private String accountName;

//    @NotNull(message = "密码")
//    private String accountPasswordRePass;

    @NotNull(message = "邮箱")
    private String accountEmail;

    @NotNull(message = "简介")
    private String accountIntroduction; //简介

    @NotNull(message = "联系人")
    private String accountContacts;

    @NotNull(message = "联系方式")
    private String accountContactInformation;

//    private int accountType = -1;

    private String accountNotes; //备注

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountIntroduction() {
        return accountIntroduction;
    }

    public void setAccountIntroduction(String accountIntroduction) {
        this.accountIntroduction = accountIntroduction;
    }

    public String getAccountContacts() {
        return accountContacts;
    }

    public void setAccountContacts(String accountContacts) {
        this.accountContacts = accountContacts;
    }

    public String getAccountContactInformation() {
        return accountContactInformation;
    }

    public void setAccountContactInformation(String accountContactInformation) {
        this.accountContactInformation = accountContactInformation;
    }

    public String getAccountNotes() {
        return accountNotes;
    }

    public void setAccountNotes(String accountNotes) {
        this.accountNotes = accountNotes;
    }
}
