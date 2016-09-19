package cn.sinobest.ypgj.util;

import java.util.*;

/**
 * �����ж�
 * @author hexiaowei
 *
 */
public class ObjectUtil {

    /**
     * �ж϶����Ƿ�Empty(null��Ԫ��Ϊ0)<br>
     * ʵ���ڶ����¶������ж�:String Collection�������� Map��������
     *
     * @param pObj
     *            ��������
     * @return boolean ���صĲ���ֵ
     */
    public static boolean isEmpty(Object pObj) {
        if (pObj == null)
            return true;
        if (pObj == "")
            return true;
        if (pObj instanceof String) {
            if (((String) pObj).length() == 0) {
                return true;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return true;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * �ж϶����Ƿ�ΪNotEmpty(!null��Ԫ��>0)<br>
     * ʵ���ڶ����¶������ж�:String Collection�������� Map��������
     *
     * @param pObj
     *            ��������
     * @return boolean ���صĲ���ֵ
     */
    public static boolean isNotEmpty(Object pObj) {
        if (pObj == null)
            return false;
        if (pObj == "")
            return false;
        if (pObj instanceof String) {
            if (((String) pObj).length() == 0) {
                return false;
            }
            if(((String) pObj).equalsIgnoreCase("null")) {
                return false;
            }
        } else if (pObj instanceof Collection) {
            if (((Collection) pObj).size() == 0) {
                return false;
            }
        } else if (pObj instanceof Map) {
            if (((Map) pObj).size() == 0) {
                return false;
            }
        }
        return true;
    }

}
