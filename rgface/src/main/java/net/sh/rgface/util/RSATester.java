package net.sh.rgface.util;

import java.util.Map;

/**
 * Created by DESTINY on 2018/7/6.
 */
public class RSATester {


    static String publicKey;
    static String privateKey;

    public static void main(String[] args) throws Exception {
        test();
        testSign();
    }

    static void test() throws Exception {

        try {
            Map<String, Object> keyMap = RSAEncrypt.genKeyPair();
            publicKey = RSAEncrypt.getPublicKey(keyMap);
            privateKey = RSAEncrypt.getPrivateKey(keyMap);
            System.err.println("公钥: \n\r" + publicKey);
            System.err.println("私钥： \n\r" + privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.err.println("公钥加密——私钥解密");
        String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
        System.out.println("\r加密前：\r\n" + source);
        byte[] encodedData = RSAEncrypt.encryptByPublicKey(source.getBytes(), publicKey);
        System.out.println("加密后：\r\n" + Base64Util.encode(encodedData));
        byte[] decodedData = RSAEncrypt.decryptByPrivateKey(encodedData, privateKey);
        System.out.println("解密后: \r\n" + new String(decodedData));

//        System.err.println("私钥加密——公钥解密");
//        String sources = "这是一行测试RSA数字签名的无意义文字";
//        System.out.println("原文字：\r\n" + sources);
//        byte[] encodedDatas = RSAEncrypt.encryptByPrivateKey(source.getBytes(), privateKey);
//        System.out.println("加密后：\r\n" + Base64Util.encode(encodedDatas));
//        byte[] decodedDatas = RSAEncrypt.decryptByPublicKey(encodedDatas, publicKey);
//        String target = Base64Util.encode(decodedDatas);
//        System.out.println("解密后: \r\n" + target);
//
//
//        System.err.println("私钥签名——公钥验证签名");
//        String sign = RSAEncrypt.sign(encodedDatas, privateKey);
//        System.err.println("签名:\r" + sign);
//        boolean status = RSAEncrypt.verify(encodedDatas, publicKey, sign);
//        System.err.println("验证结果:\r" + status);
    }

    static void testSign() throws Exception {

    }


}
