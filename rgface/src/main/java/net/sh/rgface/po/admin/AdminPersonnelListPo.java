package net.sh.rgface.po.admin;

import java.util.Map;
import java.util.Objects;

/**
 * Created by DESTINY on 2018/5/10.
 */
public class AdminPersonnelListPo {

    private Object personnelPo; //人员
    private Object accountInfoPo; //所属账户
    private Map<String, Object> PtoPo; //头像信息

    public Object getPersonnelPo() {
        return personnelPo;
    }

    public void setPersonnelPo(Object personnelPo) {
        this.personnelPo = personnelPo;
    }

    public Object getAccountInfoPo() {
        return accountInfoPo;
    }

    public void setAccountInfoPo(Object accountInfoPo) {
        this.accountInfoPo = accountInfoPo;
    }

    public Map<String, Object> getPtoPo() {
        return PtoPo;
    }

    public void setPtoPo(Map<String, Object> ptoPo) {
        PtoPo = ptoPo;
    }
}
