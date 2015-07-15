package com.xzymon.xcrawler.ejb;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.ejb.interceptor.EmbryoLogging;
import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;
import com.xzymon.xcrawler.util.CrawlingPolicy;

@Stateless
@Local(LocalIndexer.class)
@Interceptors(EmbryoLogging.class)
public class IndexerBean implements BusinessIndexer {
	private static final Logger logger = LoggerFactory.getLogger(IndexerBean.class.getName());

	@EJB
	private LocalDownloader downloader;
	
	@EJB
	private LocalCrawler crawler;
	
	@Resource
	SessionContext ctx;
	
	private long delayDuration;
	private int retries;
	
	@PersistenceContext
	EntityManager em;
	
	public IndexerBean(){
		logger.info("IndexerBean created.");
	}
	
	//zarządaj od indexera zasobu branchResource, podając ile razy ma próbować ponownie jeśli nie udało się pobrać zasobu
	
			//niech indexer zwróci informację o tym czy zasób jest mu znany czy musi go pobrać
				//indexer odpytuje bazę
				//jeśli zasób jest w bazie to trzeba go asynchronicznie zwrócić - ale od razu
				//jeśli zasobu nie ma w bazie to trzeba próbować go pobrać mając do dyspozycji pewną ilość prób
					//odwołać się do downloadera - niech ustawi @Timeout do pobrania - jeśli całość ma działać asynchronicznie
					//albo niech wywoła synchronicznie metodę @Timeout - jeśli całość ma działać synchronicznie
					//metoda @Timeout:
						//ściąga zasób
						//uzyskuje dostęp do Indexera (DI lub JNDI)
						//wywołuje metodę callback, przekazując: String URL zasobu do pobrania oraz (pobrany zasób lub null jeśli nie udało się pobrać)
					//jeśli pobranie się nie powiodło to trzeba ponowić próbę pobrania taką ilość razy na jaką pozwala CrawlingPolicy
			
	
	private void storeBranchResource(BranchResource resource){
		resource.setRun(crawler.getRun());
		em.persist(resource);
	}
	
	private void storeLeafResource(LeafResource resource){
		em.persist(resource);
	}
	
	private BranchResource findBranchResource(String url){
		BranchResource resource = null;
		String qstr = "from BranchResource br where br.url=:url";
		try{
			TypedQuery<BranchResource> tquery = em.createQuery(qstr, BranchResource.class);
			tquery.setParameter("url", url);
			resource = tquery.getSingleResult();
		} catch(NoResultException ex){
			logger.info("Query \"" + qstr + "\" returned no result.");
			return null;
		}
		return resource;
	}
	
	private LeafResource findLeafResource(String url){
		LeafResource resource = null;
		String qstr = "from BranchResource br where br.url=:url";
		try{
			TypedQuery<LeafResource> tquery = em.createQuery(qstr, LeafResource.class);
			tquery.setParameter("url", url);
			resource = tquery.getSingleResult();
		} catch(NoResultException ex){
			logger.info("Query \"" + qstr + "\" returned no result.");
			return null;
		}
		return resource;
	}

	@Override
	public boolean bookBranchResource(String url, BusinessCrawler crawler) {
		BranchResource resource = findBranchResource(url);
		if(resource == null){
			logger.info("Registering URL to download: " + url);
			downloader.registerBranchToDownload(url, 1, crawler.getPolicy().getTriggerTimeout(), crawler);
			crawler.increaseBranchTriggers();
		} else {
			receiveBranchResource(url, 0, resource, crawler);
		}
		return false;
	}

	@Override
	public boolean bookLeafResource(String url, BusinessCrawler crawler) {
		LeafResource resource = findLeafResource(url);
		if(resource == null){
			logger.info("Registering URL to download: " + url);
			downloader.registerLeafToDownload(url, 1, crawler.getPolicy().getTriggerTimeout(), crawler);
			crawler.increaseLeafTriggers();
		} else {
			receiveLeafResource(url, 0, resource, crawler);
		}
		return false;
	}

	@Override
	public void receiveBranchResource(String url, int currentRetriedCount, BranchResource resource, BusinessCrawler crawler) {
			if(resource == null){
				if(currentRetriedCount<retries){
					downloader.registerBranchToDownload(url, currentRetriedCount+1, delayDuration, crawler);
					crawler.increaseBranchTriggers();
				} else {
					//NOTE: resource == null
					crawler.crawlBranchXPath(url, resource);
				}
			} else {
				storeBranchResource(resource);
				if(currentRetriedCount>0){
					crawler.increaseBranchDownloaded();
				}
				crawler.crawlBranchXPath(url, resource);
			}
	}

	@Override
	public void receiveLeafResource(String url, int currentRetriedCount, LeafResource resource, BusinessCrawler crawler) {
		if(resource == null){
			if(currentRetriedCount<retries){
				downloader.registerLeafToDownload(url, currentRetriedCount+1, delayDuration, crawler);
				crawler.increaseLeafTriggers();
			} else {
				crawler.crawlLeafXPath(url, null);
			}
		} else {
			storeLeafResource(resource);
			if(currentRetriedCount>0){
				crawler.increaseLeafDownloaded();
			}
			crawler.crawlLeafXPath(url, resource);
		}
	}
	
}
