package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import exception.FailedConnectionException;

public class ConnectionPool {

	/**
	 * Singleton Connect to DB server and DB url using parameters from file. pool of
	 * 10 connections + get url string from file.
	 **/

	private static ConnectionPool instance;
	private static final long maxConnections = 10;
	private static Set<Connection> connections;
	private static String databaseUrl;

	/**
	 * Constructor Get params for DB server
	 * 
	 * @throws FailedConnectionException
	 */
	private ConnectionPool() throws FailedConnectionException {
		connections = new HashSet<Connection>();

		try {
			/**
			 * Get driver from file
			 */

			databaseUrl = Database.getDBURL();

			/**
			 * Get and Set connections in array Initiate maxConnection Connections
			 */
			for (int i = 0; i < maxConnections; i++) {
				connections.add(DriverManager.getConnection(databaseUrl));
			}

		} catch (Exception e) {
			throw new FailedConnectionException("Connection Pool Startup Error");
		}
	}

	/**
	 * @return ConnectionPool - SINGLETON instance
	 */
	public static ConnectionPool getInstance() throws FailedConnectionException {
		if (instance == null)
			instance = new ConnectionPool();
		return instance;
	}

	/**
	 * Methods get from Connection pool
	 * 
	 * @return Connection
	 * @throws FailedConnectionException
	 */
	public synchronized Connection getConnection() throws FailedConnectionException {

		while (connections.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
		}

		Iterator<Connection> iterator = connections.iterator();
		Connection con = iterator.next();
		iterator.remove();
		return con;
	}

	/**
	 * Methods return connection to Connection pool
	 * 
	 * @throws FailedConnectionException
	 */
	public synchronized void returnConnection(Connection con) throws FailedConnectionException {
		try {
			con.setAutoCommit(true);
		} catch (SQLException e) {
			throw new FailedConnectionException("ERROR! Return Connection Properly Failed!");
		}
		connections.add(con);
		this.notify();
	}

	/**
	 * Close all Connections
	 * 
	 * @throws FailedConnectionException
	 */
	public synchronized void closeAllConnections() throws FailedConnectionException {

		while (connections.size() == 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("Interrupted");
			}
		}

		Iterator<Connection> iterator = connections.iterator();
		while (iterator.hasNext()) {
			try {
				iterator.next().close();
			} catch (SQLException e) {
				throw new FailedConnectionException("Connections: Close All Connection: Error!");
			}
		}
	}
}
