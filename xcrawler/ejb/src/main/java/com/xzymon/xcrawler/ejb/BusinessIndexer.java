package com.xzymon.xcrawler.ejb;

import com.xzymon.xcrawler.model.BranchResource;
import com.xzymon.xcrawler.model.LeafResource;

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


public interface BusinessIndexer {
	void init(long delayDuration, int maxRetrysPerResource);
	
	boolean bookBranchResource(String url);
	boolean bookLeafResource(String url);
	
	void receiveBranchResource(String url, int currentRetriedCount, BranchResource resource);
	void receiveLeafResource(String url, int currentRetriedCount, LeafResource resource);
	
}
