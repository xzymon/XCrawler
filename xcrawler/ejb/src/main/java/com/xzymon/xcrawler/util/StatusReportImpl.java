package com.xzymon.xcrawler.util;

import java.io.Serializable;

public class StatusReportImpl implements StatusReport, Serializable{

	private static final long serialVersionUID = -494052243532952230L;
	
	private Long milisFromStart;
	private Long branchTriggers;
	private Long leafTriggers;
	private Long branchDownload;
	private Long leafDownload;
	
	@Override
	public Long getMilisFromStart() {
		return milisFromStart;
	}
	
	public void setMilisFromStart(Long milisFromStart) {
		this.milisFromStart = milisFromStart;
	}
	
	@Override
	public Long getBranchResourceDownloadTriggersCount() {
		return branchTriggers;
	}

	public void setBranchResourceDownloadTriggersCount(Long branchTriggers) {
		this.branchTriggers = branchTriggers;
	}

	@Override
	public Long getLeafResourceDownloadTriggersCount() {
		return leafTriggers;
	}

	public void setLeafResourceDownloadTriggersCount(Long leafTriggers) {
		this.leafTriggers = leafTriggers;
	}

	@Override
	public Long getSuccessfullyDownloadedBranchResourcesCount() {
		return this.branchDownload;
	}
	
	public void setSuccessfullyDownloadedBranchResourcesCount(Long branchDownload) {
		this.branchDownload = branchDownload;
	}

	@Override
	public Long getSuccessfullyDownloadedLeafResourcesCount() {
		return this.leafDownload;
	}
	
	public void setSuccessfullyDownloadedLeafResourcesCount(Long leafDownload) {
		this.leafDownload = leafDownload;
	}
}
