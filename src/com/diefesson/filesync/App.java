package com.diefesson.filesync;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.diefesson.filesync.file.FileScanner;
import com.diefesson.filesync.file.FileSynchronizer;
import com.diefesson.filesync.server.ConnectionDispatcher;
import com.diefesson.filesync.server.Server;
import com.diefesson.filesync.task.DownloadTask;
import com.diefesson.filesync.task.UploadTask;

/**
 * 
 * @author Diefesson de Sousa Silva
 * 
 * 
 */
public class App {

	private final String root;
	private final FileSynchronizer synchronizer;
	private final ExecutorService executor;
	private final Map<Integer, Server> servers;

	public App(String root) {
		this.root = root;
		synchronizer = new FileSynchronizer(root);
		executor = Executors.newFixedThreadPool(8);
		servers = new HashMap<>();
	}

	public void scan() {
		var scanner = new FileScanner(root);
		scanner.setOnFile((path) -> {
			System.out.println("Found file: %s".formatted(path));
			synchronizer.add(path);
		});
		scanner.run();
	}

	public void listen(int port) {
		unlisten(port);
		try {
			var server = new Server();
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

	public void download(String address, int port, String path) {
		var task = new DownloadTask(address, port, path, synchronizer);
		executor.execute(task);
	}

	public void upload(String address, int port, String path) {
		var task = new UploadTask(address, port, path, synchronizer);
		executor.execute(task);
	}

	public Set<Integer> getPorts() {
		return servers.keySet();
	}

}
