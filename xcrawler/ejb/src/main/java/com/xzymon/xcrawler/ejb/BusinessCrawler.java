package com.xzymon.xcrawler.ejb;

import java.io.InputStream;

import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;
import com.xzymon.xcrawler.model.Resource;
import com.xzymon.xcrawler.model.Run;
import com.xzymon.xcrawler.util.CrawlingPolicy;
import com.xzymon.xcrawler.util.StatusReport;

public interface BusinessCrawler {
	public Run getRun();
	public StatusReport getStatusReport();
	
	Long startCrawling(CrawlingPolicy policy);
	
	InputStream getResourceAsInputStream(String url, long runId);
}
