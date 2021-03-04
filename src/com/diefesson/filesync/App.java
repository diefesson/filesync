package com.diefesson.filesync;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.diefesson.filesync.file.AcessSynchronizer;
import com.diefesson.filesync.io.AuthException;
import com.diefesson.filesync.io.ConnectionAuthenticator;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.ConnectionDispatcher;
import com.diefesson.filesync.server.Server;
import com.diefesson.filesync.task.DownloadTask;
import com.diefesson.filesync.task.UploadTask;
import com.diefesson.filesync.user.UserManager;

/**
 * 
 * @author Diefesson de Sousa Silva
 * 
 * 
 */
public class App {

	private final AcessSynchronizer synchronizer;
	private final UserManager userManager;
	private final ConnectionAuthenticator authenticator;
	private final ExecutorService executor;
	private final Map<Integer, Server> servers;

	public App(AcessSynchronizer acessSynchronizer, UserManager userManager) {
		this.synchronizer = acessSynchronizer;
		this.userManager = userManager;
		this.authenticator = new ConnectionAuthenticator(userManager);
		executor = Executors.newFixedThreadPool(8);
		servers = new HashMap<>();
	}

	public void listen(int port) {
		unlisten(port);
		try {
			var server = new Server(authenticator);
			server.setOnConnect(new ConnectionDispatcher(synchronizer, executor));
			server.start("0.0.0.0", port);
			servers.put(port, server);
		} catch (IOException e) {
			System.err.println("App %d: could not start server");
		}
	}

	public void unlisten(int port) {
		var server = servers.remove(port);
		if (server != null)
			server.stop();
	}

	public Set<Integer> getPorts() {
		return servers.keySet();
	}

	public AcessSynchronizer getSynchronizer() {
		return synchronizer;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public Future<?> download(String address, int port, String path) throws IOException, AuthException {
		var connection = createConnection(address, port);
		return executor.submit(new DownloadTask(path, connection, synchronizer));
	}

	public Future<?> upload(String address, int port, String path) throws IOException, AuthException {
		var connection = createConnection(address, port);
		return executor.submit(new UploadTask(path, connection, synchronizer));
	}

	private SyncConnection createConnection(String address, int port) throws IOException, AuthException {
		var connection = new SyncConnection(address, port);
		if (authenticator.authenticate(connection)) {
			return connection;
		} else {
			connection.close();
			throw new AuthException("Authentication failed");
		}
	}

}
