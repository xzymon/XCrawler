package com.xzymon.xcrawler.ejb;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.interceptor.Interceptors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.ejb.interceptor.DownloaderInterceptor;
import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;
import com.xzymon.xcrawler.util.InfoHolder;

@Stateless
@Local(LocalDownloader.class)
public class DownloaderBean implements BusinessDownloader{
	private static final Logger logger = LoggerFactory.getLogger(DownloaderBean.class.getName());
	
	@EJB
	private LocalIndexer indexer;
	
	@Resource
	SessionContext ctx;
	
	@Resource
	private TimerService timerService;

	@Interceptors(DownloaderInterceptor.class)
	@Override
	public Document download(String url) throws IOException {
		Document doc = Jsoup.connect(url)
				.userAgent("Mozilla")
				.get();
		return doc;
	}

	@Interceptors(DownloaderInterceptor.class)
	@Override
	public void registerBranchToDownload(String url, int currentRetriesCount, long delayDuration, BusinessCrawler crawler) {
		String runKey = generateKey(crawler.getRun().getId());
		ctx.getContextData().put(runKey, crawler);
		InfoHolder ifh = new InfoHolder();
		ifh.setRunId(crawler.getRun().getId());
		ifh.setLeaf(false);
		ifh.setUrl(url);
		ifh.setCurrentRetriesCount(currentRetriesCount);
		TimerConfig tcfg = new TimerConfig();
		tcfg.setInfo(ifh);
		timerService.createSingleActionTimer(delayDuration, tcfg);
	}
	
	@Interceptors(DownloaderInterceptor.class)
	@Override
	public void registerLeafToDownload(String url, int currentRetriesCount, long delayDuration, BusinessCrawler crawler) {
		String runKey = generateKey(crawler.getRun().getId());
		ctx.getContextData().put(runKey, crawler);
		InfoHolder ifh = new InfoHolder();
		ifh.setRunId(crawler.getRun().getId());
		ifh.setLeaf(true);
		ifh.setUrl(url);
		ifh.setCurrentRetriesCount(currentRetriesCount);
		TimerConfig tcfg = new TimerConfig();
		tcfg.setInfo(ifh);
		timerService.createSingleActionTimer(delayDuration, tcfg);
	}

	@Interceptors(DownloaderInterceptor.class)
	@Timeout
	@Override
	public void download(Timer timer) {
		InfoHolder ifh = (InfoHolder) timer.getInfo();
		
		String url = ifh.getUrl();
		Integer retries = ifh.getCurrentRetriesCount();
		
		byte[] data = null;
		try {
			Document doc = Jsoup.connect(url)
					.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:38.0) Gecko/20100101 Firefox/38.0")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Language", "pl,en-US;q=0.7,en;q=0.3")
					.header("Accept-Encoding", "gzip, deflate")
					.get();
			data = doc.html().getBytes();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO here
		
		BusinessCrawler crawler = (BusinessCrawler) ctx.getContextData().get(generateKey(ifh.getRunId()));
		if(ifh.isLeaf()){
			LeafResource lr = null;
			if(data!=null){
				lr = new LeafResource();
				lr.setCreated(ifh.getCreated());
				lr.setDownloaded(new Date());
				lr.setData(data);
				lr.setUrl(url);
			}
			logger.info("Passing Crawler with beanNo=" + crawler.getBeanNo() + "to indexer.receiveLeafResource()");
			indexer.receiveLeafResource(url, retries, lr, crawler);
		} else {
			BranchResource br = null;
			if(data!=null){
				br = new BranchResource();
				br.setCreated(ifh.getCreated());
				br.setDownloaded(new Date());
				br.setData(data);
				br.setUrl(url);
			}
			logger.info("Passing Crawler with beanNo=" + crawler.getBeanNo() + "to indexer.receiveLeafResource()");
			indexer.receiveBranchResource(url, retries, br, crawler);
		}
	}
	
	private String generateKey(Long runId){
		return String.format("Run_%1$d", runId);
	}

}
