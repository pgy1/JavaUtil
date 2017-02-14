package com.springapp.util;

import com.springapp.domain.Product;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by pengguangyu on 2017/2/13.
 * JAXB�Ķ���ת������
 */
public class JaxbObjectAndXmlUtil
{

    /**
     * @param xmlStr �ַ���
     * @param c ����Class����
     * @return ����ʵ��
     */
    @SuppressWarnings("unchecked")
    public static <T> T xml2Object(String xmlStr,Class<T> c)
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(c);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            T t = (T) unmarshaller.unmarshal(new StringReader(xmlStr));

            return t;

        } catch (JAXBException e) {  e.printStackTrace();  return null; }

    }

    /**
     * @param object ����
     * @return ����xmlStr
     */
    public static String object2Xml(Object object)
    {
        try
        {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshal = context.createMarshaller();

            marshal.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // ��ʽ�����
            marshal.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// �����ʽ,Ĭ��Ϊutf-8
            marshal.setProperty(Marshaller.JAXB_FRAGMENT, false);// �Ƿ�ʡ��xmlͷ��Ϣ
            marshal.setProperty("jaxb.encoding", "utf-8");
            marshal.marshal(object,writer);

            return new String(writer.getBuffer());

        } catch (Exception e) { e.printStackTrace(); return null;}

    }

    public static void main(String[] args)
    {
        Product product = new Product();
        product.setName("pgy����");//��������

        String xmlStr = JaxbObjectAndXmlUtil.object2Xml(product);//���챨�� XML ��ʽ���ַ���
        System.out.println("����תxml���ģ� \n"+xmlStr);

        Product msgBean2 = JaxbObjectAndXmlUtil.xml2Object(xmlStr, Product.class);
        System.out.println("����תxmlת�� \n"+ msgBean2);
    }
}
