package net.sh.rgface.entity.session;

/**
 * Created by DESTINY on 2018/5/5.
 */
public class AdminSession {

    private int id;
    private int accountType;
    private String accountName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }
}
