package cn.com.choicesoft.util;

import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公用XML解析工具类
 * Created by M.c on 14-5-13.
 */
public class ComXmlUtil<T> {
    /**
     * 公用XNL解析
     *
     * @param cla    解析的实体(实体内的所有字段必须为String类型)
     * @param result
     * @Author:M.c
     */
    public List<T> comXml(Class<T> cla, String result, String docName) {
        List<T> list = null;// return对象
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(result));
            Method[] methods = cla.getMethods();// 获取实体内的所有方法
            String className = cla.getSimpleName();// 实体类名
            int eventType = parser.getEventType();
            list = new ArrayList<T>();// 实例化返回对象
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();// 获取节点Name
                if ((className.equalsIgnoreCase(name) || docName != null && docName.equals(name)) && eventType == XmlPullParser.START_TAG) {// 判断是否符合与实体类符合
                    T o = cla.newInstance();// 实体化实体类
                    for (int j = 0; j < methods.length; j++) {// 循环实体内的方法
                        String metName = methods[j].getName();// 获取方法Name
                        for (int i = 0; i < parser.getAttributeCount(); i++) {// 循环xml内的值
                            String attributeName = "set"
                                    + parser.getAttributeName(i);// 为获取的XML中字段Name加set字符以便于与set方法匹配
                            if (attributeName.equalsIgnoreCase(metName)) {// 判断字段name与实体方法名是否相同
                                Method setMethod = cla.getMethod(metName,
                                        String.class);
                                setMethod
                                        .invoke(o, parser.getAttributeValue(i));
                            }
                        }
                    }
                    list.add(o);
                }
                eventType = parser.next();// 获取下一个节点的值
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公用xml解析 返回List<Map>数组
     *
     * @param result
     * @param docName
     * @return
     */
    public List<Map<String, String>> comMapXml(String result, String docName) {
        List<Map<String, String>> list = null;// return对象
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(result));
            int eventType = parser.getEventType();
            list = new ArrayList<Map<String, String>>();// 实例化返回对象
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();// 获取节点Name
                if (docName != null && docName.equals(name) && eventType == XmlPullParser.START_TAG) {// 判断是否符合与实体类符合
                    Map<String, String> map = new HashMap<String, String>();
                    for (int i = 0; i < parser.getAttributeCount(); i++) {// 循环xml内的值
                        map.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                    }
                    list.add(map);
                }
                eventType = parser.next();// 获取下一个节点的值
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公用xml解析 返回Map
     *
     * @param result
     * @return
     */
    public Map<String, String> xml2Map(String result) {
        Map<String,String> map=null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//创建DOM解析工厂
            DocumentBuilder dombuild = factory.newDocumentBuilder();//创建DON解析器
            Document dom = dombuild.parse(new InputSource(new StringReader(result)));//开始解析XML文档并且得到整个文档的对象模型
            Element root= dom.getDocumentElement();//得到根节点<persons>
            NodeList list=root.getChildNodes();
            map=new HashMap<String, String>();
            for(int i=0;list.getLength()>i;i++){
                Node node=list.item(i);
                map.put(node.getNodeName(),node.getChildNodes().item(0).getNodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
