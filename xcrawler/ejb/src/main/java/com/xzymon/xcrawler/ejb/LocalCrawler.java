package com.xzymon.xcrawler.ejb;

import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;

public interface LocalCrawler extends BusinessCrawler {
	void crawlBranchXPath(String url, BranchResource resource);
	void crawlLeafXPath(String url, LeafResource resource);
	
	void increaseBranchTriggers();
	void increaseLeafTriggers();
	void increaseBranchDownloaded();
	void increaseLeafDownloaded();
}
