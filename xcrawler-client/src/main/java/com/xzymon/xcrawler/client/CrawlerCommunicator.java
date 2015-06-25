package com.xzymon.xcrawler.client;

import java.io.InputStream;

import javax.naming.Context;
import javax.naming.NamingException;

import com.xzymon.xcrawler.client.util.RemoteBeansUtil;
import com.xzymon.xcrawler.ejb.CrawlerBean;
import com.xzymon.xcrawler.ejb.RemoteCrawler;
import com.xzymon.xcrawler.util.CrawlingPolicy;
import com.xzymon.xcrawler.util.StatusReport;

public class CrawlerCommunicator {
	
	private Context context;
	
	private RemoteCrawler crawlerStub;
	
	public CrawlerCommunicator() throws NamingException {
		//init();
	}
	
	private void init() throws NamingException{
		crawlerStub = this.lookupRemoteCrawler();
	}
	
	public Long crawl(CrawlingPolicy policy){
		Long result = null;
		if(crawlerStub==null){
			try {
				crawlerStub = lookupRemoteCrawler();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(crawlerStub!=null){
			result = crawlerStub.startCrawling(policy);
		}
		return result;
	}
	
	public InputStream getResource(String url, Long runId){
		InputStream is = null;
		if(crawlerStub==null){
			try {
				crawlerStub = lookupRemoteCrawler();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(crawlerStub!=null){
			is = crawlerStub.getResourceAsInputStream(url, runId);
		}
		return is;
	}
	
	public StatusReport getStatusReport(){
		StatusReport result = null;
		if(crawlerStub==null){
			try {
				crawlerStub = lookupRemoteCrawler();
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(crawlerStub!=null){
			result = crawlerStub.getStatusReport();
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
