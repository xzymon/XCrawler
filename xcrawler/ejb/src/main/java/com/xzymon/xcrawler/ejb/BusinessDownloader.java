package com.xzymon.xcrawler.ejb;

import java.io.IOException;

import org.jsoup.nodes.Document;

public interface BusinessDownloader {
	public Document download(String url) throws IOException;
}
