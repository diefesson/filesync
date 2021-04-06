package com.diefesson.filesync.task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import com.diefesson.filesync.file.FileSystemBridge;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.ProtocolConstants;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class DownloadTask implements Callable<Void> {

	private final SyncConnection connection;
	private final FileSystemBridge fileSystemBridge;
	private final Path path;

	public DownloadTask(SyncConnection connection, FileSystemBridge fileSystemBridge, Path path) {
		this.connection = connection;
		this.fileSystemBridge = fileSystemBridge;
		this.path = path;
	}

	public Void call() throws IOException {
		try (connection) {
			var in = connection.getIn();
			var out = connection.getOut();
			out.write(ProtocolConstants.DOWNLOAD_REQUEST);
			out.writeUTF(path.toString());
			try (var fileOut = fileSystemBridge.writeFile(path)) {
				in.transferTo(fileOut);
			}
			return null;
		}
	}

}
