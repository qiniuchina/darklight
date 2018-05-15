package com.dxc.darklight.datasource.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import com.dxc.darklight.conf.ConfFactory;

public class SolrFactory {
	

	public static String SolrCollection = ConfFactory.getConf().get("SolrCollection","http://16.187.41.76:8983/solr/darklight");

	private SolrFactory(){
		
	}
	
	public static SolrClient getClient(){
		HttpSolrClient solrClient = new HttpSolrClient(SolrCollection);
		return solrClient;
	}
}
