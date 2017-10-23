package cn.easier.brow.sys.db.base;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

/**
* @author:     qiufh 
* @version:    1.0
*/
public class MyDataSourceFactory {
	public DataSource generateDataSource//
	(//
			String pooltype, //
			String dev, //
			String username, //
			String password, //
			String driverClassName, //
			String url, //
			int maxWait, //
			int maxActive, //
			int initialSize, //
			int maxIdle, //
			boolean testOnBorrow, //
			int minEvictableIdleTimeMillis, //
			int timeBetweenEvictionRunsMillis, //
			String connectionProperties//
	) {
		if ("dbcp".equalsIgnoreCase(pooltype)) {
			if ("test".equals(dev)) {//本地(测试)环境
				BasicDataSource dataSource = createDbcp(username, password, driverClassName, url, maxWait, maxActive, initialSize,
						maxIdle, testOnBorrow, minEvictableIdleTimeMillis, timeBetweenEvictionRunsMillis, connectionProperties);
				return dataSource;
			} else if ("dev".equals(dev)) {
				BasicDataSource dataSource = createDbcp(username, password, driverClassName, url, maxWait, maxActive, initialSize,
						maxIdle, testOnBorrow, minEvictableIdleTimeMillis, timeBetweenEvictionRunsMillis, connectionProperties);
				return dataSource;
			}
		}
		return null;
	}

	private BasicDataSource createDbcp(//
			String username, //
			String password, //
			String driverClassName, //
			String url, //
			int maxWait, //
			int maxActive, //
			int initialSize, //
			int maxIdle, //
			boolean testOnBorrow, //
			int minEvictableIdleTimeMillis, //
			int timeBetweenEvictionRunsMillis, //
			String connectionProperties//
	) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setMaxWait(maxWait);
		dataSource.setMaxActive(maxActive);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setConnectionProperties(connectionProperties);
		return dataSource;
	}
}
