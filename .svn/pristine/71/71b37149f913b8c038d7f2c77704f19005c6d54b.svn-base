/**
 * All rights Reserved
 * @Title: 	BadNewsFilterUtil.java 
 * @Package com.dxc.darklight.util 
 * @Description: 	TODO
 * @author:	 wwei4  
 * @date:	Mar 16, 2017 3:35:09 PM 
 * @version	V1.0   
 */
package com.dxc.darklight.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dxc.darklight.conf.ConfFactory;
import com.hankcs.hanlp.algoritm.ahocorasick.trie.Token;
import com.hankcs.hanlp.algoritm.ahocorasick.trie.Trie;


/** 
 * @ClassName:	BadNewsFilterUtil 
 * @Description: 基于HanLP（http://hanlp.linrunsoft.com/）分词提供的AhoCorasick算法+Trie树实现的关键词过滤算法，判断一条新闻是否属于利空新闻。 
 * @author:	wwei4
 * @date:	Mar 16, 2017 3:35:09 PM 
 *  
 */
public class BadNewsFilterUtil {
	
	private static BadNewsFilterUtil instance = new BadNewsFilterUtil();
	
	private static final Logger log = LogManager.getLogger(BadNewsFilterUtil.class);
	
	/** 利空关键词文件 */
	private static final String FILE_BADNEWS_KEYWORDS = "stockstopwords.txt";
	/** AhoCorasick算法+Trie树实现的关键词过滤算法类，来自HanLP开源分词项目 */
	private  static Trie trie;
	
	static{
		trie = new Trie();
		String filename = ConfFactory.CONF_HOME+ File.separator + FILE_BADNEWS_KEYWORDS;
		try {
			log.info(filename);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(filename), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String line = br.readLine();
			while (null != line && !"".equals(line)) {
				if(!line.startsWith("#")) {
					//添加全局关键词
					trie.addKeyword(line);
				}
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static BadNewsFilterUtil getInstance() {
		return instance;
	}
	/**
	 * 根据输入的字符串，判断这条新闻是否是利空新闻
	 * @param text 一般是新闻标题
	 * @return 利空关键字
	 */
	public String isBadNews(String text) {
		Collection<Token> tokens = trie.tokenize(text);
		String keyWords = null;
		Set<String> set = new HashSet<String>();
		//过滤重复词
		for (Token token : tokens) {
			if(token.isMatch()) set.add(token.getFragment());
		}
		for (String key : set) {
			log.info("badnews keyword : " + key);
			if(keyWords!=null&&!"".equals(keyWords)&&keyWords.length()>0){
				keyWords += "," + key;
			} else {
				keyWords = key;
			}
			
		}
		return keyWords;
	}
}
