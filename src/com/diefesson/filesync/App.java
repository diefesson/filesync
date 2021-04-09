package com.diefesson.filesync;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.diefesson.filesync.file.FileStructure;
import com.diefesson.filesync.file.FileSystemBridge;
import com.diefesson.filesync.io.AuthException;
import com.diefesson.filesync.io.ConnectionAuthenticator;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.ConnectionDispatcher;
import com.diefesson.filesync.server.Server;
import com.diefesson.filesync.task.DownloadTask;
import com.diefesson.filesync.task.ListTask;
import com.diefesson.filesync.task.SyncListener;
import com.diefesson.filesync.task.SyncTask;
import com.diefesson.filesync.task.UploadTask;
import com.diefesson.filesync.user.UserManager;

/**
 * 
 * @author Diefesson de Sousa Silva
 * 
 * 
 */
public class App {

	private final UserManager userManager;
	private final FileSystemBridge fileSystemBridge;
	private final ConnectionAuthenticator connectionAuthenticator;
	private final ExecutorService executorService;
	private final Map<Integer, Server> servers;

	public App(Path root) throws IOException {
		userManager = new UserManager();
		userManager.loadPrefs();
		connectionAuthenticator = new ConnectionAuthenticator(userManager);
		fileSystemBridge = new FileSystemBridge(root.resolve("files"));
		executorService = Executors.newFixedThreadPool(8);
		servers = new HashMap<>();
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public FileSystemBridge getFileSystemBridge() {
		return fileSystemBridge;
	}

	public ConnectionAuthenticator getConnectionAuthenticator() {
		return connectionAuthenticator;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void listen(int port) {
		unlisten(port);
		try {
			var server = new Server(connectionAuthenticator);
			server.setOnConnect(new ConnectionDispatcher(executorService, fileSystemBridge));
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

	public Future<Void> download(String address, int port, Path path) throws IOException, AuthException {
		var connection = createConnection(address, port);
		return executorService.submit(new DownloadTask(connection, fileSystemBridge, path));
	}

	public Future<Void> upload(String address, int port, Path path) throws IOException, AuthException {
		var connection = createConnection(address, port);
		return executorService.submit(new UploadTask(connection, fileSystemBridge, path));
	}

	public Future<FileStructure> listRemote(String address, int port) throws IOException, AuthException {
		var connection = createConnection(address, port);
		return executorService.submit(new ListTask(connection));
	}

	public Future<?> sync(String address, int port, SyncListener listener) {
		return executorService.submit(new SyncTask(this, address, port, listener));
	}

	public SyncConnection createConnection(String address, int port) throws IOException, AuthException {
		var connection = new SyncConnection(address, port);
		if (connectionAuthenticator.authenticate(connection)) {
			return connection;
		} else {
			connection.close();
			throw new AuthException("Authentication failed");
		}
	}

}
