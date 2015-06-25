package com.xzymon.xcrawler.util;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.Resource;
import com.xzymon.xpath_searcher.core.listener.XPathSearchingListener;

public class ResourcesSearchListener implements XPathSearchingListener {
	private static final Logger logger = LoggerFactory.getLogger(ResourcesSearchListener.class.getName());
	
	private Set<String> urls;
	
	public ResourcesSearchListener(Set<String> urls) {
		this.urls = urls;
	}
	
	@Override
	public void nextNode(Node node, String expression, int nodeId) {
		String url = node.getNodeValue();
		logger.info(String.format("nextNode - expression: %1$s, id: %2$d, value: %3$s", expression, nodeId, url));
		urls.add(url);
	}

	@Override
	public void nodesExhausted(String expression) {
		logger.info(String.format("nodesExhausted(%1$s)", expression));
	}

	@Override
	public void foundNodesCount(String expression, int count) {
		logger.info(String.format("foundNodesCount(%1$s, %2$d)", expression, count));
	}

	@Override
	public void stateReset(String expression) {
		logger.info(String.format("stateReset(%1$s)", expression));
	}

	@Override
	public void stateClear() {
		logger.info("stateClear()");
	}

}
