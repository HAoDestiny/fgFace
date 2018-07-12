package net.sh.rgface.entity.session;

/**
 * Created by DESTINY on 2018/5/14.
 */

public class PersonnelSession {

    private int personnelId;
    private String personnelName;
    private String icCard;

    public int getPersonnelId() {
        return personnelId;
    }

    public void setPersonnelId(int personnelId) {
        this.personnelId = personnelId;
    }

    public String getPersonnelName() {
        return personnelName;
    }

    public void setPersonnelName(String personnelName) {
        this.personnelName = personnelName;
    }

    public String getIcCard() {
        return icCard;
    }

    public void setIcCard(String icCard) {
        this.icCard = icCard;
    }
}
