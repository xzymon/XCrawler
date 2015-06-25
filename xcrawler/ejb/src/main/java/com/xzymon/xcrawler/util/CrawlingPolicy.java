package com.xzymon.xcrawler.util;


import java.io.Serializable;
import java.util.List;

import javax.xml.xpath.XPathExpression;

public interface CrawlingPolicy extends Serializable{
	String getRootURL();
	void setRootURL(String url);
	Long getTriggerTimeout();
	void setTriggerTimeout(Long timeout);
	Integer getMaxRetriesPerResource();
	void setMaxRetriesPerResource(int retrys);
	Boolean isStealthMode();
	void setStealthMode(Boolean stealth);
	List<String> getBranchSpec();
	void setBranchSpec(List<String> branchSpec);
	List<String> getLeafSpec();
	void setLeafSpec(List<String> leafSpec);
	List<String> getCssElementsListToRemove();
	void setCssElementsListToRemove(List<String> cssList);
}
