package com.xzymon.xcrawler.util;

import java.io.Serializable;
import java.util.Date;

public class InfoHolder implements Serializable{
	private static final long serialVersionUID = 3692068439326149058L;
	
	private Long runId;
	private boolean leaf = false;
	private String url = null;
	private Date created = null;
	private Integer currentRetriesCount;
	
	public Long getRunId() {
		return runId;
	}
	public void setRunId(Long runId) {
		this.runId = runId;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Integer getCurrentRetriesCount() {
		return currentRetriesCount;
	}
	public void setCurrentRetriesCount(Integer currentRetriesCount) {
		this.currentRetriesCount = currentRetriesCount;
	}
}