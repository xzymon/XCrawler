package com.xzymon.xcrawler.ejb;

import java.io.IOException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jsoup.nodes.Document;

import com.xzymon.xcrawler.ejb.interceptor.EmbryoLogging;
import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;

@Stateless
@Local(LocalIndexer.class)
@Interceptors(EmbryoLogging.class)
public class IndexerBean implements BusinessIndexer {

	@EJB
	private LocalDownloader downloader;
	
	@EJB
	private LocalCrawler crawler;
	
	private long delayDuration;
	private int retrys;
	
	@PersistenceContext
	EntityManager em;
	
	@Override
	public void init(long delayDuration, int maxRetrysPerResource) {
		this.delayDuration = delayDuration;
		this.retrys = maxRetrysPerResource;
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
		
		TypedQuery<BranchResource> tquery = em.createQuery("from BranchResource br where br.url=:url", BranchResource.class);
		resource = tquery.getSingleResult();
		
		return resource;
	}
	
	private LeafResource findLeafResource(String url){
		LeafResource resource = null;
		
		TypedQuery<LeafResource> tquery = em.createQuery("from BranchResource br where br.url=:url", LeafResource.class);
		resource = tquery.getSingleResult();
		
		return resource;
	}

	@Override
	public boolean bookBranchResource(String url) {
		BranchResource resource = findBranchResource(url);
		if(resource == null){
			downloader.registerBranchToDownload(url, 1, delayDuration);
			crawler.increaseBranchTriggers();
		} else {
			receiveBranchResource(url, 0, resource);
		}
		return false;
	}

	@Override
	public boolean bookLeafResource(String url) {
		LeafResource resource = findLeafResource(url);
		if(resource == null){
			downloader.registerLeafToDownload(url, 1, delayDuration);
			crawler.increaseLeafTriggers();
		} else {
			receiveLeafResource(url, 0, resource);
		}
		return false;
	}

	@Override
	public void receiveBranchResource(String url, int currentRetriedCount, BranchResource resource) {
			if(resource == null){
				if(currentRetriedCount<retrys){
					downloader.registerBranchToDownload(url, currentRetriedCount+1, delayDuration);
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
	public void receiveLeafResource(String url, int currentRetriedCount, LeafResource resource) {
		if(resource == null){
			if(currentRetriedCount<retrys){
				downloader.registerLeafToDownload(url, currentRetriedCount+1, delayDuration);
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
