package cn.sinobest.ypgj.util;

import java.util.regex.Pattern;

/**
 * ����JSPҳ���ǩ
 * 
 * @author linmi
 *
 */
public class Html2TxtUtil {

	public Html2TxtUtil() {
		//
	}

    public static String html2txt(String inputString){
        String htmlStr = inputString; //��html��ǩ���ַ���
        String textStr ="";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_ba;
        java.util.regex.Matcher m_ba;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //����script��������ʽ{��<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //����style��������ʽ{��<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //����HTML��ǩ��������ʽ
            String patternStr = "\\s+";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //����script��ǩ

            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //����style��ǩ

            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //����html��ǩ

            p_ba = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
            m_ba = p_ba.matcher(htmlStr);
            //htmlStr = m_ba.replaceAll(""); //���˿ո�

            textStr = htmlStr;

        }catch(Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        return textStr;//�����ı��ַ���
    }
}
