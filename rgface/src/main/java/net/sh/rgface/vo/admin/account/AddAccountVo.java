package net.sh.rgface.vo.admin.account;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/5/2.
 */
public class AddAccountVo {

    private int accountId = 0;

    @NotNull(message = "账号")
    private String accountName;

    @NotNull(message = "密码")
    private String accountPasswordRePass;

    @NotNull(message = "邮箱")
    private String accountEmail;

    @NotNull(message = "简介")
    private String accountIntroduction; //简介

    @NotNull(message = "联系人")
    private String accountContacts;

    @NotNull(message = "联系方式")
    private String accountContactInformation;

    @NotNull(message = "管理级别")
    private int accountType = 0; //0普通用户 1普通管理员 2超级管理员

    private String accountNotes; //备注

    private int type = 2; //1添加管理员 2添加普通用户

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

    public String getAccountPasswordRePass() {
        return accountPasswordRePass;
    }

    public void setAccountPasswordRePass(String accountPasswordRePass) {
        this.accountPasswordRePass = accountPasswordRePass;
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

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountNotes() {
        return accountNotes;
    }

    public void setAccountNotes(String accountNotes) {
        this.accountNotes = accountNotes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
