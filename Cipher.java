package cn.sinobest.ypgj.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * ���ܽ��ܹ�����
 * Created by chenjianhua on 2015/4/17
 */
public class Cipher {
 
    private static String strDefaultKey = "SINOBEST";
 
    /** ���ܹ��� */
    private javax.crypto.Cipher encryptCipher = null;
 
    /** ���ܹ��� */
    private javax.crypto.Cipher decryptCipher = null;
 
    /**
     * Ĭ�Ϲ��췽����ʹ��Ĭ����Կ
     *
     * @throws Exception
     */
    public Cipher() {
        this(strDefaultKey);
    }
 
    /**
     * ָ����Կ���췽��
     *
     * @param strKey
     *            ָ������Կ
     * @throws Exception
     */
    public Cipher(String strKey) {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());

        try {
            encryptCipher = javax.crypto.Cipher.getInstance("DES");
            encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);

            decryptCipher = javax.crypto.Cipher.getInstance("DES");
            decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }
 
    /**
     * ��byte����ת��Ϊ��ʾ16����ֵ���ַ����� �磺byte[]{8,18}ת��Ϊ��0813�� ��public static byte[]
     * hexStr2ByteArr(String strIn) ��Ϊ�����ת������
     *
     * @param arrB
     *            ��Ҫת����byte����
     * @return ת������ַ���
     * @throws Exception
     *
     */
    public static String byteArr2HexStr(byte[] arrB) {
        int iLen = arrB.length;
        // ÿ��byte�������ַ����ܱ�ʾ�������ַ����ĳ��������鳤�ȵ�����
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // �Ѹ���ת��Ϊ����
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // С��0F������Ҫ��ǰ�油0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }
 
    /**
     * ����ʾ16����ֵ���ַ���ת��Ϊbyte���飬 ��public static String byteArr2HexStr(byte[] arrB)
     * ��Ϊ�����ת������
     *
     * @param strIn ��Ҫת�����ַ���
     * @return ת�����byte����
     * @throws Exception
     *
     */
    public static byte[] hexStr2ByteArr(String strIn) {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;

        // �����ַ���ʾһ���ֽڣ������ֽ����鳤�����ַ������ȳ���2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    public static Cipher newInstance(String strKey){
        return new Cipher(strKey);
    }

    public static Cipher newInstance(){
        return new Cipher();
    }

    /**
     * �����ֽ�����
     *
     * @param arrB
     *            ����ܵ��ֽ�����
     * @return ���ܺ���ֽ�����
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) {
        try {
            return encryptCipher.doFinal(arrB);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * �����ַ���
     *
     * @param strIn
     *            ����ܵ��ַ���
     * @return ���ܺ���ַ���
     * @throws Exception
     */
    public String encrypt(String strIn) {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }
 
    /**
     * �����ֽ�����
     *
     * @param arrB
     *            ����ܵ��ֽ�����
     * @return ���ܺ���ֽ�����
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) {
        try {
            return decryptCipher.doFinal(arrB);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * �����ַ���
     *
     * @param strIn
     *            ����ܵ��ַ���
     * @return ���ܺ���ַ���
     * @throws Exception
     */
    public String decrypt(String strIn) {
        try {
            return new String(decrypt(hexStr2ByteArr(strIn)));
        } catch (Exception e) {
            return "";
        }
    }
 
    /**
     * ��ָ���ַ���������Կ����Կ������ֽ����鳤��Ϊ8λ ����8λʱ���油0������8λֻȡǰ8λ
     *
     * @param arrBTmp
     *            ���ɸ��ַ������ֽ�����
     * @return ���ɵ���Կ
     * @throws java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp) {
        // ����һ���յ�8λ�ֽ����飨Ĭ��ֵΪ0��
        byte[] arrB = new byte[8];
 
        // ��ԭʼ�ֽ�����ת��Ϊ8λ
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
 
        // ������Կ
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
 
        return key;
    }
 
}