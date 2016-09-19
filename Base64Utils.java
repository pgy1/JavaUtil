package cn.sinobest.ypgj.util;

/**
 * Base64 π§æﬂ¿‡
 * @author chenjianhua
 */
public class Base64Utils {
    private static final char[] intToBase64 = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final char[] intToAltBase64 = new char[]{'!', '\"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?'};
    private static final byte[] base64ToInt = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)62, (byte)-1, (byte)-1, (byte)-1, (byte)63, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)9, (byte)10, (byte)11, (byte)12, (byte)13, (byte)14, (byte)15, (byte)16, (byte)17, (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51};
    private static final byte[] altBase64ToInt = new byte[]{(byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8, (byte)-1, (byte)62, (byte)9, (byte)10, (byte)11, (byte)-1, (byte)52, (byte)53, (byte)54, (byte)55, (byte)56, (byte)57, (byte)58, (byte)59, (byte)60, (byte)61, (byte)12, (byte)13, (byte)14, (byte)-1, (byte)15, (byte)63, (byte)16, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)-1, (byte)17, (byte)-1, (byte)18, (byte)19, (byte)21, (byte)20, (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)32, (byte)33, (byte)34, (byte)35, (byte)36, (byte)37, (byte)38, (byte)39, (byte)40, (byte)41, (byte)42, (byte)43, (byte)44, (byte)45, (byte)46, (byte)47, (byte)48, (byte)49, (byte)50, (byte)51, (byte)22, (byte)23, (byte)24, (byte)25};

    public Base64Utils() {
    }

    public static String byteArrayToBase64(byte[] a) {
        return byteArrayToBase64(a, false);
    }

    public static String byteArrayToAltBase64(byte[] a) {
        return byteArrayToBase64(a, true);
    }

    private static String byteArrayToBase64(byte[] a, boolean alternate) {
        int aLen = a.length;
        int numFullGroups = aLen / 3;
        int numBytesInPartialGroup = aLen - 3 * numFullGroups;
        int resultLen = 4 * ((aLen + 2) / 3);
        StringBuilder result = new StringBuilder(resultLen);
        char[] intToAlpha = alternate?intToAltBase64:intToBase64;
        int inCursor = 0;

        int byte0;
        int byte1;
        for(byte0 = 0; byte0 < numFullGroups; ++byte0) {
            byte1 = a[inCursor++] & 255;
            int byte11 = a[inCursor++] & 255;
            int byte2 = a[inCursor++] & 255;
            result.append(intToAlpha[byte1 >> 2]);
            result.append(intToAlpha[byte1 << 4 & 63 | byte11 >> 4]);
            result.append(intToAlpha[byte11 << 2 & 63 | byte2 >> 6]);
            result.append(intToAlpha[byte2 & 63]);
        }

        if(numBytesInPartialGroup != 0) {
            byte0 = a[inCursor++] & 255;
            result.append(intToAlpha[byte0 >> 2]);
            if(numBytesInPartialGroup == 1) {
                result.append(intToAlpha[byte0 << 4 & 63]);
                result.append("==");
            } else {
                byte1 = a[inCursor++] & 255;
                result.append(intToAlpha[byte0 << 4 & 63 | byte1 >> 4]);
                result.append(intToAlpha[byte1 << 2 & 63]);
                result.append('=');
            }
        }

        return result.toString();
    }

    public static byte[] base64ToByteArray(String s) {
        return base64ToByteArray(s, false);
    }

    public static byte[] altBase64ToByteArray(String s) {
        return base64ToByteArray(s, true);
    }

    private static byte[] base64ToByteArray(String s, boolean alternate) {
        byte[] alphaToInt = alternate?altBase64ToInt:base64ToInt;
        int sLen = s.length();
        int numGroups = sLen / 4;
        if(4 * numGroups != sLen) {
            throw new IllegalArgumentException("String length must be a multiple of four.");
        } else {
            int missingBytesInLastGroup = 0;
            int numFullGroups = numGroups;
            if(sLen != 0) {
                if(s.charAt(sLen - 1) == 61) {
                    ++missingBytesInLastGroup;
                    numFullGroups = numGroups - 1;
                }

                if(s.charAt(sLen - 2) == 61) {
                    ++missingBytesInLastGroup;
                }
            }

            byte[] result = new byte[3 * numGroups - missingBytesInLastGroup];
            int inCursor = 0;
            int outCursor = 0;

            int ch0;
            int ch1;
            int ch2;
            for(ch0 = 0; ch0 < numFullGroups; ++ch0) {
                ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
                ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
                int ch21 = base64toInt(s.charAt(inCursor++), alphaToInt);
                int ch3 = base64toInt(s.charAt(inCursor++), alphaToInt);
                result[outCursor++] = (byte)(ch1 << 2 | ch2 >> 4);
                result[outCursor++] = (byte)(ch2 << 4 | ch21 >> 2);
                result[outCursor++] = (byte)(ch21 << 6 | ch3);
            }

            if(missingBytesInLastGroup != 0) {
                ch0 = base64toInt(s.charAt(inCursor++), alphaToInt);
                ch1 = base64toInt(s.charAt(inCursor++), alphaToInt);
                result[outCursor++] = (byte)(ch0 << 2 | ch1 >> 4);
                if(missingBytesInLastGroup == 1) {
                    ch2 = base64toInt(s.charAt(inCursor++), alphaToInt);
                    result[outCursor++] = (byte)(ch1 << 4 | ch2 >> 2);
                }
            }

            return result;
        }
    }

    private static int base64toInt(char c, byte[] alphaToInt) {
        byte result = alphaToInt[c];
        if(result < 0) {
            throw new IllegalArgumentException("Illegal character " + c);
        } else {
            return result;
        }
    }
}
