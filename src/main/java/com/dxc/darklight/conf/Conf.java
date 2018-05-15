package com.dxc.darklight.conf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * 配置文件的基本类
 * 
 * 
 */
public class Conf {
	protected Log LOG = LogFactory.getLog(this.getClass());

	private Map<String, String> properties = new ConcurrentHashMap<String, String>();

	private HashSet<String> confFileSet = new HashSet<String>();

	private ClassLoader classLoader;
	{
		classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = Conf.class.getClassLoader();
		}
	}

	public int getInt(String key) {
		return Integer.parseInt(properties.get(key));
	}

	public int getInt(String key, int defaultValue) {
		if (!properties.containsKey(key))
			return defaultValue;

		try {
			return Integer.parseInt(properties.get(key));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public long getLong(String key) {
		return Long.parseLong(properties.get(key));
	}

	public long getLong(String key, long defaultValue) {
		if (!properties.containsKey(key))
			return defaultValue;

		try {
			return Long.parseLong(properties.get(key));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public float getFloat(String key) {
		return Float.parseFloat(properties.get(key));
	}

	public float getFloat(String key, float defaultValue) {
		if (!properties.containsKey(key))
			return defaultValue;

		try {
			return Float.parseFloat(properties.get(key));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public String get(String key) {
		return properties.get(key);
	}

	public String get(String key, String defaultValue) {
		if (properties.containsKey(key)) {
			return properties.get(key);
		} else {
			return defaultValue;
		}
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(properties.get(key));
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (properties.containsKey(key)) {
			return Boolean.parseBoolean(properties.get(key));
		} else {
			return defaultValue;
		}
	}

	public Set<String> getConfFileSet() {
		return confFileSet;
	}

	public boolean containsConfFile(File confFile) {
		return confFileSet.contains(confFile.getAbsolutePath());
	}

	public void loadResource(File configFile) {
		loadResource(configFile, true);
	}

	public void loadResource(File configFile, boolean quiet) {
		if (configFile.exists()) {
			LOG.debug("loading config from " + configFile.getAbsolutePath());
		} else {
			LOG.warn("config file " + configFile.getAbsolutePath() + " does not exist.");
		}

		if (configFile == null || !configFile.exists()) {
			if (quiet)
				return;
			throw new RuntimeException(configFile.getAbsolutePath() + " not found");
		}

		if (!confFileSet.contains(configFile)) {
			confFileSet.add(configFile.getAbsolutePath());
		}

		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			// ignore all comments inside the xml file
			docBuilderFactory.setIgnoringComments(true);
			DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
			Document doc = builder.parse(configFile);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			String xpathString = "//configuration";
			XPathExpression expression = xpath.compile(xpathString.trim());
			Node confRoot = (Node) expression.evaluate(doc, XPathConstants.NODE);

			if (confRoot == null) {
				LOG.fatal("not a conf file: there's no element <configuration>");
				return;
			}

			NodeList props = confRoot.getChildNodes();
			for (int i = 0; i < props.getLength(); i++) {
				Node propNode = props.item(i);
				if (!(propNode instanceof Element))
					continue;
				Element prop = (Element) propNode;
				if (!"property".equals(prop.getTagName()))
					LOG.warn("bad conf file: element not <property>");
				NodeList fields = prop.getChildNodes();
				String attr = null;
				String value = null;
				for (int j = 0; j < fields.getLength(); j++) {
					Node fieldNode = fields.item(j);
					if (!(fieldNode instanceof Element))
						continue;
					Element field = (Element) fieldNode;
					if ("name".equals(field.getTagName()) && field.hasChildNodes()) {
						attr = ((Text) field.getFirstChild()).getData().trim();
					}
					if ("value".equals(field.getTagName()) && field.hasChildNodes()) {
						value = ((Text) field.getFirstChild()).getData();
					}
				}

				if (attr != null && value != null) {
					properties.put(attr, value);
				}
			}

		} catch (Exception e) {
			LOG.fatal("error parsing conf file: " + e);
			throw new RuntimeException(e);
		}
	}

	public void write(OutputStream out) throws IOException {

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element conf = doc.createElement("configuration");
			doc.appendChild(conf);
			conf.appendChild(doc.createTextNode("\n"));
			for (String name : properties.keySet()) {
				String value = properties.get(name);

				Element propNode = doc.createElement("property");
				conf.appendChild(propNode);

				Element nameNode = doc.createElement("name");
				nameNode.appendChild(doc.createTextNode(name));
				propNode.appendChild(nameNode);

				Element valueNode = doc.createElement("value");
				valueNode.appendChild(doc.createTextNode(value));
				propNode.appendChild(valueNode);

				conf.appendChild(doc.createTextNode("\n"));
			}

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(out);
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setParameter(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
