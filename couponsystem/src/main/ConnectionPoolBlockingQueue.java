package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import exception.FailedConnectionException;

public class ConnectionPoolBlockingQueue {

	private String databaseUrl = Database.getDBURL();
	private static Lock lock = new ReentrantLock(true);
	private static AtomicBoolean flag = new AtomicBoolean(true);
	private final int POOL_SIZE = 10;
	private BlockingQueue<Connection> connections;
	private static ConnectionPoolBlockingQueue ConnectionPoolBlockingQueue;

	private ConnectionPoolBlockingQueue() {
		connections = new ArrayBlockingQueue<>(POOL_SIZE);
		for (int i = 0; i < POOL_SIZE; i++) {
			Connection connection = null;
			try {
				connection = DriverManager.getConnection(databaseUrl);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			connections.offer(connection);
		}
	}

	/**
	 * Getting connection pool
	 *
	 * @return
	 */
	public static ConnectionPoolBlockingQueue getInstance() throws FailedConnectionException {
		if (flag.get()) {
			lock.lock();
			try {
				if (ConnectionPoolBlockingQueue == null) {
					ConnectionPoolBlockingQueue = new ConnectionPoolBlockingQueue();
					flag.set(false);
				}
			} finally {
				lock.unlock();
			}
		}
		return ConnectionPoolBlockingQueue;

	}

	/**
	 * Taking connection from connection pool
	 *
	 * @return
	 */
	public Connection getConnection() throws FailedConnectionException {
		Connection connection = null;
		try {
			connection = connections.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * Release connection into connection pool
	 *
	 * @param connection
	 */
	public void returnConnection(Connection connection) throws FailedConnectionException {
		if (connection != null) {
			connections.offer(connection);
		}
	}

	/**
	 * Remove all connections from connections queue
	 */
	public void shutDownConnections() {
		connections.stream().filter(connection -> connection != null).forEach(connections::remove);
	}

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
		System.out.println("Closed all open connections.");
	}
}
