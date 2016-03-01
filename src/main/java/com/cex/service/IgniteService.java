package com.cex.service;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStore;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.NearCacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cex.ignite.config.ConfigProperties;
import com.cex.ignite.model.CacheConfig;
import com.cex.ignite.model.Transaction;
import com.cex.ignite.model.TransactionKey;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

/**
 * Ignite service to management the lifecycle of both client and server side ignite grids.
 * 
 * @author sdu0000
 */
@Service
public class IgniteService {

	private static Logger logger = LoggerFactory.getLogger(IgniteService.class);

	private IgniteCache<TransactionKey, Transaction> cache = null;

	private IgniteCache<TransactionKey, Transaction> nearCache = null;

	private boolean initialized = false;

	private Ignite igniteServer = null;

	private Ignite igniteClient = null;

	private final String CLIENT_GRID = "client-grid";

	@PostConstruct
	public void init() {

		if (!initialized) {
			logger.info("initialize the ignite service using "
					+ ConfigProperties.getProperty(ConfigProperties.configFile));

			try {
				// start server ignite
				igniteServer = Ignition.start(ConfigProperties
						.getProperty(ConfigProperties.configFile));

				// start client ignite
				IgniteConfiguration ic = new IgniteConfiguration();
				ic.setClientMode(true);
				ic.setGridName(CLIENT_GRID);
				ic.setPeerClassLoadingEnabled(true);
				igniteClient = Ignition.start(ic);

				// start both caches
				initTxnCache();
			} catch (IgniteException e) {
				e.printStackTrace();
			}
			initialized = true;
		}

	}

	private void initTxnCache() {
		try {

			String cacheName = ConfigProperties
					.getProperty(ConfigProperties.cacheName);

			int nearCacheSize = Integer.parseInt(ConfigProperties
					.getProperty("NEARCACHE_SIZE"));

			// Configure cache store.
			CacheConfiguration<TransactionKey, Transaction> cfg = CacheConfig
					.cache(cacheName,
							new MySQLStoreFactory<TransactionKey, Transaction>());
			cfg.setWriteBehindEnabled(true);
			cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

			// partitioned with 1 backup for each partition
			cfg.setCacheMode(CacheMode.PARTITIONED);
			cfg.setBackups(1);
			
			cfg.setEvictionPolicy(new LruEvictionPolicy<TransactionKey, Transaction>(1000000));

			// create a server partitioned cache
			cache = igniteServer.getOrCreateCache(cfg);

			// create client side near cache before get the server txn cache
			NearCacheConfiguration<TransactionKey, Transaction> nearCfg = new NearCacheConfiguration<>();
			nearCfg.setNearEvictionPolicy(new LruEvictionPolicy<TransactionKey, Transaction>(
					nearCacheSize));
			nearCache = igniteClient.getOrCreateNearCache(cacheName, nearCfg);

			logger.info("cache created...");
			
			// warm up cache with "ACT" status
			initialLoad();

		} catch (Exception e) {
			logger.error("failed to create ignite cache.", e);
		}

	}
	
	private void initialLoad(){
		
		cache.loadCache(null, TransactionKey.class, "select * from transaction where card_status='ACT'");
		
		logger.info("finish preload...");
	}

	@PreDestroy
	public void stop() {
		logger.info("shut down ignite service.");
		finish();
	}

	private void finish() {

		if (nearCache != null) {
			try {
				nearCache.close();
			} catch (Exception e) {
				logger.error("failed to close the nearCache", e);
			}
		}

		if (cache != null) {
			try {
				cache.close();
			} catch (Exception e) {
				logger.error("failed to close the txnCache", e);
			}
		}

		if (igniteClient != null) {
			try {
				igniteClient.close();
			} catch (IgniteException e) {
				logger.error("failed to close the igniteClient instance", e);
			}
		}

		if (igniteServer != null) {
			try {
				igniteServer.close();
			} catch (IgniteException e) {
				logger.error("failed to close the ignite instance", e);
			}
		}
	}

	public IgniteCache<TransactionKey, Transaction> getTxnCache() {
		return cache;
	}

	public IgniteCache<TransactionKey, Transaction> getNearCache() {
		return nearCache;
	}
	
	public Ignite getIgnite(){
		return igniteServer;
	}

	/**
	 * Constructs and returns a fully configured instance of a
	 * {@link CacheJdbcPojoStoreFactory}.
	 */
	private static class MySQLStoreFactory<K, V> extends
			CacheJdbcPojoStoreFactory<K, V> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -305731694994951366L;

		/** {@inheritDoc} */
		@Override
		public CacheJdbcPojoStore<K, V> create() {
			setDialect(new MySQLDialect());

			MysqlConnectionPoolDataSource ds = new MysqlConnectionPoolDataSource();
			ds.setURL(ConfigProperties.getProperty(ConfigProperties.dbUrl));
			ds.setUser(ConfigProperties.getProperty(ConfigProperties.dbUser));
			ds.setPassword(ConfigProperties.getProperty(ConfigProperties.dbPwd));

			setDataSource(ds);

			return super.create();
		}
	}
}
