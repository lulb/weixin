package servlet;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import service.Service;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;


@WebServlet("/wx")
public class Servlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request,
                          javax.servlet.http.HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("utf8");
        response.setCharacterEncoding("utf8");
        //查看微信公众号发送回来的消息
        ServletInputStream is = request.getInputStream();
        byte [] b = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = is.read(b))!= -1){
            sb.append(new String(b,0,len));
        }
        String s = sb.toString();
        System.out.println(s);
        System.out.println("post");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            InputSource a = new InputSource();
            a.setCharacterStream(new StringReader(s));
            try {
                org.w3c.dom.Document doc = db.parse(a);
                String message = doc.getDocumentElement().getTextContent();
                String test = doc.getDocumentElement().getTagName();
                System.out.println(test+message);
            } catch (IOException e) {
                // handle IOException
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e1) {
            // handle ParserConfigurationException
        }
        String responseXml = "<xml><ToUserName><![CDATA[o_tkt1ARaxx-f4_tH_1jGTQb2kJY]]></ToUserName><FromUserName><![CDATA[gh_17d4d5e39137]]></FromUserName><CreateTime>"+ System.currentTimeMillis()/1000+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[你好]]></Content></xml>";
        System.out.println(responseXml);
        PrintWriter out  = response.getWriter();
        out.print(responseXml);
        out.flush();
        out.close();

        //处理消息和事件推送
//        Map<String, String> map = new HashMap<>();
//        InputSource in = new InputSource(new StringReader(s));
//        in.setEncoding("UTF-8");
//        SAXReader reader = new SAXReader();
//        Document document = null;
//        try {
//            document = reader.read(in);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//        Element root = document.getRootElement();
//        List<Element> elements = root.elements();
////        for(Iterator<Element> it = elements.iterator();it.hasNext();){
////            Element element = it.next();
////            System.out.println(element.getName()+" : "+element.getTextTrim());
////        }
//        for(Element e:elements) {
//            map.put(e.getName(), e.getStringValue());
//        }



        System.out.println("post");
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        /**
         * signature	微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
         * timestamp	时间戳
         * nonce	随机数
         * echostr	随机字符串
         * */

        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");


        //校验请求
        if(Service.check(timestamp,nonce,signature)){
            //若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败
            PrintWriter out = response.getWriter();
            out.print(echostr);
            out.flush();
            out.close();
            System.out.println("接入成功");
        }else{
            System.out.println("接入失败");
        }

        System.out.println("get");

    }
}
