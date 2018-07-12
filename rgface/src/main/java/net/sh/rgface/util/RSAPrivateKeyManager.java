package net.sh.rgface.util;

import java.util.HashMap;

/**
 * Created by DESTINY on 2018/7/6.
 */
public class RSAPrivateKeyManager {

    private static RSAPrivateKeyManager rsaPrivateKeyManager;
    private HashMap<String, String> privateKeyMap;

    public RSAPrivateKeyManager () {
        privateKeyMap = new HashMap<>();
    }

    public static RSAPrivateKeyManager getInstance () {
        if (null == rsaPrivateKeyManager) {
            rsaPrivateKeyManager =  new RSAPrivateKeyManager();
        }
        return rsaPrivateKeyManager;
    }

    public String getPrivateKey(String key) {
        return privateKeyMap.get(key);
    }

    public void setPrivateKey(String key, String rsa) {
        privateKeyMap.put(key, rsa);
    }

    public HashMap<String, String> getPrivateKeyMap() {
        return privateKeyMap;
    }

    public void setPrivateKeyMap(HashMap<String, String> privateKeyMap) {
        this.privateKeyMap = privateKeyMap;
    }
}
