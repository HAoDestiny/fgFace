package net.sh.rgface.vo.personnel;

import javax.validation.constraints.NotNull;

/**
 * Created by DESTINY on 2018/7/6.
 */
public class RSAVo {

    private String mch_id; //商户id
    private String nonce_str; //随机串

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }
}
