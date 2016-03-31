package com.cex.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cex.model.Card;
import com.cex.model.CardKey;
import com.cex.model.CardTransaction;
import com.cex.model.CardTransactionKey;

@Service
public class CacheRepository {

	public static final String cardCache = "CARD_CACHE";

	public static final String transactionCache = "CARD_TRANSACTION_CACHE";

	private static Logger logger = LoggerFactory
			.getLogger(CacheRepository.class);

	@Autowired
	private IgniteService igniteService;

	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<String, IgniteCache> caches = new ConcurrentHashMap<>();

	@PostConstruct
	public void init() {
		logger.info("initialize all cches.");
		Ignite igniteServer = igniteService.getIgnite();
		
		
		CacheConfigWrapper<CardTransactionKey, CardTransaction> twp = new CacheConfigWrapper<>(
				transactionCache, 1000000);
		IgniteCache<CardTransactionKey, CardTransaction> txnICache = igniteServer
				.getOrCreateCache(twp.getConfig());
		caches.put(transactionCache, txnICache);
		
		
		CacheConfigWrapper<CardKey, Card> cwp = new CacheConfigWrapper<>(
				cardCache, 1000000);
		IgniteCache<CardKey, Card> cardICache = igniteServer
				.getOrCreateCache(cwp.getConfig());
		caches.put(cardCache, cardICache);

		
		logger.info("caches have been initialized");
	}

	@SuppressWarnings("rawtypes")
	@PreDestroy
	public void stop() {
		Collection<IgniteCache> c = caches.values();
		if(c !=null && c.size()>0){
			for (Iterator iterator = c.iterator(); iterator.hasNext();) {
				IgniteCache cache = (IgniteCache) iterator.next();
				cache.close();
		    }
		}

	}

	public IgniteCache<CardKey, Card> getCardCache() {
		@SuppressWarnings("unchecked")
		IgniteCache<CardKey, Card> igniteCache = (IgniteCache<CardKey, Card>) caches
				.get(cardCache);
		return igniteCache;
	}

	public IgniteCache<CardTransactionKey, CardTransaction> getCardTransactionCache() {
		@SuppressWarnings("unchecked")
		IgniteCache<CardTransactionKey, CardTransaction> igniteCache = (IgniteCache<CardTransactionKey, CardTransaction>) caches
				.get(transactionCache);
		return igniteCache;
	}
}
