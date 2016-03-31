package com.cex.ignite.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

	private static Properties properties = null;
	
	public static final String configFile = "CONFIG_FILE";

	public static final String cacheName = "CACHE_NAME";

	public static final String dbUrl = "DB_URL";

	public static final String dbUser = "DB_USER";

	public static final String dbPwd = "DB_PWD";

	static {
		properties = new Properties();
		try (final InputStream stream = ConfigProperties.class.getClassLoader().
				getResourceAsStream("application.properties")) {
			properties.load(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getProperty(String key){
		return properties.getProperty(key);
	}
}
