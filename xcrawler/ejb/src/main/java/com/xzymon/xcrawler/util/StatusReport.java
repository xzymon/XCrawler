package com.xzymon.xcrawler.util;

public interface StatusReport {
	Long getMilisFromStart();
	Long getBranchResourceDownloadTriggersCount();
	Long getLeafResourceDownloadTriggersCount();
	Long getSuccessfullyDownloadedBranchResourcesCount();
	Long getSuccessfullyDownloadedLeafResourcesCount();
}
