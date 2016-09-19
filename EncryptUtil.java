package cn.sinobest.ypgj.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Author : lihaoquan
 * Description : 加密解密工具类
 */
public class EncryptUtil {

    public static String getMD5(String str) {
        return encode(str, "MD5");
    }

    public static String getSHA1(String str) {
        return encode(str, "SHA-1");
    }

    public static String getLittleMD5(String str) {
        String estr = encode(str, "MD5");
        return estr.substring(0, 20);
    }

    public static String getLittleSHA1(String str) {
        String estr = encode(str, "SHA-1");
        return estr.substring(0, 20);
    }

    private static String encode(String str, String type) {
        try {
            MessageDigest alga = java.security.MessageDigest.getInstance(type);
            alga.update(str.getBytes("UTF-8"));
            byte[] digesta = alga.digest();
            return byte2hex(digesta);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 异或加密
     *
     * @param str
     * @param key
     * @return
     */
    public static String xorEncrypt(String str, String key) {
        BigInteger strbi = new BigInteger(str.getBytes());
        BigInteger keybi = new BigInteger(key.getBytes());
        BigInteger encryptbi = strbi.xor(keybi);

        return new String(encryptbi.toByteArray());
    }

    /**
     * 异或解密
     *
     * @param encryptStr
     * @param key
     * @return
     */
    public static String xorDecrypt(String encryptStr, String key) {
        BigInteger encryptbi = new BigInteger(encryptStr.getBytes());
        BigInteger keybi = new BigInteger(key.getBytes());
        BigInteger decryptbi = encryptbi.xor(keybi);
        return new String(decryptbi.toByteArray());
    }

}
