package com.diefesson.filesync.server.task;

import java.nio.file.Path;

import com.diefesson.filesync.file.FileSystemBridge;
import com.diefesson.filesync.io.SyncConnection;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class ServerUploadTask implements Runnable {

	private final SyncConnection connection;
	private final FileSystemBridge fileSystemBridge;

	public ServerUploadTask(SyncConnection connection, FileSystemBridge fileSystemBridge) {
		this.connection = connection;
		this.fileSystemBridge = fileSystemBridge;
	}

	@Override
	public void run() {
		try (connection) {
			var in = connection.getIn();
			var path = Path.of(in.readUTF());
			try (var fileOut = fileSystemBridge.writeFile(path)) {
				in.transferTo(fileOut);
			}
		} catch (Exception e) {
			System.err.println("ServerUploadTask %s: error donwloading file from client");
			e.printStackTrace();
		}
	}

}
