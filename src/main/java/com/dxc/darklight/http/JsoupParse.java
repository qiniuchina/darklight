package com.dxc.darklight.http;

import java.io.ByteArrayInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JsoupParse {
	private static final Log log = LogFactory.getLog(JsoupParse.class);
	public static Document getDocument(String url) throws Exception {
		Http http = HttpFactory.getHttpClient();
		// log.debug("http request : " + url);
		FetchResult fr = http.get(url);
		
		Document doc = Jsoup.parse(new ByteArrayInputStream(fr.getContent()), fr.getEncoding(), url);
		return doc;
	}
	/**
	 * 用http请求获得一个连接的文档
	 * @param url
	 * @param charset
	 * @return
	 */
	public static Document getDocument(String url, String charset)throws Exception {
		Http http = HttpFactory.getHttpClient();
		Document doc = null;
		FetchResult fr = http.get(url);
		doc = Jsoup.parse(new ByteArrayInputStream(fr.getContent()), charset, url);
		return doc;
	}

	public static Document getDocument(String url, String charset, long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Http http = HttpFactory.getHttpClient();
		Document doc = null;
		try {
			FetchResult fr = http.get(url);
			int httpCode = fr.getHttpCode();
			if(httpCode == 200){
				doc = Jsoup.parse(new ByteArrayInputStream(fr.getContent()), charset, url);
			}else{
				log.debug("http get " + httpCode + " : "+ url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static Elements getElements(Document doc, String css) {
		if (doc == null)
			return null;
		if (css == null)
			return null;
		return doc.select(css);
	}

	public static Elements getElements(Element element, String css) {
		if (element == null)
			return null;
		if (css == null)
			return null;
		return element.select(css);
	}
	
	/**
	 * 获取一个标签的父标签
	 * @param element
	 * @param css
	 * @param isOwner true 获取该标签下的文本内容，不获取子标签的内容
	 * @return
	 */
	public static String getParentText(Element element, String childElementCss, boolean isOwner) {
	    try {
	        if (element == null)
	            return null;
	        if (childElementCss == null)
	            return null;
	         Element parent = element.select(childElementCss).first().parent();
	        if (parent == null) {
	            return null;
	        } else {
	            if (isOwner) {
	                return parent.ownText();
	            } else {
	                return parent.text();
	            }
	        }
        }
        catch (Exception e) {
            return null;
        }
    }

	public static Element getElement(Element element, String css) {
		if (element == null)
			return null;
		if (css == null)
			return null;
		return element.select(css) != null ? element.select(css).first() : null;
	}

	public static Element getElement(Document doc, String css) {
		if (doc == null)
			return null;
		if (css == null)
			return null;
		return doc.select(css) != null ? doc.select(css).first() : null;
	}

	public static String getText(Element element) {
		if (element == null)
			return null;
		return element.text();
	}

	public static String getText(Element element, String css) {
		if (element == null)
			return null;
		if (css == null)
			return null;
		return getText(getElement(element, css));
	}

	public static String getText(Document doc, String css) {
		return getText(getElement(doc, css));
	}

	public static String getAttr(Element element, String css, String key) {
		if (element == null)
			return null;
		if (css == null)
			return null;
		Element newElement = getElement(element, css);
		if (newElement == null)
			return null;
		return newElement.attr(key);
	}

	public static String getAttr(Element element, String key) {
		if (element == null)
			return null;
		if (key == null)
			return null;
		return element.attr(key);
	}
	
	public static String getOwnText(Document document,String css) {
		if (document == null)
			return null;
		if (css == null)
			return null;
		return getOwnText(getElement(document, css));
	}
	
	public static String getOwnText(Element element) {
		if (element == null)
			return null;
		return element.ownText();
	}
	
	public static String getOwnText(Element element,String css) {
		if (element == null)
			return null;
		if (css == null)
			return null;
		return getOwnText(element.select(css) == null ? null : element.select(css).first());
	}
	
	public static String getData(Document document,String css){
		if (document == null)
			return null;
		if (css == null)
			return null;
		Element element = getElement(document, css);
		if(element == null)
			return null;
		return element.data();
	}
	
	public static void showElements(Elements elements) {
		if (elements != null && !elements.isEmpty()) {
			for (Element ele : elements) {
				System.out.println(ele.html());
				System.out.println("==============================================");
			}
		}
	}
}
