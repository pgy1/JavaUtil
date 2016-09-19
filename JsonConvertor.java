package cn.sinobest.ypgj.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Author : lihaoquan
 * Description :
 */
public class JsonConvertor {


    /**
     * Json字符串转对象
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T Json2Object(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json,clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 对象转Json字符串
     * @param object
     * @return
     */
    public static String Object2Json(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
