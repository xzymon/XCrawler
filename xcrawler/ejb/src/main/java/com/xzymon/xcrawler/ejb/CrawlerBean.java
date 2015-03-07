package com.xzymon.xcrawler.ejb;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;

import org.jsoup.nodes.Document;

@Stateful
@Remote(RemoteCrawler.class)
public class CrawlerBean implements BusinessCrawler {

	@EJB
	private LocalDownloader downloader;
	
	@Override
	public String getResource(String url) {
		String result = null;
		try{
			Document doc = downloader.download(url);
			result = doc.html();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
		}
		return result;
	}
	
}
