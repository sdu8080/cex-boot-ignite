package com.cex.service;

import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.eviction.lru.LruEvictionPolicy;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStore;
import org.apache.ignite.cache.store.jdbc.CacheJdbcPojoStoreFactory;
import org.apache.ignite.cache.store.jdbc.dialect.MySQLDialect;
import org.apache.ignite.configuration.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cex.ignite.config.ConfigProperties;
import com.cex.model.Card;
import com.cex.model.CardKey;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

public class CacheConfigWrapper<K, V> {
	private static Logger logger = LoggerFactory
			.getLogger(CacheConfigWrapper.class);

	CacheConfiguration<K, V> cfg = null;

	public CacheConfigWrapper(String cacheName, int size) {
		try {
			// Configure cache store.
			cfg = com.cex.model.CacheConfig.cache(cacheName,
					new MySQLStoreFactory<K, V>());

			cfg.setWriteBehindEnabled(true);
			cfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);

			// partitioned with 1 backup for each partition
			cfg.setCacheMode(CacheMode.PARTITIONED);
			cfg.setBackups(1);

			cfg.setEvictionPolicy(new LruEvictionPolicy<CardKey, Card>(size));

		} catch (Exception e) {
			logger.error("failed to initialize cache configuration for "
					+ cacheName);
		}
	}
	
	public CacheConfiguration<K, V> getConfig(){
		return cfg;
	}
	
	
	/**
	 * Constructs and returns a fully configured instance of a
	 * {@link CacheJdbcPojoStoreFactory}.
	 */
	public static class MySQLStoreFactory<K, V> extends
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
