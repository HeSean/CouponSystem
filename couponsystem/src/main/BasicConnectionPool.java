package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicConnectionPool implements ConnectionPool {

	private final String url;

	private final List<Connection> connectionPool;
	private final List<Connection> usedConnections = new ArrayList<>();
	private static final int INITIAL_POOL_SIZE = 10;
	private final int MAX_POOL_SIZE = 10;

	public static BasicConnectionPool create(String url) throws SQLException {
		List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
		for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
			pool.add(createConnection(url));
		}
		return new BasicConnectionPool(url, pool);
	}

	private BasicConnectionPool(String url, List<Connection> connectionPool) {
		this.url = url;

		this.connectionPool = connectionPool;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (connectionPool.isEmpty()) {
			if (usedConnections.size() < MAX_POOL_SIZE) {
				connectionPool.add(createConnection(url));
			} else {
				throw new RuntimeException("Maximum pool size reached, no available connections!");
			}
		}

		Connection connection = connectionPool.remove(connectionPool.size() - 1);
		usedConnections.add(connection);
		return connection;
	}

	@Override
	public boolean releaseConnection(Connection connection) {
		connectionPool.add(connection);
		return usedConnections.remove(connection);
	}

	private static Connection createConnection(String url) throws SQLException {
		return DriverManager.getConnection(url);
	}

	@Override
	public int getSize() {
		return connectionPool.size() + usedConnections.size();
	}

	@Override
	public List<Connection> getConnectionPool() {
		return connectionPool;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void closeAllConnections() throws SQLException {
		usedConnections.forEach(this::releaseConnection);
		for (Connection c : connectionPool) {
			c.close();
		}
		connectionPool.clear();
	}
}
