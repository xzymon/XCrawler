package com.xzymon.xcrawler.ejb;

import java.io.IOException;

import org.jsoup.nodes.Document;

public interface BusinessDownloader {
	public Document download(String url) throws IOException;
	
	void registerBranchToDownload(String url, int currentRetriesCount, long delayDuration);
	
	void registerLeafToDownload(String url, int currentRetriesCount, long delayDuration);
	
	void download(javax.ejb.Timer timer);
}
