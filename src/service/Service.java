package service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Service {
    private static final String TOKEN = "lulb";

    /**
     *
     * 验证签名
     */
    public static boolean check(String timestamp,String nonce,String signature){

        /**
         * 1）将token、timestamp、nonce三个参数进行字典序排序
         * 2）将三个参数字符串拼接成一个字符串进行sha1加密
         * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         */

        //1）将token、timestamp、nonce三个参数进行字典序排序
        String[] strs = new String[] {TOKEN,timestamp,nonce};
        Arrays.sort(strs);
        //2）将三个参数字符串拼接成一个字符串进行sha1加密
        String str = strs[0]+strs[1]+strs[2];
        String mysig = sha1(str);
        //打印两个字符串
//        System.out.println(mysig);
//        System.out.println(signature);

        //3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        return mysig.equalsIgnoreCase(signature);
    }

    /**
     * 进行sha1算法加密
     *
     * */
    private static String sha1(String src) {

        try {
            //获取一个加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest =  md.digest(src.getBytes());
            char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
            StringBuilder sb = new StringBuilder();
            //处理加密结果
            for(byte b:digest){
                sb.append(chars[(b>>4)&15]);
                sb.append(chars[(b&15)]);
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用来解析xml数据包需要导入dom4j的jar
     * */
    public static Map<String, String> parseRequest(InputStream is) {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            //读取输入流，获取文档对象
            Document document = reader.read(is);
            //根据文档对象获取根节点
            Element root = document.getRootElement();
            //获取根节点的所有的子节点
            List<Element> elements = root.elements();
            for(Element e:elements) {
                map.put(e.getName(), e.getStringValue());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

//    public static Map<String,String> parseRequseet(HttpServletRequest request){
//
//        Map<String,String> messageMap=new HashMap<String, String>();
//        InputStream inputStream=null;
//        try {
//            //读取request Stream信息
//            inputStream=request.getInputStream();
//        } catch (IOException e) {
//            e.printStackTrace();
//            }
//
//        SAXReader reader = new SAXReader();
//        Document document=null;
//        try {
//            document = reader.read(inputStream);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//        Element root=document.getRootElement();
//        List<Element> elementsList=root.elements();
//        for(Element e:elementsList){
//            messageMap.put(e.getName(),e.getText());
//        }
//        try {
//            inputStream.close();
//            inputStream=null;
//            } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        return messageMap;
//    }

//    public static Map<String,String> parseRequseet(InputStream is) {
//
//        Map<String, String> map = new HashMap<>();
//        SAXReader reader = new SAXReader();
//        try {
//            //读取输入流，获取文档对象
//            Document document = reader.read(is);
//            //根据文档对象获取根节点
//            Element root = document.getRootElement();
//            //获取根节点的所以子节点
//            List <Element> elements = root.elements();
//            for(Element e:elements){
//                map.put(e.getName(),e.getStringValue());
//
//            }
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//
//        return map;
//    }

}
