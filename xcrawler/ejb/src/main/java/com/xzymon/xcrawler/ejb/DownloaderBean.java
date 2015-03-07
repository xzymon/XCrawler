package com.xzymon.xcrawler.ejb;

import java.io.IOException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.xzymon.xcrawler.ejb.interceptor.DownloaderInterceptor;

@Stateless
@Local(LocalDownloader.class)
public class DownloaderBean implements BusinessDownloader{

	@Interceptors(DownloaderInterceptor.class)
	@Override
	public Document download(String url) throws IOException {
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")
				.get();
		return doc;
	}

}
