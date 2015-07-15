package com.xzymon.xcrawler.ejb;

import java.io.InputStream;

import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;
import com.xzymon.xcrawler.model.Run;
import com.xzymon.xcrawler.util.CrawlingPolicy;
import com.xzymon.xcrawler.util.StatusReport;

public interface BusinessCrawler {
	public Run getRun();
	public StatusReport getStatusReport();
	
	Long startCrawling(CrawlingPolicy policy);
	
	InputStream getResourceAsInputStream(String url, long runId);
	
	CrawlingPolicy getPolicy();
	
	void increaseBranchTriggers();
	void increaseLeafTriggers();
	void increaseBranchDownloaded();
	void increaseLeafDownloaded();
	
	void crawlBranchXPath(String url, BranchResource resource);
	void crawlLeafXPath(String url, LeafResource resource);
	
	Long getBeanNo();
}
