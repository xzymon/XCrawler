package com.xzymon.xcrawler.ejb;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.ejb.interceptor.DownloaderInterceptor;
import com.xzymon.xcrawler.ejb.interceptor.EmbryoLogging;
import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;
import com.xzymon.xcrawler.model.Resource;
import com.xzymon.xcrawler.model.Run;
import com.xzymon.xcrawler.util.CrawlingPolicy;
import com.xzymon.xcrawler.util.ResourcesSearchListener;
import com.xzymon.xcrawler.util.StatusReport;
import com.xzymon.xcrawler.util.StatusReportImpl;
import com.xzymon.xpath_searcher.core.XPathSearcher;
import com.xzymon.xpath_searcher.core.listener.XPathSearchingListener;

@Stateful
@Local(LocalCrawler.class)
@Remote(RemoteCrawler.class)
@Interceptors(EmbryoLogging.class)
public class CrawlerBean implements BusinessCrawler, LocalCrawler {
	private static final Logger logger = LoggerFactory.getLogger(CrawlerBean.class.getName());

	private CrawlingPolicy policy;
	private Run run;
	
	private long branchTriggers;
	private long leafTriggers;
	
	private long branchResDownloaded;
	private long leafResDownloaded;
	
	private LinkedHashSet<String> branchStringURLs;
	private LinkedHashSet<String> leafStringURLs;
	
	private int previousBranchOrder = -1;
	private int previousLeafOrder = -1;
	
	private String previousBranchStringURL = null;
	private String currentBranchStringURL = null;
	private String previousLeafStringURL = null;
	private String currentLeafStringURL = null;
	
	private Iterator<String> itBranch, itLeaf;
	
	@EJB
	private LocalIndexer indexer;
	
	@PersistenceContext
	private EntityManager em;
	
	public CrawlerBean(){
		branchStringURLs = new LinkedHashSet<String>();
		leafStringURLs = new LinkedHashSet<String>();
	}
	
	@Interceptors(DownloaderInterceptor.class)
	public Long startCrawling(CrawlingPolicy policy){
		Long runId = null;
		if(policy!=null){
			this.policy = policy;
			this.run = new Run();
			this.run.setRootURL(this.policy.getRootURL());
			this.run.setStarted(new Date());
			this.run.setSerializedPolicy(policy);
			em.persist(this.run);
			this.run = em.merge(this.run);
			this.indexer.init(policy.getTriggerTimeout(), policy.getMaxRetriesPerResource());
			this.branchStringURLs.add(this.run.getRootURL());
			crawlGetResource();
			return this.run.getId();
		}
		return runId;
	}
	
	@Interceptors(DownloaderInterceptor.class)
	private void crawlGetResource(){
		int branchOrder = 0;
		int leafOrder = 0;
		boolean branchExhausted = false;
		boolean leafExhausted = false;
		
		currentBranchStringURL = null;
		currentLeafStringURL = null;
		
		//pobierz kolejny branchResource z listy [String]
		if(previousBranchStringURL==null){
			itBranch = branchStringURLs.iterator();
			if(itBranch.hasNext()){
				currentBranchStringURL = itBranch.next();
			}
		} else {
			itBranch = branchStringURLs.iterator();
			String branchStringURL = null;
			while(itBranch.hasNext() && (branchStringURL=itBranch.next())!=previousBranchStringURL){
				branchOrder++;
			}
			if(branchStringURL==previousBranchStringURL){
				if(itBranch.hasNext()){
					currentBranchStringURL = itBranch.next();
				}
			} else {
				if(!itBranch.hasNext()){
					branchExhausted = true;
				}
			}
		}
		if(currentBranchStringURL!=null && currentBranchStringURL!=previousBranchStringURL){
			indexer.bookBranchResource(currentBranchStringURL);
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
		//<wyłom>
		//jeśli branchResource zostały już wyczerpane to przejdź do pobierania leafResource
		//pobierz kolejny leafResource z listy
		//zarządaj od indexera zasobu leafResource, podając ile razy ma próbować ponownie jeśli nie udało się pobrać zasobu
		
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
				
		//</wyłom>
		}
		
		if(currentBranchStringURL!=null){
			previousBranchStringURL = currentBranchStringURL;
		} else {
		
			if(previousLeafStringURL==null){
				itLeaf = leafStringURLs.iterator();
				if(itLeaf.hasNext()){
					currentLeafStringURL = itLeaf.next();
				}
			} else {
				itLeaf = leafStringURLs.iterator();
				String leafStringURL = null;
				while(itLeaf.hasNext() && (leafStringURL=itLeaf.next())!=previousLeafStringURL){
					leafOrder++;
				}
				if(leafStringURL==previousLeafStringURL){
					if(itLeaf.hasNext()){
						currentLeafStringURL = itLeaf.next();
					}
				} else {
					if(!itLeaf.hasNext()){
						leafExhausted = true;
					}
				}
			}
			if(currentLeafStringURL!=null && currentLeafStringURL!=previousLeafStringURL){
				indexer.bookLeafResource(currentLeafStringURL);
			}
			
			if(currentLeafStringURL!=null){
				previousLeafStringURL = currentLeafStringURL;
			}
		
		}
	}
	
	@Interceptors(DownloaderInterceptor.class)
	public void crawlBranchXPath(String url, BranchResource resource) {
		//wywołanie przeszukiwania XPath
			//dla każdego wyrażenia dot. branchResource wyszukaj elementy [które muszą być url-ami]
			//dodaj do listy [synchronizowanej?] LinkedHashSet
		if(resource!=null){
			InputStream is = new ByteArrayInputStream(resource.getData());
			List<String> xpathsList = policy.getBranchSpec();
			List<String> cssList = policy.getCssElementsListToRemove();
			List<XPathSearchingListener> xpathListeners = new LinkedList<XPathSearchingListener>();
			xpathListeners.add(new ResourcesSearchListener(branchStringURLs));
			try {
				XPathSearcher.htmlSearch(is, xpathsList, cssList, xpathListeners);
			} catch (XPathExpressionException e) {
				logger.warn("exception: XPathExpressionException");
				e.printStackTrace();
			}
		}
		crawlGetResource();
	}

	@Interceptors(DownloaderInterceptor.class)
	public void crawlLeafXPath(String url, LeafResource resource) {
		//wywołanie przeszukiwania XPath
			//dla każdego wyrażenia dot. leafResource wyszukaj elementy [które muszą być url-ami]
			//dodaj do listy [synchronizowanej?] LinkedHashSet
		if(resource!=null){
			InputStream is = new ByteArrayInputStream(resource.getData());
			List<String> xpathsList = policy.getLeafSpec();
			List<String> cssList = policy.getCssElementsListToRemove();
			List<XPathSearchingListener> xpathListeners = new LinkedList<XPathSearchingListener>();
			xpathListeners.add(new ResourcesSearchListener(leafStringURLs));
			try {
				XPathSearcher.htmlSearch(is, xpathsList, cssList, xpathListeners);
			} catch (XPathExpressionException e) {
				logger.warn("exception: XPathExpressionException");
				e.printStackTrace();
			}
		}
		crawlGetResource();
	}
	
	public Run getRun() {
		return run;
	}

	@Interceptors(DownloaderInterceptor.class)
	@Override
	public StatusReport getStatusReport() {
		StatusReportImpl sr = new StatusReportImpl();
		sr.setMilisFromStart((new Date().getTime() - this.run.getStarted().getTime()));
		sr.setBranchResourceDownloadTriggersCount(this.branchTriggers);
		sr.setLeafResourceDownloadTriggersCount(this.leafTriggers);
		sr.setSuccessfullyDownloadedBranchResourcesCount(this.branchResDownloaded);
		sr.setSuccessfullyDownloadedLeafResourcesCount(this.leafResDownloaded);
		return sr;
	}

	@Override
	public void increaseBranchTriggers() {
		this.branchTriggers++;
	}

	@Override
	public void increaseLeafTriggers() {
		this.leafTriggers++;
	}

	@Override
	public InputStream getResourceAsInputStream(String url, long runId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void increaseBranchDownloaded() {
		this.branchResDownloaded++;
	}

	@Override
	public void increaseLeafDownloaded() {
		this.leafResDownloaded++;
	}

	
	
}
