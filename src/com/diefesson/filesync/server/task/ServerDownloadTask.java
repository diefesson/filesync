package com.diefesson.filesync.server.task;

import java.io.IOException;
import java.nio.file.Path;

import com.diefesson.filesync.file.FileSystemBridge;
import com.diefesson.filesync.io.SyncConnection;

/**
 * Resolves download requests
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class ServerDownloadTask implements Runnable {

	private final SyncConnection connection;
	private final FileSystemBridge fileSystemBridge;

	public ServerDownloadTask(SyncConnection connection, FileSystemBridge fileSystemBridge) {
		this.connection = connection;
		this.fileSystemBridge = fileSystemBridge;
	}

	@Override
	public void run() {
		try (connection) {
			var in = connection.getIn();
			var out = connection.getOut();
			var path = Path.of(in.readUTF());
			try (var fileIn = fileSystemBridge.readFile(path)) {
				fileIn.transferTo(out);
			}
		} catch (IOException e) {
			System.err.println("ServerDownloadTask %s: error while serving download to client");
			e.printStackTrace();
		}
	}
}
