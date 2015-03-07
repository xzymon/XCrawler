package com.xzymon.xcrawler.client;

import javax.naming.Context;
import javax.naming.NamingException;

import com.xzymon.xcrawler.client.util.RemoteBeansUtil;
import com.xzymon.xcrawler.ejb.CrawlerBean;
import com.xzymon.xcrawler.ejb.RemoteCrawler;

public class CrawlerCommunicator {
	
	private Context context;
	
	private RemoteCrawler crawlerStub;
	
	public String crawl(String url){
		String result = null;
		if(crawlerStub==null){
			try {
				crawlerStub = lookupRemoteCrawler();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(crawlerStub!=null){
			result = crawlerStub.getResource(url);
		}
		return result;
	}
	
	public RemoteCrawler lookupRemoteCrawler() throws NamingException{
		context = RemoteBeansUtil.getInvocationContext();
		String appName = "xcrawler";
		String moduleName = "xcrawler-ejb";
		String distinctName = "";
		String beanName = CrawlerBean.class.getSimpleName();
		String interfaceName = RemoteCrawler.class.getName();
		String lookupName = String.format("ejb:%1$s/%2$s/%3$s/%4$s!%5$s?stateful",
				appName, moduleName, distinctName, beanName, interfaceName);
		return (RemoteCrawler) context.lookup(lookupName);
	}
}
