package com.xzymon.xcrawler.util;

import java.util.List;

import javax.xml.xpath.XPathExpression;

public class CrawlingPolicyImpl implements CrawlingPolicy {
	private static final long serialVersionUID = -4065534722165403422L;
	/**
	 * URL korzenia z którego rozpoczyna się przetwarzanie - wydobywanie danych
	 */
	private String rootURL;
	/**
	 * limit czasu w milisekundach na pobranie pojedyńczego zasobu sieciowego
	 */
	private Long timeout;
	/**
	 * czy tryb ukrywania jest aktywny
	 * aktywne ukrywanie polega na inicjowaniu pobierania zasobów nie co stały odstęp czasowy ale co zróżnicowany odstęp czasowy
	 */
	private Boolean stealth;
	/**
	 * 
	 */
	private List<String> branchSpec;
	private List<String> leafSpec;
	

	@Override
	public String getRootURL() {
		return rootURL;
	}

	@Override
	public void setRootURL(String url) {
		rootURL = url;
	}

	@Override
	public Long getTriggerTimeout() {
		return timeout;
	}

	@Override
	public void setTriggerTimeout(Long timeout) {
		this.timeout = timeout;
	}

	@Override
	public Boolean isStealthMode() {
		return stealth;
	}

	@Override
	public void setStealthMode(Boolean stealth) {
		this.stealth = stealth;
	}

	@Override
	public List<String> getBranchSpec() {
		return branchSpec;
	}

	@Override
	public void setBranchSpec(List<String> branchSpec) {
		this.branchSpec = branchSpec;
	}

	@Override
	public List<String> getLeafSpec() {
		return leafSpec;
	}

	@Override
	public void setLeafSpec(List<String> leafSpec) {
		this.leafSpec = leafSpec;
	}

	@Override
	public Integer getMaxRetriesPerResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMaxRetriesPerResource(int retrys) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getCssElementsListToRemove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCssElementsListToRemove(List<String> cssList) {
		// TODO Auto-generated method stub
		
	}

}
